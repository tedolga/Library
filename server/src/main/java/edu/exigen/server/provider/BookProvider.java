package edu.exigen.server.provider;

import edu.exigen.client.entities.Book;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;

import java.util.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookProvider {

    private Map<String, Book> isbnCash = new HashMap<String, Book>();
    private Map<String, HashSet<Book>> searchCash = new HashMap<String, HashSet<Book>>();
    private BookDAO bookDAO;
    private ReservationRecordProvider recordProvider;

    public BookProvider(BookDAO bookDAO, ReservationRecordProvider recordProvider) {
        this.bookDAO = bookDAO;
        this.recordProvider = recordProvider;
    }

    public void createBook(Book book) throws LibraryProviderException {
        checkISBNCount(book);
        Book sameBook = isbnCash.get(book.getIsbn());
        if (sameBook == null) {
            try {
                bookDAO.createBook(book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            addBookToSearchCash(book);
            addToISBNCash(book);
        } else {
            book.setCount(sameBook.getCount() + book.getCount());
            book.setId(sameBook.getId());
            try {
                bookDAO.updateBook(sameBook.getId(), book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(sameBook, book);
            updateSearchCash(sameBook, book);
        }
    }

    public void updateBook(Book oldBook, Book newBook) throws LibraryProviderException {
        Book copyOld = oldBook.copyBook();
        if (newBook.getCount() <= 5) {
            try {
                bookDAO.updateBook(oldBook.getId(), newBook);
                newBook.setId(oldBook.getId());
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(copyOld, newBook);
            updateSearchCash(copyOld, newBook);
        } else {
            throw new LibraryProviderException("Count of books with ISBN " + newBook.getIsbn() + " can't be updated up " +
                    "to " + newBook.getCount() + ", maximum count of " +
                    "book copies should be less than 5, you can add no more than 5 books.");
        }
    }

    public void deleteBooks(Book book, int deleteCount) throws LibraryProviderException {
        int bookCount = getBookCount(book);
        checkDeletionChance(book, deleteCount);
        if (bookCount <= deleteCount) {
            try {
                bookDAO.delete(book.getId());
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            removeFromISBNCash(book);
            removeBookFromSearchCash(book);
        } else {
            Book copy = book.copyBook();
            book.setCount(bookCount - deleteCount);
            try {
                bookDAO.updateBook(book.getId(), book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(copy, book);
            updateSearchCash(copy, book);
        }
    }

    public int getBookCount(Book book) {
        return (isbnCash.get(book.getIsbn()) != null) ? isbnCash.get(book.getIsbn()).getCount() : 0;
    }

    public List<Book> searchBooks(String searchString) {
        String[] searchTokens = searchString.toLowerCase().split(" ");
        Set<Book> resultSet = new HashSet<Book>();
        for (String token : searchTokens) {
            Set<Book> foundBooks = searchCash.get(token);
            if (foundBooks != null) {
                resultSet.addAll(foundBooks);
            }
        }
        List<Book> resultList = new ArrayList<Book>();
        resultList.addAll(resultSet);
        return resultList;
    }

    public List<Book> readAll() {
        return bookDAO.readAll();
    }

    public void loadData() throws LibraryProviderException {
        try {
            bookDAO.loadStorage();
            recordProvider.loadData();
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        List<Book> books = readAll();
        for (Book book : books) {
            isbnCash.put(book.getIsbn(), book);
            addBookToSearchCash(book);
        }
    }

    private void updateISBNCash(Book oldBook, Book newBook) {
        removeFromISBNCash(oldBook);
        addToISBNCash(newBook);
    }

    private void removeFromISBNCash(Book oldBook) {
        isbnCash.remove(oldBook.getIsbn());
    }

    private void updateSearchCash(Book oldBook, Book newBook) {
        removeBookFromSearchCash(oldBook);
        addBookToSearchCash(newBook);
    }

    private void checkISBNCount(Book book) throws LibraryProviderException {
        int isbnCount = getBookCount(book);
        int newBookCount = book.getCount();
        if (!(isbnCount + newBookCount <= 5)) {
            throw new LibraryProviderException(newBookCount + " books with ISBN " + book.getIsbn() + " can't be added to library, maximum count of " +
                    "book copies should be less than 5, you can add no more than " + (5 - isbnCount) + " book(s).");
        }
    }

    private void addToISBNCash(Book book) {
        isbnCash.put(book.getIsbn(), book);
    }

    private void addBookToSearchCash(Book book) {
        addWordsToSearchCash(getBookTokens(book), book);
    }


    private void removeBookFromSearchCash(Book book) {
        removeTokensFromSearchCash(getBookTokens(book), book);
    }

    private void removeTokensFromSearchCash(Iterable<String> tokens, Book book) {
        for (String word : tokens) {
            HashSet<Book> books = searchCash.get(word);
            if (books != null) {
                books.remove(book);
                if (books.size() < 1) {
                    searchCash.remove(word);
                }
            }
        }
    }

    private void addWordsToSearchCash(Iterable<String> tokens, Book book) {
        for (String word : tokens) {
            HashSet<Book> books = searchCash.get(word);
            if (books != null) {
                books.add(book);
            } else {
                books = new HashSet<Book>();
                books.add(book);
                searchCash.put(word, books);
            }
        }
    }

    private Iterable<String> getBookTokens(Book book) {
        List<String> tokens = new ArrayList<String>();
        addFieldTokens(tokens, book.getIsbn().toLowerCase());
        addFieldTokens(tokens, book.getTitle().toLowerCase());
        addFieldTokens(tokens, book.getAuthor().toLowerCase());
        addFieldTokens(tokens, book.getTopic().toLowerCase());
        addFieldTokens(tokens, String.valueOf(book.getYear()).toLowerCase());
        return tokens;
    }

    private void addFieldTokens(Collection<String> allTokens, String field) {
        String[] tokenArray = field.split(" ");
        Collections.addAll(allTokens, tokenArray);
    }

    private void checkDeletionChance(Book book, int deleteCount) throws LibraryProviderException {
        int bookCount = getBookCount(book);
        int reservedCount = recordProvider.getReservedBookCount(book.getId());
        int availableCount = bookCount - reservedCount;
        if (availableCount < deleteCount) {
            throw new LibraryProviderException("Can't delete " + deleteCount + " copies of book with ISBN " + book.getIsbn()
                    + ", only " + availableCount + " can be deleted.");
        }
    }
}

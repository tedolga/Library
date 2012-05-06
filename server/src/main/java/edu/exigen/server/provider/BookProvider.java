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

    public BookProvider(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void createBook(Book book) throws LibraryProviderException {
        checkISBNCount(book);
        Book sameBook = isbnCash.get(book.getIsbn());
        if (sameBook == null) {
            int id;
            try {
                id = bookDAO.createBook(book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            book.setId(id);
            addBookToSearchCash(book);
        } else {
            book.setCount(sameBook.getCount() + book.getCount());
            try {
                bookDAO.updateBook(sameBook.getId(), book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
        }
        addToISBNCash(book);
    }

    public void updateBook(Book oldBook, Book newBook) throws LibraryProviderException {
        if (newBook.getCount() <= 5) {
            try {
                bookDAO.updateBook(oldBook.getId(), newBook);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(oldBook, newBook);
            updateSearchCash(oldBook, newBook);
        } else {
            throw new LibraryProviderException(newBook.getCount() + " books with ISBN " + newBook.getIsbn() + "can't be added to library, maximum count of " +
                    "book copies should be less than 5, you can save only 5 books.");
        }
    }

    public void deleteBooks(Book book, int count) throws LibraryProviderException {
        int bookCount = getBookCount(book);
        if (bookCount < count) {
            try {
                bookDAO.delete(book.getId());
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            removeFromISBNCash(book);
            removeBookFromSearchCash(book);
        } else {
            book.setCount(bookCount - count);
            try {
                bookDAO.updateBook(book.getId(), book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(isbnCash.get(book.getIsbn()), book);
        }
    }

    public int getBookCount(Book book) {
        return (isbnCash.get(book.getIsbn()) != null) ? isbnCash.get(book.getIsbn()).getCount() : 0;
    }

    public List<Book> readBooks(String searchString) {
        searchString = searchString.toLowerCase();
        String[] searchTokens = searchString.split(" ");
        List<Book> result = new ArrayList<Book>();
        for (String token : searchTokens) {
            Set<Book> foundBooks = searchCash.get(token);
            if (foundBooks != null) {
                result.addAll(foundBooks);
            }
        }
        return result;
    }

    public List<Book> readAll() {
        return bookDAO.readAll();
    }

    public void loadData() throws LibraryProviderException {
        try {
            bookDAO.loadStorage();
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
            throw new LibraryProviderException(newBookCount + " books with ISBN " + book.getIsbn() + "can't be added to library, maximum count of " +
                    "book copies should be less than 5, you can add only " + (5 - isbnCount) + " books.");
        }
    }

    private void addToISBNCash(Book book) {
        isbnCash.put(book.getIsbn(), book);
    }

    private void addBookToSearchCash(Book book) {
        addWordsToSearchCash(book.getIsbn().toLowerCase(), book);
        addWordsToSearchCash(book.getTitle().toLowerCase(), book);
        addWordsToSearchCash(book.getTopic().toLowerCase(), book);
        addWordsToSearchCash(book.getAuthor().toLowerCase(), book);
        addWordsToSearchCash(String.valueOf(book.getYear()).toLowerCase(), book);
    }


    private void removeBookFromSearchCash(Book book) {
        removeTokensFromSearchCash(book.getIsbn().toLowerCase(), book);
        removeTokensFromSearchCash(book.getTitle().toLowerCase(), book);
        removeTokensFromSearchCash(book.getAuthor().toLowerCase(), book);
        removeTokensFromSearchCash(book.getTopic().toLowerCase(), book);
        removeTokensFromSearchCash(String.valueOf(book.getYear()).toLowerCase(), book);
    }

    private void removeTokensFromSearchCash(String searchString, Book book) {
        String[] words = searchString.split(" ");
        for (String word : words) {
            HashSet<Book> books = searchCash.get(word);
            if (books != null) {
                books.remove(book);
                if (books.size() == 0) {
                    searchCash.remove(word);
                }
            }
        }
    }

    private void addWordsToSearchCash(String searchString, Book book) {
        String[] words = searchString.split(" ");
        for (String word : words) {
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
}

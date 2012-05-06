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
        checkISBNCount(newBook);
        try {
            bookDAO.updateBook(oldBook.getId(), newBook);
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        updateISBNCash(newBook, oldBook);
        updateSearchCash(oldBook, newBook);
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
        return isbnCash.get(book.getIsbn()).getCount();
    }

    public List<Book> readBooks(String searchString) {
        searchString = searchString.toLowerCase();
        String[] searchTokens = searchString.split(" ");
        List<Book> result = new ArrayList<Book>();
        for (String token : searchTokens) {
            Set<Book> foundBooks = searchCash.get(token);
            result.addAll(foundBooks);
        }
        return result;
    }

    public List<Book> readAll() {
        return bookDAO.readAll();
    }

    private void updateISBNCash(Book newBook, Book oldBook) {
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
        if (!(isbnCount < 5)) {
            throw new LibraryProviderException("Book with ISBN" + book.getIsbn() + "can't be added to library, maximum count of " +
                    "book copies should be less than 5");
        }
    }

    private void addToISBNCash(Book book) {
        isbnCash.put(book.getIsbn(), book);
    }

    private void addBookToSearchCash(Book book) {
        customizeBook(book);
        addWordsToSearchCash(book.getIsbn(), book);
        addWordsToSearchCash(book.getTitle(), book);
        addWordsToSearchCash(book.getTopic(), book);
        addWordsToSearchCash(book.getAuthor(), book);
        addWordsToSearchCash(String.valueOf(book.getYear()), book);
    }

    private void customizeBook(Book book) {
        book.setIsbn(book.getIsbn().toLowerCase());
        book.setTitle(book.getTitle().toLowerCase());
        book.setAuthor(book.getAuthor().toLowerCase());
        book.setTopic(book.getTopic().toLowerCase());
    }

    private void removeBookFromSearchCash(Book book) {
        customizeBook(book);
        removeBookFromSearchCash0(book.getIsbn(), book);
        removeBookFromSearchCash0(book.getTitle(), book);
        removeBookFromSearchCash0(book.getAuthor(), book);
        removeBookFromSearchCash0(book.getTopic(), book);
        removeBookFromSearchCash0(String.valueOf(book.getYear()), book);
    }

    private void removeBookFromSearchCash0(String searchString, Book book) {
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

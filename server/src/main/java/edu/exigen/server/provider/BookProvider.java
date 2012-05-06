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

    private Map<String, List<Book>> isbnCash = new HashMap<String, List<Book>>();
    private Map<String, HashSet<Integer>> searchCash = new HashMap<String, HashSet<Integer>>();
    private BookDAO bookDAO;

    public BookProvider(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void createBook(Book book) throws LibraryProviderException {
        customizeBook(book);
        String isbn = book.getIsbn();
        checkISBNCount(isbn);
        int id;
        try {
            id = bookDAO.createBook(book);
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        book.setId(id);
        addToISBNCash(isbn, book);
    }

    public void updateBook(Book oldBook, Book newBook) throws LibraryProviderException {
        customizeBook(oldBook);
        customizeBook(newBook);
        String newIsbn = newBook.getIsbn();
        String oldIsbn = oldBook.getIsbn();
        checkISBNCount(newIsbn);
        try {
            bookDAO.updateBook(oldBook.getId(), newBook);
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        if (!newIsbn.equals(oldIsbn)) {
            addToISBNCash(newIsbn, newBook);
            removeFromISBNCash(oldIsbn, oldBook);
        } else {
            updateISBNCash(newIsbn, newBook, oldBook);
        }

    }

    public void deleteBook(Book book) throws LibraryProviderException {
        try {
            bookDAO.delete(book.getId());
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        removeFromISBNCash(book.getIsbn(), book);
    }

    public List<Book> readBooks(String searchString) {
        searchString = searchString.toLowerCase();
        String[] searchTokens = searchString.split(" ");

    }

    private void updateISBNCash(String newIsbn, Book newBook, Book oldBook) {
        List<Book> books = isbnCash.get(newIsbn);
        for (Book book : books) {
            if (book.getId() == oldBook.getId()) {
                book.setAuthor(newBook.getAuthor());
                book.setTitle(newBook.getTitle());
                book.setTopic(newBook.getTopic());
                book.setYear(newBook.getYear());
                return;
            }
        }
    }

    private void removeFromISBNCash(String oldIsbn, Book oldBook) {
        List<Book> books = isbnCash.get(oldIsbn);
        for (Book book : books) {
            if (book.getId() == oldBook.getId()) {
                books.remove(oldBook);
                return;
            }
        }

    }

    private void checkISBNCount(String isbn) throws LibraryProviderException {
        if (!(isbnCash.get(isbn).size() < 5)) {
            throw new LibraryProviderException("Book with ISBN" + isbn + "can't be added to library, maximum count of " +
                    "book copies should be less than 5");
        }
    }

    private void addToISBNCash(String isbn, Book book) {
        List<Book> books = isbnCash.get(isbn);
        if (books != null) {
            books.add(book);
        } else {
            books = new ArrayList<Book>();
            books.add(book);
            isbnCash.put(isbn, books);
        }
    }

    private void addBookToSearchCash(Book book) {
        int id = book.getId();
        addWordsToSearchCash(book.getIsbn(), id);
        addWordsToSearchCash(book.getTitle(), id);
        addWordsToSearchCash(book.getTopic(), id);
        addWordsToSearchCash(book.getAuthor(), id);
        addWordsToSearchCash(String.valueOf(book.getYear()), id);
    }

    private static void customizeBook(Book book) {
        book.setIsbn(book.getIsbn().toLowerCase());
        book.setTitle(book.getTitle().toLowerCase());
        book.setAuthor(book.getAuthor().toLowerCase());
        book.setTopic(book.getTopic().toLowerCase());
    }

    private void addWordsToSearchCash(String searchString, int id) {
        String[] words = searchString.split(" ");
        for (String word : words) {
            HashSet<Integer> ids = searchCash.get(word);
            if (ids != null) {
                ids.add(id);
            } else {
                ids = new HashSet<Integer>();
                ids.add(id);
                searchCash.put(word, ids);
            }
        }
    }
}

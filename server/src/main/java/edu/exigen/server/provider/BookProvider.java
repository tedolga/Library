package edu.exigen.server.provider;

import edu.exigen.client.entities.Book;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookProvider {

    private Map<String, List<Book>> isbnCash = new HashMap<String, List<Book>>();
    private Map<String,List<Integer>> searchCash=new HashMap<String, List<Integer>>();
    private BookDAO bookDAO;

    public BookProvider(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void createBook(Book book) throws LibraryProviderException {
        customizeBook(book);
        String isbn = book.getIsbn();
        checkISBNCount(isbn);
        try {
            bookDAO.createBook(book);
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
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

//    public List<Book> readBooks(String searchString){
//        String[] searchTokens=searchString.split(" ");
//
//    }

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

    public static void customizeBook(Book book){
        book.setIsbn(book.getIsbn().toLowerCase());
        book.setTitle(book.getTitle().toLowerCase());
        book.setAuthor(book.getAuthor().toLowerCase());
        book.setTopic(book.getTopic().toLowerCase());
    }
}

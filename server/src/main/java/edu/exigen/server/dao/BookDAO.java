package edu.exigen.server.dao;

import edu.exigen.entities.Book;

import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface BookDAO {
    int createBook(Book book) throws LibraryDAOException;

    Book readBook(int id) throws LibraryDAOException;

    List<Book> readAll();

    boolean updateBook(int id, Book newBook) throws LibraryDAOException;

    boolean delete(int id) throws LibraryDAOException;

    void loadStorage() throws LibraryDAOException;
}

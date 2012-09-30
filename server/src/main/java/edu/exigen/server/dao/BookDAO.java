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

    /**
     * Updates book data.
     *
     * @param id      of book which should be updated.
     * @param newBook book which properties should be set to the updated book.
     * @return true if book was updated successfully or false - if book was not found.
     * @throws LibraryDAOException
     */
    boolean updateBook(int id, Book newBook) throws LibraryDAOException;

    boolean delete(int id) throws LibraryDAOException;

}

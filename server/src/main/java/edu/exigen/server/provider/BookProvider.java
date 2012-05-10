package edu.exigen.server.provider;

import edu.exigen.entities.Book;
import edu.exigen.server.dao.LibraryDAOException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface BookProvider extends Remote {
    void createBook(Book book) throws LibraryProviderException, RemoteException;

    void updateBook(Book oldBook, Book newBook) throws LibraryProviderException, RemoteException;

    void deleteBooks(Book book, int deleteCount) throws LibraryProviderException, RemoteException;

    int getBookCount(Book book) throws RemoteException;

    List<Book> searchBooks(String searchString) throws RemoteException;

    List<Book> readAll() throws RemoteException;

    Book getBookById(int id) throws RemoteException, LibraryDAOException;
}

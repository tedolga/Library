package edu.exigen.client;

import edu.exigen.client.entities.Book;
import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.LibraryProviderException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class LibraryClient {

    private static final String BOOK_PROVIDER_URL = "rmi://localhost/book_provider";

    private final BookProvider bookProvider;

    public LibraryClient(BookProvider bookProvider) {
        this.bookProvider = bookProvider;
    }

    public static void main(String[] args) throws NamingException, LibraryProviderException, RemoteException {
        Context namingContext = new InitialContext();
        BookProvider bookProvider = (BookProvider) namingContext.lookup(BOOK_PROVIDER_URL);
        LibraryClient libraryClient = new LibraryClient(bookProvider);
    }


    public void createBook(Book book) throws LibraryProviderException, RemoteException {
        bookProvider.createBook(book);
    }


}

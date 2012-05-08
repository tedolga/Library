package edu.exigen.client;

import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.ReaderProvider;
import edu.exigen.server.provider.ReservationRecordProvider;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class LibraryClient {

    private static final String BOOK_PROVIDER_URL = "rmi://localhost/book_provider";
    private static final String READER_PROVIDER_URL = "rmi://localhost/reader_provider";
    private static final String RECORD_PROVIDER_URL = "rmi://localhost/record_provider";

    private BookProvider bookProvider;
    private ReaderProvider readerProvider;
    private ReservationRecordProvider recordProvider;

    public LibraryClient(BookProvider bookProvider, ReaderProvider readerProvider, ReservationRecordProvider recordProvider) {
        this.bookProvider = bookProvider;
        this.readerProvider = readerProvider;
        this.recordProvider = recordProvider;
    }

    public static void main(String[] args) {
        try {
            Context namingContext = new InitialContext();
            BookProvider bookProvider = (BookProvider) namingContext.lookup(BOOK_PROVIDER_URL);
            ReaderProvider readerProvider = (ReaderProvider) namingContext.lookup(READER_PROVIDER_URL);
            ReservationRecordProvider recordProvider = (ReservationRecordProvider) namingContext.lookup(RECORD_PROVIDER_URL);
            LibraryClient libraryClient = new LibraryClient(bookProvider, readerProvider, recordProvider);
        } catch (NamingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}

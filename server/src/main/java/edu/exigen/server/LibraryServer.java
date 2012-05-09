package edu.exigen.server;

import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;
import edu.exigen.server.provider.BookProviderImpl;
import edu.exigen.server.provider.LibraryProviderException;
import edu.exigen.server.provider.ReaderProviderImpl;
import edu.exigen.server.provider.ReservationRecordProviderImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class LibraryServer {

    private static final String READER_PROVIDED_XML = "readers.xml";
    private static final String BOOK_PROVIDED_XML = "books.xml";
    private static final String RECORD_PROVIDED_XML = "records.xml";

    private static final String BOOK_PROVIDER_NAME = "rmi:book_provider";
    private static final String READER_PROVIDER_NAME = "rmi:reader_provider";
    private static final String RECORD_PROVIDER_NAME = "rmi:record_provider";
    private static final int SERVER_PORT = 1099;

    private ReservationRecordProviderImpl recordProvider;
    private BookProviderImpl bookProvider;
    private ReaderProviderImpl readerProvider;

    public static void main(String[] args) throws Exception {
        try {
            LibraryServer libraryServer = new LibraryServer();
            libraryServer.loadServer();
            LocateRegistry.createRegistry(SERVER_PORT);
            libraryServer.registerProviders();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Server is already started.", "Library server", JOptionPane.INFORMATION_MESSAGE);
            System.exit(-1);
        }
    }

    public LibraryServer() throws RemoteException {
        BookDAO bookDAO = new BookDAO(BOOK_PROVIDED_XML);
        ReaderDAO readerDAO = new ReaderDAO(READER_PROVIDED_XML);
        ReservationRecordDAO recordDAO = new ReservationRecordDAO(RECORD_PROVIDED_XML);
        recordProvider = new ReservationRecordProviderImpl(bookDAO, readerDAO, recordDAO);
        bookProvider = new BookProviderImpl(bookDAO, recordProvider);
        readerProvider = new ReaderProviderImpl(readerDAO, recordProvider);
    }

    public void loadServer() throws LibraryProviderException, RemoteException {
        bookProvider.loadData();
        readerProvider.loadData();
        recordProvider.loadData();
    }

    public void registerProviders() throws NamingException {
        Context namingContext = new InitialContext();
        namingContext.bind(BOOK_PROVIDER_NAME, bookProvider);
        namingContext.bind(READER_PROVIDER_NAME, readerProvider);
        namingContext.bind(RECORD_PROVIDER_NAME, recordProvider);
    }
}

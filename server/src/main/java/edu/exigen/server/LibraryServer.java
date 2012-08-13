package edu.exigen.server;

import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.LibraryProviderException;
import edu.exigen.server.provider.ReaderProvider;
import edu.exigen.server.provider.ReservationRecordProvider;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * RMI library server for managing book, reader and record information.
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class LibraryServer {

    private static final String BOOK_PROVIDER_NAME = "rmi:book_provider";

    /**
     * Reader provider name in rmi registry
     */
    private static final String READER_PROVIDER_NAME = "rmi:reader_provider";

    /**
     * Record provider name in rmi registry
     */
    private static final String RECORD_PROVIDER_NAME = "rmi:record_provider";

    /**
     * Connection port.
     */
    private static final int SERVER_PORT = 1099;

    private ReservationRecordProvider recordProvider;
    private BookProvider bookProvider;
    private ReaderProvider readerProvider;


    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Internal server error. " + e.getMessage(), "Library server", JOptionPane.ERROR_MESSAGE);
            }
        });
        try {
            ApplicationContext context = new FileSystemXmlApplicationContext("spring-config.xml");
            LibraryServer libraryServer = context.getBean(LibraryServer.class);
            libraryServer.loadServer();
            LocateRegistry.createRegistry(SERVER_PORT);
            libraryServer.registerProviders();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "Server was started.\nPress 'OK' to destroy the Server", "Library server", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            });
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Server is already started.", "Library server", JOptionPane.INFORMATION_MESSAGE);
            System.exit(-1);
        } catch (LibraryProviderException e) {
            JOptionPane.showMessageDialog(null, "Can't load data, cause: " + e.getMessage(), "Library server", JOptionPane.INFORMATION_MESSAGE);
            System.exit(-1);
        } catch (NamingException e) {
            JOptionPane.showMessageDialog(null, "Can't find server providers, cause: " + e.getMessage(), "Library server", JOptionPane.INFORMATION_MESSAGE);
            System.exit(-1);
        }
    }

    public LibraryServer() {
    }

    /**
     * Loads all provider's initialization data.
     *
     * @throws LibraryProviderException in case of library provider errors.
     * @throws RemoteException          in case of RemoteException
     */
    public void loadServer() throws LibraryProviderException, RemoteException {
        bookProvider.loadData();
        readerProvider.loadData();
        recordProvider.loadData();
    }

    /**
     * Registers all server providers.
     *
     * @throws NamingException in case of naming exceptions.
     */
    public void registerProviders() throws NamingException {
        Context namingContext = new InitialContext();
        namingContext.bind(BOOK_PROVIDER_NAME, bookProvider);
        namingContext.bind(READER_PROVIDER_NAME, readerProvider);
        namingContext.bind(RECORD_PROVIDER_NAME, recordProvider);
    }

    @Required
    public void setBookProvider(BookProvider bookProvider) {
        this.bookProvider = bookProvider;
    }

    @Required
    public void setReaderProvider(ReaderProvider readerProvider) {
        this.readerProvider = readerProvider;
    }

    @Required
    public void setRecordProvider(ReservationRecordProvider recordProvider) {
        this.recordProvider = recordProvider;
    }
}

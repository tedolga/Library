package edu.exigen.server.provider;

import edu.exigen.entities.Reader;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface ReaderProvider extends Remote {

    void createReader(Reader reader) throws LibraryProviderException, RemoteException;

    List<Reader> readAll() throws RemoteException;

    List<Reader> searchReaders(String searchString) throws RemoteException;

    void updateReader(Reader oldReader, Reader newReader) throws LibraryProviderException, RemoteException;

    void deleteReader(Reader reader) throws LibraryProviderException, RemoteException;
}

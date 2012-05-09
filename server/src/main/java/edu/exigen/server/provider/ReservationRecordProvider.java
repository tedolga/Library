package edu.exigen.server.provider;

import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.Reader;
import edu.exigen.client.entities.ReservationRecord;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface ReservationRecordProvider extends Remote {
    void createRecord(int readerId, int bookId, Date dateOfReturn) throws LibraryProviderException, RemoteException;

    void deleteRecord(ReservationRecord reservationRecord) throws LibraryProviderException, RemoteException;

    List<ReservationRecord> readAll() throws RemoteException;

    void checkAvailableBooks(int bookId) throws LibraryProviderException, RemoteException;

    int getReservedBookCount(int bookId) throws RemoteException;

    List<Book> getReservedReaderBooks(Reader reader) throws LibraryProviderException, RemoteException;
}

package edu.exigen.server.provider;

import edu.exigen.entities.Book;
import edu.exigen.entities.Reader;
import edu.exigen.entities.ReservationRecord;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReservationRecordProviderImpl extends UnicastRemoteObject implements ReservationRecordProvider {
    private BookDAO bookDAO;
    private ReaderDAO readerDAO;
    private ReservationRecordDAO recordDAO;

    public ReservationRecordProviderImpl(BookDAO bookDAO, ReaderDAO readerDAO, ReservationRecordDAO recordDAO)
            throws RemoteException {
        super();
        this.bookDAO = bookDAO;
        this.readerDAO = readerDAO;
        this.recordDAO = recordDAO;
    }

    public synchronized void createRecord(int readerId, int bookId, Date dateOfReturn) throws LibraryProviderException, RemoteException {
        checkAvailableBooks(bookId);
        ReservationRecord record = createReservationRecord(readerId, bookId, dateOfReturn);
        try {
            recordDAO.createRecord(record);
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }

    }

    public synchronized void deleteRecord(ReservationRecord reservationRecord) throws LibraryProviderException, RemoteException {
        try {
            if (reservationRecord != null) {
                recordDAO.delete(reservationRecord.getId());
            }
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
    }

    public List<ReservationRecord> readAll() throws RemoteException {
        return recordDAO.readAll();
    }

    public void checkAvailableBooks(int bookId) throws LibraryProviderException, RemoteException {
        try {
            int sumCount = bookDAO.readBook(bookId).getCount();
            int reservedCount = getReservedBookCount(bookId);
            if ((sumCount - reservedCount) < 1) {
                throw new LibraryProviderException("There are no available books now.");
            }
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
    }

    public int getReservedBookCount(int bookId) throws RemoteException {
        List<ReservationRecord> records = readAll();
        int reservedCount = 0;
        for (ReservationRecord record : records) {
            if (record.getBookId() == bookId && !(record.getReturnDate().before(new Date()))) {
                reservedCount++;
            }
        }
        return reservedCount;
    }

    public List<Book> getReservedReaderBooks(Reader reader) throws LibraryProviderException, RemoteException {
        int readerId = reader.getId();
        List<ReservationRecord> records = readAll();
        List<Book> reservedBooks = new ArrayList<Book>();
        for (ReservationRecord record : records) {
            if (record.getReaderId() == readerId && !(record.getReturnDate().before(new Date()))) {
                try {
                    reservedBooks.add(bookDAO.readBook(record.getBookId()));
                } catch (LibraryDAOException e) {
                    throw new LibraryProviderException(e.getMessage(), e);
                }
            }
        }
        return reservedBooks;
    }

    private synchronized ReservationRecord createReservationRecord(int readerId, int bookId, Date dateOfReturn) {
        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setBookId(bookId);
        reservationRecord.setReaderId(readerId);
        reservationRecord.setIssueDate(new Date());
        reservationRecord.setReturnDate(dateOfReturn);
        return reservationRecord;
    }

    public void loadData() throws LibraryProviderException {

    }
}

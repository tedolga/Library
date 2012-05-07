package edu.exigen.server.provider;

import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.Reader;
import edu.exigen.client.entities.ReservationRecord;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReservationRecordProvider {
    private BookDAO bookDAO;
    private ReaderDAO readerDAO;
    private ReservationRecordDAO recordDAO;

    public ReservationRecordProvider(BookDAO bookDAO, ReaderDAO readerDAO, ReservationRecordDAO recordDAO) {
        this.bookDAO = bookDAO;
        this.readerDAO = readerDAO;
        this.recordDAO = recordDAO;
    }

    public void createRecord(Reader reader, Book book, Date dateOfReturn) throws LibraryProviderException {
        int bookId = book.getId();
        checkAvailableBooks(bookId);
        ReservationRecord record = createReservationRecord(reader, book, dateOfReturn);
        try {
            recordDAO.createRecord(record);
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }

    }

    public void deleteRecord(ReservationRecord reservationRecord) throws LibraryProviderException {
        try {
            recordDAO.delete(reservationRecord.getId());
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
    }

    public List<ReservationRecord> readAll() {
        return recordDAO.readAll();
    }

    public void checkAvailableBooks(int bookId) throws LibraryProviderException {
        try {
            int sumCount = bookDAO.readBook(bookId).getCount();
            int reservedCount = getReservedBookCount(bookId);
            if ((sumCount - reservedCount) < 1) {
                throw new LibraryProviderException("Reservation failed. There are no available books now.");
            }
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
    }

    public int getReservedBookCount(int bookId) {
        List<ReservationRecord> records = readAll();
        int reservedCount = 0;
        for (ReservationRecord record : records) {
            if (record.getBookId() == bookId && !(record.getReturnDate().before(new Date()))) {
                reservedCount++;
            }
        }
        return reservedCount;
    }

    public List<Book> getReservedReaderBooks(Reader reader) throws LibraryProviderException {
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

    private ReservationRecord createReservationRecord(Reader reader, Book book, Date dateOfReturn) {
        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setBookId(book.getId());
        reservationRecord.setReaderId(reader.getId());
        reservationRecord.setIssueDate(new Date());
        reservationRecord.setReturnDate(dateOfReturn);
        return reservationRecord;
    }

    public void loadData() throws LibraryProviderException {
        try {
            bookDAO.loadStorage();
            readerDAO.loadStorage();
            recordDAO.loadStorage();
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
    }
}

package edu.exigen.server.provider;

import edu.exigen.entities.Book;
import edu.exigen.entities.Reader;
import edu.exigen.entities.ReservationRecord;
import edu.exigen.server.dao.xml.XMLBookDAO;
import edu.exigen.server.dao.xml.XMLReaderDAO;
import edu.exigen.server.dao.xml.XMLReservationRecordDAO;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReservationRecordProviderTest {
    private static final String READER_PROVIDED_XML = "readerProvided.xml";
    private static final String BOOK_PROVIDED_XML = "bookProvided.xml";
    private static final String RECORD_PROVIDED_XML = "recordProvided.xml";
    private XMLBookDAO bookDAO = new XMLBookDAO(BOOK_PROVIDED_XML);
    private XMLReaderDAO readerDAO = new XMLReaderDAO(READER_PROVIDED_XML);
    private XMLReservationRecordDAO recordDAO = new XMLReservationRecordDAO(RECORD_PROVIDED_XML);
    private ReservationRecordProviderImpl recordProvider;
    private BookProviderImpl bookProvider;
    private ReaderProviderImpl readerProvider;

    @Before
    public void setUp() throws Exception {
        recordProvider = new ReservationRecordProviderImpl(bookDAO, readerDAO, recordDAO);
        readerProvider = new ReaderProviderImpl(readerDAO, recordProvider);
        bookProvider = new BookProviderImpl(bookDAO, recordProvider);
        readerDAO.loadStorage();
        bookDAO.loadStorage();
        recordDAO.loadStorage();
        bookProvider.loadData();
        readerProvider.loadData();
    }

    @BeforeClass
    @AfterClass
    public static void clear() {
        File file = new File(READER_PROVIDED_XML);
        file.delete();
        file = new File(BOOK_PROVIDED_XML);
        file.delete();
        file = new File(RECORD_PROVIDED_XML);
        file.delete();
    }

    @Test
    public void testCreateRecord() throws Exception {
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("34/5 Nevsky pr.");
        reader.setDateOfBirth(new Date());
        readerProvider.createReader(reader);
        Book book = new Book();
        book.setIsbn("6767-466-676-77");
        book.setTitle("Povesti Belkina");
        book.setAuthor("Pushkin");
        book.setTopic("russian classic");
        book.setYear(1998);
        book.setCount(2);
        bookProvider.createBook(book);
        Calendar returnDate = Calendar.getInstance();
        returnDate.add(Calendar.DAY_OF_MONTH, 14);
        recordProvider.createRecord(reader.getId(), book.getId(), returnDate.getTime());
        recordProvider.createRecord(reader.getId(), book.getId(), returnDate.getTime());
        try {
            recordProvider.createRecord(reader.getId(), book.getId(), returnDate.getTime());
            Assert.assertTrue(false);
        } catch (LibraryProviderException e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testDeleteRecord() throws Exception {
        ReservationRecord record = recordProvider.readAll().get(0);
        Assert.assertEquals(2, recordProvider.readAll().size());
        recordProvider.deleteRecord(record);
        Assert.assertEquals(1, recordProvider.readAll().size());
        Reader reader = readerProvider.searchReaders("Ivan").get(0);
        Book book = bookProvider.searchBooks("6767-466-676-77").get(0);
        Calendar returnDate = Calendar.getInstance();
        returnDate.add(Calendar.DAY_OF_MONTH, 14);
        recordProvider.createRecord(reader.getId(), book.getId(), returnDate.getTime());
    }
}

package edu.exigen.server.provider;

import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.Reader;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;
import org.junit.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReaderProviderTest {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final String READER_PROVIDED_XML = "readerProvided.xml";
    private static final String BOOK_PROVIDED_XML = "bookProvided.xml";
    private static final String RECORD_PROVIDED_XML = "recordProvided.xml";
    private ReaderDAO readerDAO = new ReaderDAO(READER_PROVIDED_XML);
    private BookDAO bookDAO = new BookDAO(BOOK_PROVIDED_XML);
    private ReservationRecordDAO recordDAO = new ReservationRecordDAO(RECORD_PROVIDED_XML);
    private ReservationRecordProviderImpl recordProvider;
    private ReaderProviderImpl provider;

    @Before
    public void setUp() throws Exception {
        recordProvider = new ReservationRecordProviderImpl(bookDAO, readerDAO, recordDAO);
        provider = new ReaderProviderImpl(readerDAO, recordProvider);
        provider.loadData();
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
    public void testCreateReader() throws Exception {
        Assert.assertEquals(0, provider.readAll().size());
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("34/5 Nevsky pr.");
        reader.setDateOfBirth(new Date());
        provider.createReader(reader);
        Assert.assertEquals(1, provider.readAll().size());
        Assert.assertEquals(1, provider.readAll().get(0).getId());
        Reader newReader = reader.copy();
        newReader.setId(0);
        newReader.setFirstName("Petr");
        provider.createReader(newReader);
        Assert.assertEquals(2, provider.readAll().size());
        Assert.assertEquals(2, provider.searchReaders("Petr").get(0).getId());
    }

    @Test
    public void testSearchReaders() throws Exception {
        Assert.assertEquals(1, provider.searchReaders("Petr").size());
        Assert.assertEquals(2, provider.searchReaders("Petrov").size());
        Assert.assertEquals(2, provider.searchReaders("Petr Petrov").size());
        Assert.assertEquals(2, provider.searchReaders(dateFormat.format(new Date())).size());
    }

    @Test
    public void testUpdateReader() throws Exception {
        Reader oldReader = provider.searchReaders("1").get(0);
        Reader reader = new Reader();
        reader.setId(oldReader.getId());
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("39/5 pr. Veteranov");
        reader.setDateOfBirth(new Date());
        provider.updateReader(oldReader, reader);
        Assert.assertEquals("39/5 pr. Veteranov", provider.searchReaders("1").get(0).getAddress());
        Assert.assertEquals(2, provider.readAll().size());
        Assert.assertEquals(1, provider.searchReaders("Veteranov").size());
    }

    @Test
    public void testDeleteReader() throws Exception {
        Reader oldReader = provider.searchReaders("2").get(0);
        provider.deleteReader(oldReader);
        Assert.assertEquals(1, provider.readAll().size());
        Assert.assertEquals(0, provider.searchReaders("Petr").size());
        Assert.assertEquals(1, provider.searchReaders("Petr Petrov").size());
        Book book = new Book();
        book.setIsbn("6767-466-676-77");
        book.setTitle("Povesti Belkina");
        book.setAuthor("Pushkin");
        book.setTopic("russian classic");
        book.setYear(1998);
        book.setCount(1);
        bookDAO.createBook(book);
        Reader reader = provider.searchReaders("1").get(0);
        Calendar returnDate = Calendar.getInstance();
        returnDate.add(Calendar.DAY_OF_MONTH, 14);
        recordProvider.createRecord(reader, book, returnDate.getTime());
        try {
            provider.deleteReader(reader);
            Assert.assertTrue(false);
        } catch (LibraryProviderException e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(true);
        }
    }
}

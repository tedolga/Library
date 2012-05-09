package edu.exigen.server.provider;

import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.Reader;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;
import org.junit.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;


/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookProviderTest {
    private static final String READER_PROVIDED_XML = "readerProvided.xml";
    private static final String BOOK_PROVIDED_XML = "bookProvided.xml";
    private static final String RECORD_PROVIDED_XML = "recordProvided.xml";
    private BookDAO bookDAO = new BookDAO(BOOK_PROVIDED_XML);
    private ReaderDAO readerDAO = new ReaderDAO(READER_PROVIDED_XML);
    private ReservationRecordDAO recordDAO = new ReservationRecordDAO(RECORD_PROVIDED_XML);
    private ReservationRecordProviderImpl recordProvider;
    private BookProviderImpl provider;

    @Before
    public void setUp() throws Exception {
        recordProvider = new ReservationRecordProviderImpl(bookDAO, readerDAO, recordDAO);
        provider = new BookProviderImpl(bookDAO, recordProvider);
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
    public void testCreateBook() throws Exception {
        Assert.assertEquals(0, provider.readAll().size());
        Book book = new Book();
        book.setIsbn("6767-466-676-77");
        book.setTitle("Povesti Belkina");
        book.setAuthor("Pushkin");
        book.setTopic("russian classic");
        book.setYear(1998);
        book.setCount(3);
        provider.createBook(book);
        Assert.assertEquals(1, provider.readAll().size());
        Assert.assertEquals(1, provider.searchBooks(book.getIsbn()).get(0).getId());
        Assert.assertEquals(3, provider.searchBooks(book.getIsbn()).get(0).getCount());
        book = new Book();
        book.setIsbn("6767-466-676-77");
        book.setTitle("Povesti Belkina");
        book.setAuthor("Pushkin");
        book.setTopic("russian classic");
        book.setYear(1998);
        book.setCount(2);
        provider.createBook(book);
        Assert.assertEquals(1, provider.readAll().size());
        Assert.assertEquals(5, provider.searchBooks(book.getAuthor()).get(0).getCount());
        book = new Book();
        book.setIsbn("6767-466-676-78");
        book.setTitle("Povesti Belkina");
        book.setAuthor("Pushkin");
        book.setTopic("russian classic");
        book.setYear(2006);
        book.setCount(2);
        provider.createBook(book);
        Assert.assertEquals(2, provider.readAll().size());
        Assert.assertEquals(2, provider.getBookCount(book));
        Assert.assertEquals(2, provider.searchBooks("1998 2006").size());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = provider.searchBooks("Povesti").get(0);
        String isbn = book.getIsbn();
        System.out.println("isbn = " + isbn);
        Book newBook = new Book();
        newBook.setIsbn(isbn);
        newBook.setTitle("Povesti Belkina");
        newBook.setAuthor("Pushkin A");
        newBook.setTopic("russian classic");
        newBook.setYear(2006);
        newBook.setCount(2);
        provider.updateBook(book, newBook);
        Assert.assertEquals(2, provider.readAll().size());
        Assert.assertEquals(2, provider.getBookCount(book));
        Assert.assertEquals("Pushkin A", provider.searchBooks(newBook.getIsbn()).get(0).getAuthor());
        Assert.assertEquals(2, provider.searchBooks("Pushkin").size());
        newBook = new Book();
        newBook.setIsbn("6767-466-676-79");
        newBook.setTitle("Povesti Belkina");
        newBook.setAuthor("Pushkin A");
        newBook.setTopic("russian classic");
        newBook.setYear(2009);
        newBook.setCount(4);
        provider.updateBook(book, newBook);
        Assert.assertEquals(2, provider.readAll().size());
        Assert.assertEquals(4, provider.getBookCount(book));
        Assert.assertEquals("Pushkin A", provider.searchBooks(newBook.getIsbn()).get(0).getAuthor());
        book = new Book();
        book.setIsbn(isbn);
        book.setTitle("Povesti Belkina");
        book.setAuthor("Pushkin");
        book.setTopic("russian classic");
        book.setYear(2006);
        book.setCount(2);
        Assert.assertEquals(0, provider.getBookCount(book));
        Assert.assertEquals(0, provider.searchBooks(isbn).size());
        book = provider.searchBooks("6767-466-676-78").get(0);
        newBook = book.copyBook();
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("34/5 Nevsky pr.");
        Calendar returnDate = Calendar.getInstance();
        returnDate.add(Calendar.DAY_OF_MONTH, 14);
        reader.setDateOfBirth(new Date());
        readerDAO.createReader(reader);
        recordProvider.createRecord(reader.getId(), book.getId(), returnDate.getTime());
        recordProvider.createRecord(reader.getId(), book.getId(), returnDate.getTime());
        newBook.setCount(1);
        try {
            provider.updateBook(book, newBook);
            Assert.assertTrue(false);
        } catch (LibraryProviderException e) {
            Assert.assertTrue(true);
        }
        newBook.setCount(4);
        provider.updateBook(book, newBook);
    }

    @Test
    public void testDeleteBooks() throws Exception {
        Book book = provider.searchBooks("2006").get(0);
        provider.deleteBooks(book, 1);
        Assert.assertEquals(3, provider.getBookCount(book));
        Assert.assertEquals(3, provider.searchBooks("2006").get(0).getCount());
        provider.deleteBooks(book, 1);
        Assert.assertEquals(2, provider.getBookCount(book));
        Assert.assertEquals(1, provider.searchBooks("2006").size());
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("34/5 Nevsky pr.");
        reader.setDateOfBirth(new Date());
        readerDAO.createReader(reader);
        book = provider.searchBooks("2009").get(0);
        Calendar returnDate = Calendar.getInstance();
        returnDate.add(Calendar.DAY_OF_MONTH, 14);
        recordProvider.createRecord(reader.getId(), book.getId(), returnDate.getTime());
        provider.deleteBooks(book, 3);
        try {
            provider.deleteBooks(book, 1);
            Assert.assertTrue(false);
        } catch (LibraryProviderException e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testExceptions() throws Exception {
        Book book = new Book();
        book.setIsbn("6767-466-676-70");
        book.setTitle("Povesti Belkina");
        book.setAuthor("Pushkin");
        book.setTopic("russian classic");
        book.setYear(1998);
        book.setCount(4);
        provider.createBook(book);
        book = book.copyBook();
        book.setId(0);
        book.setCount(2);
        try {
            provider.createBook(book);
            Assert.assertTrue(false);
        } catch (LibraryProviderException e) {
            Assert.assertTrue(true);
            System.out.println(e.getMessage());
        }
        book = book.copyBook();
        book.setId(0);
        book.setIsbn("6767-466-676");
        book.setCount(6);
        try {
            provider.createBook(book);
            Assert.assertTrue(false);
        } catch (LibraryProviderException e) {
            Assert.assertTrue(true);
            System.out.println(e.getMessage());
        }
        book.setCount(5);
        provider.createBook(book);
        Book newBook = book.copyBook();
        newBook.setId(0);
        newBook.setCount(6);
        try {
            provider.updateBook(book, newBook);
            Assert.assertTrue(false);
        } catch (LibraryProviderException e) {
            Assert.assertTrue(true);
            System.out.println(e.getMessage());
        }
    }


}

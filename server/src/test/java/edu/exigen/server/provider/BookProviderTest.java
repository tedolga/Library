package edu.exigen.server.provider;

import edu.exigen.client.entities.Book;
import edu.exigen.server.dao.BookDAO;
import org.junit.*;

import java.io.File;


/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookProviderTest {
    private static final String FILE_NAME = "booksProvided.xml";
    private BookDAO bookDAO = new BookDAO(FILE_NAME);
    private BookProvider provider = new BookProvider(bookDAO);

    @Before
    public void setUp() throws Exception {
        provider.loadData();
    }

    @BeforeClass
    @AfterClass
    public static void clear() {
        File file = new File(FILE_NAME);
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
    }

    @Test
    public void testDeleteBooks() throws Exception {
        Book book = provider.searchBooks("2006").get(0);
        provider.deleteBooks(book, 1);
        Assert.assertEquals(1, provider.getBookCount(book));
        Assert.assertEquals(1, provider.searchBooks("2006").get(0).getCount());
        provider.deleteBooks(book, 2);
        Assert.assertEquals(0, provider.getBookCount(book));
        Assert.assertEquals(0, provider.searchBooks("2006").size());
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

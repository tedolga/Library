package edu.exigen.server.dao;

import edu.exigen.client.entities.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookDAOTest {

    private String booksFile = "booksData.xml";

    @Test
    public void testCreateBook() throws Exception {
        BookDAO bookDAO = new BookDAO(booksFile);
        bookDAO.loadStorage();
        Book book = new Book();
        book.setIsbn("BE-5656");
        book.setAuthor("B. R. Tomas");
        book.setTitle("Tom");
        book.setTopic("Biography");
        book.setYear(1998);
        int id = bookDAO.createBook(book);
        Assert.assertEquals(1, id);
        bookDAO = new BookDAO(booksFile);
        bookDAO.loadStorage();
        book.setIsbn("BE-5655");
        Assert.assertEquals(2, bookDAO.createBook(book));
    }

    @After
    public void tearDown() throws Exception {
        File testFile = new File(booksFile);
        testFile.delete();
    }
}

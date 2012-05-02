package edu.exigen.server.dao;

import edu.exigen.client.entities.Book;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookDAOTest {
    @Test
    public void testCreateBook() throws Exception {
        String booksFile = "booksData.xml";
        File testFile = new File(booksFile);
        testFile.delete();
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
}

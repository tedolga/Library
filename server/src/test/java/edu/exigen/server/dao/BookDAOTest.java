package edu.exigen.server.dao;

import edu.exigen.entities.Book;
import edu.exigen.server.dao.xml.XMLBookDAO;
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
    public void testBookDAOPositive() throws Exception {
        BookDAO bookDAO = new XMLBookDAO(booksFile);
        bookDAO.loadStorage();
        Book book = new Book();
        book.setIsbn("BE-5656");
        book.setAuthor("B. R. Tomas");
        book.setTitle("Tom");
        book.setTopic("Biography");
        book.setYear(1998);
        int id = bookDAO.createBook(book);
        Assert.assertEquals(1, id);
        bookDAO = new XMLBookDAO(booksFile);
        bookDAO.loadStorage();
        book.setIsbn("BE-5655");
        Assert.assertEquals(2, bookDAO.createBook(book));
        Assert.assertEquals(book.getIsbn(), bookDAO.readBook(2).getIsbn());
        Assert.assertEquals(2, bookDAO.readAll().size());
        Book newBook = new Book();
        newBook.setIsbn("00000");
        newBook.setTitle("Pinoccio");
        newBook.setAuthor("Italian");
        newBook.setTopic("fairy Tale");
        newBook.setYear(1980);
        Assert.assertTrue(bookDAO.updateBook(1, newBook));
        bookDAO.loadStorage();
        Assert.assertEquals(newBook.getTitle(), bookDAO.readBook(1).getTitle());
        Assert.assertTrue(bookDAO.delete(1));
        bookDAO.loadStorage();
        Assert.assertEquals(1, bookDAO.readAll().size());
        Assert.assertTrue(bookDAO.delete(2));
        Assert.assertEquals(0, bookDAO.readAll().size());
    }

    @Test
    public void testReadEmptyStorage() throws Exception {
        File file = new File("Empty.xml");
        file.createNewFile();
        BookDAO bookDAO = new XMLBookDAO("Empty.xml");
        bookDAO.loadStorage();
        Assert.assertEquals(0, bookDAO.readAll().size());
    }

    @After
    public void tearDown() throws Exception {
        File testFile = new File(booksFile);
        testFile.delete();
    }
}

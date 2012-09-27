package edu.exigen.server.dao.hibernate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.exigen.entities.Book;
import junit.framework.Assert;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class HibernateBookDAOTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresUnitWork");
    private static HibernateBookDAO bookDAO = new HibernateBookDAO();
    private static int rowCounter = 0;

    @BeforeClass
    public static void beforeAll() {
        bookDAO.setEntityManagerFactory(entityManagerFactory);
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book();
        book.setAuthor("Pushkin");
        book.setIsbn("ISBN-67676-09-98");
        book.setTitle("Fairy tales");
        book.setTopic("Classic");
        book.setYear(1980);
        book.setCount(4);
        int bookId = bookDAO.createBook(book);
        rowCounter += 1;
        Assert.assertEquals(book.getIsbn(), bookDAO.readBook(bookId).getIsbn());

    }

    @Test
    public void testReadBook() throws Exception {
        Book book = new Book();
        book.setAuthor("Gogol");
        book.setIsbn("ISBN-67676-09-80");
        book.setTitle("Revizor");
        book.setTopic("Classic");
        book.setYear(1970);
        book.setCount(1);
        int bookId = bookDAO.createBook(book);
        rowCounter += 1;
        Assert.assertEquals(book.getIsbn(), bookDAO.readBook(bookId).getIsbn());
        Assert.assertEquals(book.getAuthor(), bookDAO.readBook(bookId).getAuthor());
        Assert.assertEquals(book.getTitle(), bookDAO.readBook(bookId).getTitle());
        Assert.assertEquals(book.getTopic(), bookDAO.readBook(bookId).getTopic());
        Assert.assertEquals(book.getYear(), bookDAO.readBook(bookId).getYear());
        Assert.assertEquals(book.getCount(), bookDAO.readBook(bookId).getCount());
    }


    @Test
    public void testReadAll() throws Exception {
        Assert.assertEquals(rowCounter, bookDAO.readAll().size());
    }

    @Test
    public void testUpdateBook() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }
}

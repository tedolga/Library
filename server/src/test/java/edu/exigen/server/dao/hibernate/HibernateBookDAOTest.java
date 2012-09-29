package edu.exigen.server.dao.hibernate;

import edu.exigen.entities.Book;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class HibernateBookDAOTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresUnitHome");
    private static HibernateBookDAO bookDAO = new HibernateBookDAO(entityManagerFactory);
    private static int rowCounter = 0;

    @AfterClass
    public static void afterAll() {
        entityManagerFactory.close();
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
        int id = 1;
        Book newBook = new Book();
        newBook.setIsbn("ISBN-67676-09-99");
        newBook.setAuthor("Pushkin A. S.");
        newBook.setTitle("Fairy-Tales");
        newBook.setTopic("Russian Classic");
        newBook.setYear(1999);
        newBook.setCount(2);
        bookDAO.updateBook(id, newBook);
        Assert.assertEquals(newBook.getIsbn(), bookDAO.readBook(id).getIsbn());
        Assert.assertEquals(newBook.getAuthor(), bookDAO.readBook(id).getAuthor());
        Assert.assertEquals(newBook.getTitle(), bookDAO.readBook(id).getTitle());
        Assert.assertEquals(newBook.getTopic(), bookDAO.readBook(id).getTopic());
        Assert.assertEquals(newBook.getYear(), bookDAO.readBook(id).getYear());
        Assert.assertEquals(newBook.getCount(), bookDAO.readBook(id).getCount());
        Assert.assertFalse(bookDAO.updateBook(3, new Book()));
    }

    @Test
    public void testDelete() throws Exception {
        int id = 2;
        bookDAO.delete(id);
        Assert.assertEquals(--rowCounter, bookDAO.readAll().size());
        Assert.assertFalse(bookDAO.delete(3));
    }
}

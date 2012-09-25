package edu.exigen.server.dao.hibernate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import edu.exigen.entities.Book;
import edu.exigen.server.dao.BookDAO;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class HibernateBookDAOTest {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresUnit");

    @Test
    public void testCreateBook() throws Exception {
        BookDAO bookDAO = new HibernateBookDAO();
        Book book = new Book();
        book.setAuthor("Pushkin");
        book.setIsbn("ISBN-67676-09-98");
        book.setTitle("Fairy tales");
        book.setTopic("Classic");
        book.setYear(1980);
        book.setCount(4);
        int bookId = bookDAO.createBook(book);

    }

    @Test
    public void testReadBook() throws Exception {


    }

    @Test
    public void testReadAll() throws Exception {

    }

    @Test
    public void testUpdateBook() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }
}

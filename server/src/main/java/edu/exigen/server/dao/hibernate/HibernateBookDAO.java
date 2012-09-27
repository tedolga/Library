package edu.exigen.server.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

import edu.exigen.entities.Book;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class HibernateBookDAO implements BookDAO {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public int createBook(Book book) throws LibraryDAOException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(book);
        tx.commit();
        em.close();
        return book.getId();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Book readBook(int id) throws LibraryDAOException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Book book = em.find(Book.class, id);
        tx.commit();
        em.close();
        return book;
    }

    @Override
    public List<Book> readAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        List<Book> allBooks = em.createQuery("select b from Book b order by b.id", Book.class).getResultList();
        tx.commit();
        em.close();
        return allBooks;
    }

    @Override
    public boolean updateBook(int id, Book newBook) throws LibraryDAOException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Book oldBook = em.find(Book.class, id);
        oldBook.setIsbn(newBook.getIsbn());
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setTitle(newBook.getTitle());
        oldBook.setTopic(newBook.getTopic());
        oldBook.setYear(newBook.getYear());
        oldBook.setCount(newBook.getCount());
        tx.commit();
        em.close();
        return true;
    }

    @Override
    public boolean delete(int id) throws LibraryDAOException {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        tx.commit();
        em.close();
        return true;
    }

}

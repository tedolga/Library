package edu.exigen.server.dao.hibernate;

import edu.exigen.entities.Book;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Book> readAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean updateBook(int id, Book newBook) throws LibraryDAOException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean delete(int id) throws LibraryDAOException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void loadStorage() throws LibraryDAOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

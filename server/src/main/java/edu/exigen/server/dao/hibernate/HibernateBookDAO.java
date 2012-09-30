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

    public HibernateBookDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public int createBook(Book book) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(book);
            tx.commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return book.getId();
    }

    @Override
    public Book readBook(int id) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Book book = em.find(Book.class, id);
            tx.commit();
            return book;
        } finally {
            if (em != null) {
                em.close();
            }

        }
    }

    @Override
    public List<Book> readAll() {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            List<Book> allBooks = em.createQuery("select b from Book b order by b.id", Book.class).getResultList();
            tx.commit();
            return allBooks;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @Override
    public boolean updateBook(int id, Book newBook) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Book oldBook = em.find(Book.class, id);
            if (oldBook == null) {
                return false;
            }
            oldBook.setIsbn(newBook.getIsbn());
            oldBook.setAuthor(newBook.getAuthor());
            oldBook.setTitle(newBook.getTitle());
            oldBook.setTopic(newBook.getTopic());
            oldBook.setYear(newBook.getYear());
            oldBook.setCount(newBook.getCount());
            tx.commit();
            return true;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @Override
    public boolean delete(int id) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Book book = em.find(Book.class, id);
            if (book == null) {
                return false;
            }
            em.remove(book);
            tx.commit();
            return true;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}

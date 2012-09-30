package edu.exigen.server.dao.hibernate;

import edu.exigen.entities.Reader;
import edu.exigen.server.dao.LibraryDAOException;
import edu.exigen.server.dao.ReaderDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class HibernateReaderDAO implements ReaderDAO {

    private EntityManagerFactory entityManagerFactory;

    public HibernateReaderDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public int createReader(Reader reader) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(reader);
            tx.commit();
            return reader.getId();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Reader readReader(int id) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Reader reader = em.find(Reader.class, id);
            tx.commit();
            return reader;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Reader> readAll() {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            List<Reader> allReaders = em.createQuery("select r from Reader r order by r.id", Reader.class).getResultList();
            tx.commit();
            return allReaders;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public boolean updateReader(int id, Reader newReader) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Reader oldReader = em.find(Reader.class, id);
            if (oldReader == null) {
                return false;
            }
            oldReader.setFirstName(newReader.getFirstName());
            oldReader.setLastName(newReader.getLastName());
            oldReader.setAddress(newReader.getAddress());
            oldReader.setDateOfBirth(newReader.getDateOfBirth());
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
            Reader reader = em.find(Reader.class, id);
            if (reader == null) {
                return false;
            }
            em.remove(reader);
            tx.commit();
            return true;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}

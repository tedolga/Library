package edu.exigen.server.dao.hibernate;

import edu.exigen.entities.ReservationRecord;
import edu.exigen.server.dao.LibraryDAOException;
import edu.exigen.server.dao.ReservationRecordDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class HibernateRecordDAO implements ReservationRecordDAO {
    private EntityManagerFactory entityManagerFactory;

    public HibernateRecordDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public int createRecord(ReservationRecord record) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(record);
            tx.commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return record.getId();
    }

    @Override
    public ReservationRecord readRecord(int id) throws LibraryDAOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            ReservationRecord reservationRecord = em.find(ReservationRecord.class, id);
            tx.commit();
            return reservationRecord;
        } finally {
            if (em != null) {
                em.close();
            }

        }
    }

    @Override
    public List<ReservationRecord> readAll() {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            List<ReservationRecord> reservationRecords = em.createQuery("select r from ReservationRecord r order by r.id", ReservationRecord.class).getResultList();
            tx.commit();
            return reservationRecords;
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
            ReservationRecord reservationRecord = em.find(ReservationRecord.class, id);
            if (reservationRecord == null) {
                return false;
            }
            em.remove(reservationRecord);
            tx.commit();
            return true;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

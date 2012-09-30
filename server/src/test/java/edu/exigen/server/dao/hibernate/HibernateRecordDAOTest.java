package edu.exigen.server.dao.hibernate;

import edu.exigen.entities.ReservationRecord;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class HibernateRecordDAOTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresUnitHome");
    private static HibernateRecordDAO recordDAO = new HibernateRecordDAO(entityManagerFactory);
    private static int rowCount = 0;

    @AfterClass
    public static void afterAll() {
        entityManagerFactory.close();
    }

    @Test
    public void testCreateAndReadRecord() throws Exception {
        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setBookId(1);
        reservationRecord.setReaderId(2);
        reservationRecord.setIssueDate(new Date());
        reservationRecord.setReturnDate(new Date());
        int recordId = recordDAO.createRecord(reservationRecord);
        ReservationRecord returnedRecord = recordDAO.readRecord(recordId);
        Assert.assertNotNull(returnedRecord);
        rowCount += 1;
        Assert.assertEquals(reservationRecord.getBookId(), returnedRecord.getBookId());
        Assert.assertEquals(reservationRecord.getReaderId(), returnedRecord.getReaderId());
        Assert.assertEquals(reservationRecord.getIssueDate(), returnedRecord.getIssueDate());
        Assert.assertEquals(reservationRecord.getReturnDate(), returnedRecord.getReturnDate());
        int anotherRecordId = recordDAO.createRecord(new ReservationRecord());
        Assert.assertTrue(recordId < anotherRecordId);
        rowCount += 1;
        Assert.assertNull(recordDAO.readRecord(123));
    }

    @Test
    public void testReadAll() throws Exception {
        Assert.assertEquals(rowCount, recordDAO.readAll().size());
    }

    @Test
    public void testDelete() throws Exception {
        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setBookId(4);
        reservationRecord.setReaderId(5);
        reservationRecord.setIssueDate(new Date());
        reservationRecord.setReturnDate(new Date());
        int recordId = recordDAO.createRecord(reservationRecord);
        Assert.assertTrue(recordDAO.delete(recordId));
        Assert.assertNull(recordDAO.readRecord(recordId));
        Assert.assertFalse(recordDAO.delete(123));
    }
}

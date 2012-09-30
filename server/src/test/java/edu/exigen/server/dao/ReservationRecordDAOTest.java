package edu.exigen.server.dao;

import edu.exigen.entities.ReservationRecord;
import edu.exigen.server.dao.xml.XMLReservationRecordDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class ReservationRecordDAOTest {

    private String recordsFileName = "test_records.xml";

    @Before
    public void setUp() throws Exception {
        File testFile = new File(recordsFileName);
        testFile.delete();
    }

    @Test
    public void testAllDAOPositive() throws Exception {
        XMLReservationRecordDAO recordDAO = new XMLReservationRecordDAO(recordsFileName);
        recordDAO.loadStorage();
        Assert.assertEquals(0, recordDAO.readAll().size());
        ReservationRecord record = new ReservationRecord();
        record.setBookId(1);
        record.setReaderId(2);
        record.setIssueDate(new Date());
        Calendar returnDate = new GregorianCalendar();
        returnDate.add(Calendar.DAY_OF_MONTH, 14);
        record.setReturnDate(returnDate.getTime());
        Assert.assertEquals(1, recordDAO.createRecord(record));
        Assert.assertEquals(1, recordDAO.readAll().size());
        recordDAO.loadStorage();
        Assert.assertEquals(2, recordDAO.readRecord(1).getReaderId());
        Assert.assertTrue(recordDAO.delete(1));
        recordDAO.loadStorage();
        Assert.assertEquals(0, recordDAO.readAll().size());
    }

    @After
    public void tearDown() throws Exception {
        File testFile = new File(recordsFileName);
        testFile.delete();
    }
}

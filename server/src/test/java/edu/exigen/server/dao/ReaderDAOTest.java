package edu.exigen.server.dao;

import edu.exigen.entities.Reader;
import edu.exigen.server.dao.xml.XMLReaderDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class ReaderDAOTest {
    private String readersFile = "readersData.xml";

    @Test
    public void testCreateReader() throws Exception {
        ReaderDAO readerDAO = new XMLReaderDAO(readersFile);
        readerDAO.loadStorage();
        Assert.assertEquals(0, readerDAO.readAll().size());
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Ivanov");
        reader.setAddress("24 A Street, Moscow,2345656");
        reader.setDateOfBirth(new Date());
        Assert.assertEquals(1, readerDAO.createReader(reader));
        reader.setLastName("Petrov");
        Assert.assertEquals(2, readerDAO.createReader(reader));
        readerDAO.loadStorage();
        Assert.assertEquals("Petrov", readerDAO.readReader(2).getLastName());
        Assert.assertEquals(2, readerDAO.readAll().size());
    }

    @After
    public void tearDown() throws Exception {
        File testFile = new File(readersFile);
        testFile.delete();
    }
}

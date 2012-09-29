package edu.exigen.server.dao.hibernate;

import edu.exigen.entities.Reader;
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
public class HibernateReaderDAOTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresUnitHome");
    private static HibernateReaderDAO readerDAO = new HibernateReaderDAO(entityManagerFactory);
    private static int rowCounter = 0;

    @AfterClass
    public static void afterAll() {
        entityManagerFactory.close();
    }

    @Test
    public void testCreateReader() throws Exception {
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("26 Pushkin street, 27 APT");
        reader.setDateOfBirth(new Date());
        int readerId = readerDAO.createReader(reader);
        rowCounter += 1;
        Assert.assertEquals(reader.getAddress(), readerDAO.readReader(readerId).getAddress());
        Assert.assertTrue(readerId < readerDAO.createReader(new Reader()));
        rowCounter += 1;
    }

    @Test
    public void testReadReader() throws Exception {
        Reader reader = new Reader();
        reader.setFirstName("Petr");
        reader.setLastName("Ivanov");
        reader.setAddress("2 Gogol street, 123 APT");
        reader.setDateOfBirth(new Date());
        int readerId = readerDAO.createReader(reader);
        rowCounter += 1;
        Reader returnedReader = readerDAO.readReader(readerId);
        Assert.assertEquals(reader.getFirstName(), returnedReader.getFirstName());
        Assert.assertEquals(reader.getLastName(), returnedReader.getLastName());
        Assert.assertEquals(reader.getDateOfBirth(), returnedReader.getDateOfBirth());
        Assert.assertEquals(reader.getAddress(), returnedReader.getAddress());
    }


    @Test
    public void testReadAll() throws Exception {
        Assert.assertEquals(rowCounter, readerDAO.readAll().size());
    }

    @Test
    public void testUpdateReader() throws Exception {
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("26 Pushkin street, 27 APT");
        reader.setDateOfBirth(new Date());
        int readerId = readerDAO.createReader(reader);
        Reader newReader = new Reader();
        newReader.setFirstName("Igor");
        newReader.setLastName("Ivanov");
        newReader.setAddress("2 Gogol street, 123 APT");
        readerDAO.updateReader(readerId, newReader);
        Reader returnedReader = readerDAO.readReader(readerId);
        Assert.assertEquals(newReader.getFirstName(), returnedReader.getFirstName());
        Assert.assertEquals(newReader.getLastName(), returnedReader.getLastName());
        Assert.assertEquals(newReader.getDateOfBirth(), returnedReader.getDateOfBirth());
        Assert.assertEquals(newReader.getAddress(), returnedReader.getAddress());
        Assert.assertFalse(readerDAO.updateReader(143, new Reader()));
    }

    @Test
    public void testDelete() throws Exception {
        Reader reader = new Reader();
        reader.setFirstName("Ivan");
        reader.setLastName("Petrov");
        reader.setAddress("26 Pushkin street, 27 APT");
        reader.setDateOfBirth(new Date());
        int readerId = readerDAO.createReader(reader);
        rowCounter += 1;
        readerDAO.delete(readerId);
        Assert.assertEquals(--rowCounter, readerDAO.readAll().size());
        Assert.assertFalse(readerDAO.delete(153));
    }

}

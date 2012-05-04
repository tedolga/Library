package edu.exigen.server.storage;

import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.Reader;
import edu.exigen.client.entities.ReservationRecord;
import edu.exigen.server.IOUtils;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class StorageUtilsTest {
    private BookStorage bookStorage;
    private ReaderStorage readerStorage;
    private ReservationRecordStorage recordStorage;
    private File bookXML = new File("books.xml");
    private File readerXML = new File("reader.xml");
    private File recordXML = new File("records.xml");
    FileOutputStream outputStream;

    @Test
    public void testCreateStorage() throws Exception {
        //Book storage creation
        Book book = new Book();
        book.setId(1);
        book.setIsbn("BE-5656");
        book.setAuthor("B. R. Tomas");
        book.setTitle("Tom");
        book.setTopic("Biography");
        book.setYear(1998);
        List<Book> books = new ArrayList<Book>();
        books.add(book);
        bookStorage = new BookStorage();
        bookStorage.setElements(books);
        outputStream = null;
        try {
            outputStream = new FileOutputStream(bookXML);
            StorageUtils.createStorage(outputStream, bookStorage);
        } finally {
            IOUtils.closeSafely(outputStream);
        }
        Assert.assertTrue(bookXML.exists());

        //Reader storage creation
        Reader reader = new Reader();
        reader.setId(1);
        reader.setAddress("Pylkovckoe shosse 4");
        reader.setFirstName("Peter");
        reader.setLastName("Petrov");
        reader.setDateOfBirth(new Date());
        List<Reader> readers = new ArrayList<Reader>();
        readers.add(reader);
        readers.add(new Reader());
        readerStorage = new ReaderStorage();
        readerStorage.setElements(readers);
        try {
            outputStream = new FileOutputStream(readerXML);
            StorageUtils.createStorage(outputStream, readerStorage);
        } finally {
            IOUtils.closeSafely(outputStream);
        }
        Assert.assertTrue(readerXML.exists());

        //Records storage creation
        ReservationRecord record = new ReservationRecord();
        record.setId(1);
        record.setBookId(1);
        record.setReaderId(2);
        record.setIssueDate(new Date());
        record.setReturnDate(new Date(System.currentTimeMillis() + 2000000));
        List<ReservationRecord> records = new ArrayList<ReservationRecord>();
        records.add(record);
        recordStorage = new ReservationRecordStorage();
        recordStorage.setRecords(records);
        try {
            outputStream = new FileOutputStream(recordXML);
            StorageUtils.createStorage(outputStream, recordStorage);
        } finally {
            IOUtils.closeSafely(outputStream);
        }
        Assert.assertTrue(recordXML.exists());

    }

    @Test
    public void testRetrieveStorage() throws Exception {
        //Get books from file
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(bookXML);
            bookStorage = StorageUtils.retrieveStorage(inputStream);
        } finally {
            IOUtils.closeSafely(inputStream);
        }
        List<Book> books = bookStorage.getElements();
        Assert.assertEquals(1, books.size());
        Assert.assertEquals("BE-5656", books.get(0).getIsbn());

    }
}

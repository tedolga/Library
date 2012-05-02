package edu.exigen.server.storage;

import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.Reader;
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
    private File bookXML = new File("resources/books.xml");
    private File readerXML = new File("resources/reader.xml");
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
            if (outputStream != null) {
                outputStream.close();
            }
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
        ReaderStorage readerStorage = new ReaderStorage();
        readerStorage.setElements(readers);
        try {
            outputStream = new FileOutputStream(readerXML);
            StorageUtils.createStorage(outputStream, readerStorage);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        Assert.assertTrue(readerXML.exists());

    }

    @Test
    public void testRetrieveStorage() throws Exception {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(bookXML);
            bookStorage = StorageUtils.retrieveStorage(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        List<Book> books = bookStorage.getElements();
        Assert.assertEquals(1, books.size());
        Assert.assertEquals("BE-5656", books.get(0).getIsbn());

    }
}

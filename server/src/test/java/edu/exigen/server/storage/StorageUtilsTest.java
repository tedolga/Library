package edu.exigen.server.storage;

import edu.exigen.client.entities.Book;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class StorageUtilsTest {
    private Storage<Book> bookStorage;
    private File bookXML = new File("resources/books.xml");

    @Test
    public void testCreateStorage() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("BE-5656");
        book.setAuthor("B. R. Tomas");
        book.setTitle("Tom");
        book.setTopic("Biography");
        book.setYear(1998);
        List<Book> books = new ArrayList<Book>();
        books.add(book);
        bookStorage = new Storage<Book>();
        bookStorage.setElements(books);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(bookXML);
            StorageUtils.createStorage(outputStream, bookStorage);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        Assert.assertTrue(bookXML.exists());

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

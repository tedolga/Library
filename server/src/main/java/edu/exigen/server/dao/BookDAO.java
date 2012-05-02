package edu.exigen.server.dao;

import edu.exigen.client.entities.Book;
import edu.exigen.server.IOUtils;
import edu.exigen.server.storage.BookStorage;
import edu.exigen.server.storage.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookDAO {

    private final String storeFileName;
    private BookStorage storage;

    public BookDAO(String storeFileName) {
        this.storeFileName = storeFileName;
    }

    public int createBook(Book book) throws LibraryDAOException {
        int id = storage.incrementAndGet();
        book.setId(id);
        storage.addBook(book);
        updateStorage();
        return id;
    }

    public void loadStorage() throws LibraryDAOException {
        File storageFile = new File(storeFileName);
        FileInputStream inputStream = null;
        try {
            if (!storageFile.exists()) {
                storageFile.createNewFile();
                storage = new BookStorage();
            } else {
                inputStream = new FileInputStream(storeFileName);
                storage = StorageUtils.retrieveStorage(inputStream);
            }
        } catch (Exception e) {
            throw new LibraryDAOException("Can't load data, the storage " + storageFile.getAbsolutePath() + ".", e);
        } finally {
            IOUtils.closeSafely(inputStream);
        }
    }

    private void updateStorage() throws LibraryDAOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(storeFileName);
            StorageUtils.createStorage(outputStream, storage);
        } catch (Exception e) {
            throw new LibraryDAOException("Can't save data, the storage " + storeFileName + ".", e);
        } finally {
            IOUtils.closeSafely(outputStream);
        }
    }
}
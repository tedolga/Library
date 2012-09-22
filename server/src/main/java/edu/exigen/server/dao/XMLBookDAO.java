package edu.exigen.server.dao;

import edu.exigen.entities.Book;
import edu.exigen.server.IOUtils;
import edu.exigen.server.storage.BookStorage;
import edu.exigen.server.storage.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class XMLBookDAO implements BookDAO {

    private final String storeFileName;
    private BookStorage storage;

    public XMLBookDAO(String storeFileName) {
        this.storeFileName = storeFileName;
    }

    @Override
    public int createBook(Book book) throws LibraryDAOException {
        int id = storage.incrementAndGet();
        book.setId(id);
        storage.addBook(book);
        updateStorage();
        return id;
    }

    @Override
    public Book readBook(int id) throws LibraryDAOException {
        Book book = storage.getBook(id);
        if (book == null) {
            throw new LibraryDAOException("Book with id=" + id + " is not found");
        }
        return book;
    }

    @Override
    public List<Book> readAll() {
        return storage.getElements();
    }


    @Override
    public boolean updateBook(int id, Book newBook) throws LibraryDAOException {
        Book oldBook = readBook(id);
        oldBook.setIsbn(newBook.getIsbn());
        oldBook.setTitle(newBook.getTitle());
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setTopic(newBook.getTopic());
        oldBook.setYear(newBook.getYear());
        oldBook.setCount(newBook.getCount());
        updateStorage();
        return true;
    }

    @Override
    public boolean delete(int id) throws LibraryDAOException {
        Book book = readBook(id);
        boolean result = storage.removeBook(book);
        updateStorage();
        return result;
    }

    @Override
    public void loadStorage() throws LibraryDAOException {
        File storageFile = new File(storeFileName);
        FileInputStream inputStream = null;
        try {
            if (!storageFile.exists() || storageFile.length() == 0) {
                storage = new BookStorage();
            } else {
                inputStream = new FileInputStream(storeFileName);
                storage = StorageUtils.retrieveStorage(inputStream);
            }
        } catch (Exception e) {
            throw new LibraryDAOException("Can't load data from the storage " + storageFile.getAbsolutePath() + ".", e);
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
            throw new LibraryDAOException("Can't save data to the storage " + storeFileName + ".", e);
        } finally {
            IOUtils.closeSafely(outputStream);
        }
    }
}
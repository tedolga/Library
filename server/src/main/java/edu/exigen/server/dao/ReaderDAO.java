package edu.exigen.server.dao;

import edu.exigen.entities.Reader;
import edu.exigen.server.IOUtils;
import edu.exigen.server.storage.ReaderStorage;
import edu.exigen.server.storage.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class ReaderDAO {
    private final String storeFileName;
    private ReaderStorage storage;

    public ReaderDAO(String storeFileName) {
        this.storeFileName = storeFileName;
    }

    public int createReader(Reader reader) throws LibraryDAOException {
        int id = storage.incrementAndGet();
        reader.setId(id);
        storage.addReader(reader);
        updateStorage();
        return id;
    }

    public Reader readReader(int id) throws LibraryDAOException {
        Reader reader = storage.getReader(id);
        if (reader == null) {
            throw new LibraryDAOException("Reader with id=" + id + "is not found");
        }
        return reader;
    }

    public List<Reader> readAll() {
        return storage.getElements();
    }


    public boolean updateReader(int id, Reader newReader) throws LibraryDAOException {
        Reader oldReader = readReader(id);
        oldReader.setFirstName(newReader.getFirstName());
        oldReader.setLastName(newReader.getLastName());
        oldReader.setAddress(newReader.getAddress());
        oldReader.setDateOfBirth(newReader.getDateOfBirth());
        updateStorage();
        return true;
    }

    public boolean delete(int id) throws LibraryDAOException {
        Reader reader = readReader(id);
        boolean result = storage.removeReader(reader);
        updateStorage();
        return result;
    }

    public void loadStorage() throws LibraryDAOException {
        File storageFile = new File(storeFileName);
        FileInputStream inputStream = null;
        try {
            if (!storageFile.exists() || storageFile.length() == 0) {
                storage = new ReaderStorage();
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

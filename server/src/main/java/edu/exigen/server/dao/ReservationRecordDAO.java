package edu.exigen.server.dao;

import edu.exigen.client.entities.ReservationRecord;
import edu.exigen.server.IOUtils;
import edu.exigen.server.storage.ReservationRecordStorage;
import edu.exigen.server.storage.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class ReservationRecordDAO {

    private final String storeFileName;
    private ReservationRecordStorage storage;

    public ReservationRecordDAO(String storeFileName) {
        this.storeFileName = storeFileName;
    }


    public int createRecord(ReservationRecord record) throws LibraryDAOException {
        int id = storage.incrementAndGet();
        record.setId(id);
        storage.addRecord(record);
        updateStorage();
        return id;
    }

    public ReservationRecord readRecord(int id) throws LibraryDAOException {
        ReservationRecord record = storage.getRecord(id);
        if (record == null) {
            throw new LibraryDAOException("Reservation record with id=" + id + "is not found");
        }
        return record;
    }

    public List<ReservationRecord> readAll() {
        return storage.getRecords();
    }

    public boolean delete(int id) throws LibraryDAOException {
        ReservationRecord oldRecord = readRecord(id);
        boolean result = storage.removeRecord(oldRecord);
        updateStorage();
        return result;
    }

    public void loadStorage() throws LibraryDAOException {
        File storageFile = new File(storeFileName);
        FileInputStream inputStream = null;
        try {
            if (!storageFile.exists()) {
                storage = new ReservationRecordStorage();
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

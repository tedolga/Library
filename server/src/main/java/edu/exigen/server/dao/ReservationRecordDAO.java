package edu.exigen.server.dao;

import edu.exigen.entities.ReservationRecord;

import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface ReservationRecordDAO {
    int createRecord(ReservationRecord record) throws LibraryDAOException;

    ReservationRecord readRecord(int id) throws LibraryDAOException;

    List<ReservationRecord> readAll();

    boolean delete(int id) throws LibraryDAOException;

    void loadStorage() throws LibraryDAOException;
}

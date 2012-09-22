package edu.exigen.server.dao;

import edu.exigen.entities.Reader;

import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface ReaderDAO {
    int createReader(Reader reader) throws LibraryDAOException;

    Reader readReader(int id) throws LibraryDAOException;

    List<Reader> readAll();

    boolean updateReader(int id, Reader newReader) throws LibraryDAOException;

    boolean delete(int id) throws LibraryDAOException;

    void loadStorage() throws LibraryDAOException;
}

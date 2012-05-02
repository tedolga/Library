package edu.exigen.server.dao;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class LibraryDAOException extends Exception {

    public LibraryDAOException(String message) {
        super(message);
    }

    public LibraryDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}

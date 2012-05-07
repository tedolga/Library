package edu.exigen.server.provider;

import edu.exigen.client.entities.Reader;
import edu.exigen.server.dao.LibraryDAOException;
import edu.exigen.server.dao.ReaderDAO;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReaderProvider {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private ReaderDAO readerDAO;
    private ReservationRecordProvider recordProvider;
    private Map<String, HashSet<Reader>> searchCash = new HashMap<String, HashSet<Reader>>();

    public ReaderProvider(ReaderDAO readerDAO, ReservationRecordProvider recordProvider) {
        this.readerDAO = readerDAO;
        this.recordProvider = recordProvider;
    }

    public void createReader(Reader reader) throws LibraryProviderException {
        try {
            readerDAO.createReader(reader);
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        addReaderToSearchCash(reader);
    }

    public List<Reader> readAll() {
        return readerDAO.readAll();
    }

    public List<Reader> searchReaders(String searchString) {
        String[] searchTokens = searchString.toLowerCase().split(" ");
        Set<Reader> resultSet = new HashSet<Reader>();
        for (String token : searchTokens) {
            Set<Reader> foundReaders = searchCash.get(token);
            if (foundReaders != null) {
                resultSet.addAll(foundReaders);
            }
        }
        List<Reader> resultList = new ArrayList<Reader>();
        resultList.addAll(resultSet);
        return resultList;
    }

    public void updateReader(Reader oldReader, Reader newReader) throws LibraryProviderException {
        Reader copyOld = oldReader.copy();
        try {
            readerDAO.updateReader(oldReader.getId(), newReader);
            newReader.setId(oldReader.getId());
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        updateSearchCash(copyOld, newReader);
    }

    public void deleteReader(Reader reader) throws LibraryProviderException {
        checkDeletionChance(reader);
        try {
            readerDAO.delete(reader.getId());
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        removeReaderFromSearchCash(reader);
    }

    private void checkDeletionChance(Reader reader) throws LibraryProviderException {
        int reservedBooksCount = recordProvider.getReservedReaderBooks(reader).size();
        if (reservedBooksCount > 0) {
            throw new LibraryProviderException("Reader with id " + reader.getId() + " has " + reservedBooksCount +
                    "reserved books and cannot be deleted.");
        }
    }

    public void loadData() throws LibraryProviderException {
        try {
            readerDAO.loadStorage();
            recordProvider.loadData();
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        List<Reader> readers = readAll();
        for (Reader reader : readers) {
            addReaderToSearchCash(reader);
        }
    }


    private void updateSearchCash(Reader oldReader, Reader newReader) {
        removeReaderFromSearchCash(oldReader);
        addReaderToSearchCash(newReader);
    }

    private void removeReaderFromSearchCash(Reader oldReader) {
        removeWordsFromSearchCash(getReaderTokens(oldReader), oldReader);
    }

    private void removeWordsFromSearchCash(Iterable<String> readerTokens, Reader oldReader) {
        for (String word : readerTokens) {
            HashSet<Reader> readers = searchCash.get(word);
            if (readers != null) {
                readers.remove(oldReader);
                if (readers.size() < 1) {
                    searchCash.remove(word);
                }
            }
        }
    }

    private void addReaderToSearchCash(Reader reader) {
        addWordsToSearchCash(getReaderTokens(reader), reader);
    }

    private void addWordsToSearchCash(Iterable<String> readerTokens, Reader reader) {
        for (String word : readerTokens) {
            HashSet<Reader> readers = searchCash.get(word);
            if (readers != null) {
                readers.add(reader);
            } else {
                readers = new HashSet<Reader>();
                readers.add(reader);
                searchCash.put(word, readers);
            }
        }
    }

    private Iterable<String> getReaderTokens(Reader reader) {
        List<String> tokens = new ArrayList<String>();
        addFieldTokens(tokens, String.valueOf(reader.getId()).toLowerCase());
        addFieldTokens(tokens, reader.getFirstName().toLowerCase());
        addFieldTokens(tokens, reader.getLastName().toLowerCase());
        addFieldTokens(tokens, reader.getAddress().toLowerCase());
        addFieldTokens(tokens, dateFormat.format(reader.getDateOfBirth()).toLowerCase());
        return tokens;
    }

    private void addFieldTokens(List<String> allTokens, String field) {
        String[] tokenArray = field.split(" ");
        Collections.addAll(allTokens, tokenArray);
    }


}

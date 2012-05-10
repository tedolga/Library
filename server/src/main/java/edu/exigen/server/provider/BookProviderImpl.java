package edu.exigen.server.provider;

import edu.exigen.entities.Book;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Manages creation, deletion and update of books on server side.
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class BookProviderImpl extends UnicastRemoteObject implements BookProvider {

    /**
     * Map for cashing all books for appropriate isbn.
     */
    private Map<String, Book> isbnCash = new HashMap<String, Book>();

    /**
     * Map for cashing all books for appropriate key word.
     */
    private Map<String, HashSet<Book>> searchCash = new HashMap<String, HashSet<Book>>();

    /**
     * DAO for working with books.
     */
    private BookDAO bookDAO;

    /**
     * Provider for reservation information.
     */
    private ReservationRecordProviderImpl recordProvider;

    public BookProviderImpl(BookDAO bookDAO, ReservationRecordProviderImpl recordProvider) throws RemoteException {
        super();
        this.bookDAO = bookDAO;
        this.recordProvider = recordProvider;
    }

    /**
     * Creates new book in book storage. If book with the isbn as in new book is already exists, updates count
     * of books with appropriate isbn. Doesn't allow to create more than 5 copies of books with the same isbn.
     *
     * @param book for creation
     * @throws LibraryProviderException in case of LibraryProviderException
     * @throws RemoteException          in case of RemoteException
     */
    public synchronized void createBook(Book book) throws LibraryProviderException, RemoteException {
        checkISBNCount(book);
        Book sameBook = isbnCash.get(book.getIsbn());
        if (sameBook == null) {
            try {
                bookDAO.createBook(book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            addBookToSearchCash(book);
            addToISBNCash(book);
        } else {
            book.setCount(sameBook.getCount() + book.getCount());
            book.setId(sameBook.getId());
            try {
                bookDAO.updateBook(sameBook.getId(), book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(sameBook, book);
            updateSearchCash(sameBook, book);
        }
    }

    /**
     * Updates book information.
     *
     * @param oldBook book, which parameters should be updated.
     * @param newBook book, which parameters will be set to old book.
     * @throws LibraryProviderException in case of LibraryProviderException
     * @throws RemoteException          in case of RemoteException
     */
    public synchronized void updateBook(Book oldBook, Book newBook) throws LibraryProviderException, RemoteException {
        Book copyOld = oldBook.copyBook();
        int newBookCount = newBook.getCount();
        int reservedBookCount = recordProvider.getReservedBookCount(oldBook.getId());
        if (newBookCount < reservedBookCount) {
            throw new LibraryProviderException("Can't decrease count of copies of book with ISBN " + oldBook.getIsbn() +
                    " to " + newBookCount + ", minimum value is " + reservedBookCount);
        }
        if (newBook.getCount() <= 5) {
            try {
                bookDAO.updateBook(oldBook.getId(), newBook);
                newBook.setId(oldBook.getId());
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(copyOld, newBook);
            updateSearchCash(copyOld, newBook);
        } else {
            throw new LibraryProviderException("Count of books with ISBN " + newBook.getIsbn() + " can't be updated up " +
                    "to " + newBook.getCount() + ", maximum count of " +
                    "book copies should be less than 5, you can add no more than 5 books.");
        }
    }

    public synchronized void deleteBooks(Book book, int deleteCount) throws LibraryProviderException, RemoteException {
        int bookCount = getBookCount(book);
        checkDeletionChance(book, deleteCount);
        if (bookCount <= deleteCount) {
            try {
                bookDAO.delete(book.getId());
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            removeFromISBNCash(book);
            removeBookFromSearchCash(book);
        } else {
            Book copy = book.copyBook();
            book.setCount(bookCount - deleteCount);
            try {
                bookDAO.updateBook(book.getId(), book);
            } catch (LibraryDAOException e) {
                throw new LibraryProviderException(e.getMessage(), e);
            }
            updateISBNCash(copy, book);
            updateSearchCash(copy, book);
        }
    }

    public int getBookCount(Book book) throws RemoteException {
        return (isbnCash.get(book.getIsbn()) != null) ? isbnCash.get(book.getIsbn()).getCount() : 0;
    }

    public List<Book> searchBooks(String searchString) throws RemoteException {
        String[] searchTokens = searchString.toLowerCase().split(" ");
        Set<Book> resultSet = new HashSet<Book>();
        for (String token : searchTokens) {
            Set<Book> foundBooks = searchCash.get(token);
            if (foundBooks != null) {
                resultSet.addAll(foundBooks);
            }
        }
        List<Book> resultList = new ArrayList<Book>();
        resultList.addAll(resultSet);
        return resultList;
    }

    public List<Book> readAll() throws RemoteException {
        return bookDAO.readAll();
    }

    @Override
    public Book getBookById(int id) throws RemoteException, LibraryDAOException {
        return bookDAO.readBook(id);
    }

    public void loadData() throws LibraryProviderException, RemoteException {
        try {
            bookDAO.loadStorage();
            recordProvider.loadData();
        } catch (LibraryDAOException e) {
            throw new LibraryProviderException(e.getMessage(), e);
        }
        List<Book> books = readAll();
        for (Book book : books) {
            isbnCash.put(book.getIsbn(), book);
            addBookToSearchCash(book);
        }
    }

    private void updateISBNCash(Book oldBook, Book newBook) {
        removeFromISBNCash(oldBook);
        addToISBNCash(newBook);
    }

    private void removeFromISBNCash(Book oldBook) {
        isbnCash.remove(oldBook.getIsbn());
    }

    private void updateSearchCash(Book oldBook, Book newBook) {
        removeBookFromSearchCash(oldBook);
        addBookToSearchCash(newBook);
    }

    private void checkISBNCount(Book book) throws LibraryProviderException, RemoteException {
        int isbnCount = getBookCount(book);
        int newBookCount = book.getCount();
        if (!(isbnCount + newBookCount <= 5)) {
            throw new LibraryProviderException(newBookCount + " books with ISBN " + book.getIsbn() + " can't be added to library, maximum count of " +
                    "book copies should be less than 5, you can add no more than " + (5 - isbnCount) + " book(s).");
        }
    }

    private void addToISBNCash(Book book) {
        isbnCash.put(book.getIsbn(), book);
    }

    private void addBookToSearchCash(Book book) {
        addWordsToSearchCash(getBookTokens(book), book);
    }


    private void removeBookFromSearchCash(Book book) {
        removeTokensFromSearchCash(getBookTokens(book), book);
    }

    private void removeTokensFromSearchCash(Iterable<String> tokens, Book book) {
        for (String word : tokens) {
            HashSet<Book> books = searchCash.get(word);
            if (books != null) {
                books.remove(book);
                if (books.size() < 1) {
                    searchCash.remove(word);
                }
            }
        }
    }

    private void addWordsToSearchCash(Iterable<String> tokens, Book book) {
        for (String word : tokens) {
            HashSet<Book> books = searchCash.get(word);
            if (books != null) {
                books.add(book);
            } else {
                books = new HashSet<Book>();
                books.add(book);
                searchCash.put(word, books);
            }
        }
    }

    private Iterable<String> getBookTokens(Book book) {
        List<String> tokens = new ArrayList<String>();
        addFieldTokens(tokens, book.getIsbn().toLowerCase());
        addFieldTokens(tokens, book.getTitle().toLowerCase());
        addFieldTokens(tokens, book.getAuthor().toLowerCase());
        addFieldTokens(tokens, book.getTopic().toLowerCase());
        addFieldTokens(tokens, String.valueOf(book.getYear()).toLowerCase());
        return tokens;
    }

    private void addFieldTokens(Collection<String> allTokens, String field) {
        String[] tokenArray = field.split(" ");
        Collections.addAll(allTokens, tokenArray);
    }

    private void checkDeletionChance(Book book, int deleteCount) throws LibraryProviderException, RemoteException {
        int bookCount = getBookCount(book);
        int reservedCount = recordProvider.getReservedBookCount(book.getId());
        int availableCount = bookCount - reservedCount;
        if (availableCount < deleteCount) {
            throw new LibraryProviderException("Can't delete" + deleteCount + " book(s) with ISBN '" + book.getIsbn() + "'. There are " +
                    reservedCount + " book(s) reserved and " + availableCount + " book(s) available.");
        }
    }
}

package edu.exigen.server.storage;

import edu.exigen.entities.Book;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */
@XmlRootElement(name = "bookStorage")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookStorage {

    @XmlElement
    private int id;

    @XmlElement(name = "book")
    private List<Book> books = new ArrayList<Book>();

    public List<Book> getElements() {
        return books;
    }

    public void setElements(List<Book> books) {
        this.books = books;
    }

    public synchronized int incrementAndGet() {
        return ++id;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Book getBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public boolean removeBook(Book book) {
        return books.remove(book);
    }
}

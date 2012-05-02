package edu.exigen.server.storage;

import edu.exigen.client.entities.Book;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */

@XmlRootElement(name = "books")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookStorage extends Storage {

    @XmlElement
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

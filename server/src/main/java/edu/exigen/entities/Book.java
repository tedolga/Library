package edu.exigen.entities;

import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;
import edu.exigen.server.dao.hibernate.HibernateBookDAO;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author O. Tedikova
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "BOOKS")
public class Book {

    public static void main(String[] args) throws LibraryDAOException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresUnit");
        BookDAO bookDAO = new HibernateBookDAO();
        Book book = new Book();
        book.setAuthor("Pushkin");
        book.setIsbn("ISBN-67676-09-98");
        book.setTitle("Fairy tales");
        book.setTopic("Classic");
        book.setYear(1980);
        book.setCount(4);
        System.out.println(bookDAO.createBook(book));
    }

    @XmlElement
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @XmlElement
    @Column(name = "ISBN")
    private String isbn;

    @XmlElement
    @Column(name = "TITLE")
    private String title;

    @XmlElement
    @Column(name = "AUTHOR")
    private String author;

    @XmlElement
    @Column(name = "TOPIC")
    private String topic;

    @XmlElement
    @Column(name = "YEAR")
    private int year;

    @XmlElement
    @Column(name = "COUNT")
    private int count;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCount() {
        return count;
    }

    public synchronized int incrementAndGetCount() {
        return ++count;
    }

    public synchronized int decrementAndGetCount() {
        return --count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Book copyBook() {
        Book copy = new Book();
        copy.setId(this.getId());
        copy.setIsbn(isbn);
        copy.setTitle(title);
        copy.setAuthor(author);
        copy.setTopic(topic);
        copy.setYear(year);
        copy.setCount(count);
        return copy;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", topic='" + topic + '\'' +
                ", year=" + year +
                ", count=" + count +
                '}';
    }
}

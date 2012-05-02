package edu.exigen.client.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author O. Tedikova
 * @version 1.0
 */

@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
public class Book extends Entity {

    @XmlElement
    private String isbn;
    @XmlElement
    private String title;
    @XmlElement
    private String author;
    @XmlElement
    private String topic;
    @XmlElement
    private int year;

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
}

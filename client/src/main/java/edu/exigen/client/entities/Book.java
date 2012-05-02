package edu.exigen.client.entities;

import java.util.Calendar;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class Book extends Entity {

    private String isbn;
    private String title;
    private String author;
    private String topic;
    private Calendar year;

    public Book(int id) {
        super(id);
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

    public Calendar getYear() {
        return year;
    }

    public void setYear(Calendar year) {
        this.year = year;
    }
}

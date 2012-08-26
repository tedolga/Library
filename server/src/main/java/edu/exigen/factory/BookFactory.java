package edu.exigen.factory;

import edu.exigen.entities.Book;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookFactory {

    public static Book getObject(String isbn, String title, String author, String topic, int year, int count) throws Exception {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setTopic(topic);
        book.setYear(year);
        book.setCount(count);
        return book;
    }
}

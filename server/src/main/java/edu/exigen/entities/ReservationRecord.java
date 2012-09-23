package edu.exigen.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * @author O. Tedikova
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReservationRecord {

    @XmlElement
    private int readerId;
    @XmlElement
    private int bookId;
    @XmlElement
    private Date issueDate;
    @XmlElement
    private Date returnDate;

    @XmlElement
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReaderId() {
        return readerId;
    }

    public int getBookId() {
        return bookId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}

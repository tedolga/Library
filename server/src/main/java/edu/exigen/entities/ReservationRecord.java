package edu.exigen.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author O. Tedikova
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "RESERVATION_RECORDS")
public class ReservationRecord implements Serializable {

    @XmlElement
    @Column(name = "READER_ID")
    private int readerId;
    @XmlElement
    @Column(name = "BOOK_ID")
    private int bookId;
    @XmlElement
    @Column(name = "ISSUE_DATE")
    private Date issueDate;
    @XmlElement
    @Column(name = "RETURN_DATE")
    private Date returnDate;

    @XmlElement
    @Id
    @GeneratedValue
    @Column(name = "ID")
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

package edu.exigen.client.entities;

import java.util.Date;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class ReservationRecord extends Entity {
    private int readerId;
    private int bookId;
    private Date issueDate;
    private Date dateOfReturn;

    public int getReaderId() {
        return readerId;
    }

    public int getBookId() {
        return bookId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
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

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }
}

package edu.exigen.client.entities;

import java.util.Calendar;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class ReservationRecord extends Entity {
    private int readerId;
    private int bookId;
    private Calendar issueDate;
    private Calendar dateOfReturn;

    public int getReaderId() {
        return readerId;
    }

    public int getBookId() {
        return bookId;
    }

    public Calendar getIssueDate() {
        return issueDate;
    }

    public Calendar getDateOfReturn() {
        return dateOfReturn;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setIssueDate(Calendar issueDate) {
        this.issueDate = issueDate;
    }

    public void setDateOfReturn(Calendar dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }
}

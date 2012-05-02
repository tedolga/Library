package edu.exigen.client.entities;

import java.util.Calendar;

/**
 * @author O. Tedikova
 * @version 1.0
 */
public class ReservationRecord extends Entity {
    private final int readerId;
    private final int bookId;
    private final Calendar issueDate;
    private final Calendar dateOfReturn;

    public ReservationRecord(int id, int readerId, int bookId, Calendar issueDate, Calendar dateOfReturn) {
        super(id);
        this.readerId = readerId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dateOfReturn = dateOfReturn;
    }

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
}

package edu.exigen.client.gui;

import edu.exigen.LibraryConstraints;
import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.ReservationRecord;
import edu.exigen.server.provider.BookProvider;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class RecordTableModel extends AbstractTableModel {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(LibraryConstraints.LIBRARY_DATE_PATTERN);
    BookProvider bookProvider;
    private List<ReservationRecord> tableData;

    public RecordTableModel(List<ReservationRecord> tableData, BookProvider bookProvider) {
        this.tableData = tableData;
        this.bookProvider = bookProvider;
    }

    public int getRowCount() {
        return tableData.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "id";
            case 1:
                return "Library card";
            case 2:
                return "BookISBN";
            case 3:
                return "Date of Issue";
            case 4:
                return "Date of Return";
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            default:
                return Object.class;
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ReservationRecord record = tableData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return record.getId();
            case 1:
                return record.getReaderId();
            case 2:
                Book book;
                try {
                    book = bookProvider.getBookById(record.getBookId());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                return (book != null) ? book.getIsbn() : "";
            case 3:
                return dateFormat.format(record.getIssueDate());
            case 4:
                return dateFormat.format(record.getReturnDate());
            default:
                return Object.class;
        }
    }

    public void setTableData(List<ReservationRecord> tableData) {
        this.tableData = tableData;
    }

    public List<ReservationRecord> getTableData() {
        return tableData;
    }
}

package edu.exigen.client.gui;

import edu.exigen.entities.Book;
import edu.exigen.entities.ReservationRecord;
import edu.exigen.server.ProvidersHolder;
import edu.exigen.server.provider.BookProvider;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class RecordTableModel extends AbstractTableModel {

    private List<ReservationRecord> tableData;
    private ProvidersHolder providersHolder;

    public RecordTableModel(List<ReservationRecord> tableData, ProvidersHolder providersHolder) {
        this.tableData = tableData;
        this.providersHolder = providersHolder;
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
        return getValueAt(0, columnIndex).getClass();
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
                    book = providersHolder.getBookProvider().getBookById(record.getBookId());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                return (book != null) ? book.getIsbn() : "";
            case 3:
                return record.getIssueDate();
            case 4:
                return record.getReturnDate();
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

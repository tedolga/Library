package edu.exigen.client.gui;

import edu.exigen.entities.Book;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookTableModel extends AbstractTableModel {
    private List<Book> tableData;

    public BookTableModel(List<Book> tableData) {
        this.tableData = tableData;
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
                return "ISBN";
            case 1:
                return "Title";
            case 2:
                return "Author";
            case 3:
                return "Topic";
            case 4:
                return "Year";
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return Integer.class;
            default:
                return Object.class;
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = tableData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return book.getIsbn();
            case 1:
                return book.getTitle();
            case 2:
                return book.getAuthor();
            case 3:
                return book.getTopic();
            case 4:
                return book.getYear();
            default:
                return Object.class;
        }
    }

    public void setTableData(List<Book> tableData) {
        this.tableData = tableData;
    }

    public List<Book> getTableData() {
        return tableData;
    }
}

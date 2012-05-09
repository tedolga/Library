package edu.exigen.client.gui;

import edu.exigen.client.entities.Book;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookTableModel implements TableModel {
    private List<Book> tableData;

    public BookTableModel(List<Book> tableData) {
        this.tableData = tableData;
    }

    public int getRowCount() {
        return tableData.size();
    }

    public int getColumnCount() {
        return 6;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "id";
            case 1:
                return "ISBN";
            case 2:
                return "Title";
            case 3:
                return "Author";
            case 4:
                return "Topic";
            case 5:
                return "Year";
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return Integer.class;
            default:
                return Object.class;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = tableData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return book.getId();
            case 1:
                return book.getIsbn();
            case 2:
                return book.getTitle();
            case 3:
                return book.getAuthor();
            case 4:
                return book.getTopic();
            case 5:
                return book.getYear();
            default:
                return Object.class;
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    public void addTableModelListener(TableModelListener l) {

    }

    public void removeTableModelListener(TableModelListener l) {

    }

    public List<Book> getTableData() {
        return tableData;
    }

    public void setTableData(List<Book> tableData) {
        this.tableData = tableData;
    }
}
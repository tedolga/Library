package edu.exigen.client.gui;

import edu.exigen.LibraryConstraints;
import edu.exigen.client.entities.Reader;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReaderTableModel extends AbstractTableModel {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(LibraryConstraints.LIBRARY_DATE_PATTERN);
    private List<Reader> tableData;

    public ReaderTableModel(List<Reader> tableData) {
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
                return "id";
            case 1:
                return "First Name";
            case 2:
                return "Last Name";
            case 3:
                return "Address";
            case 4:
                return "Date of Birth";
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
            default:
                return Object.class;
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Reader reader = tableData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return reader.getId();
            case 1:
                return reader.getFirstName();
            case 2:
                return reader.getLastName();
            case 3:
                return reader.getAddress();
            case 4:
                return dateFormat.format(reader.getDateOfBirth());
            default:
                return Object.class;
        }
    }

    public List<Reader> getTableData() {
        return tableData;
    }

    public void setTableData(List<Reader> tableData) {
        this.tableData = tableData;
    }
}

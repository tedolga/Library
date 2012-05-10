package edu.exigen.client.gui;

import edu.exigen.entities.ReservationRecord;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;
import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.BookProviderImpl;
import edu.exigen.server.provider.ReservationRecordProvider;
import edu.exigen.server.provider.ReservationRecordProviderImpl;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class RecordAdminComponent {

    private static final String PANEL_NAME = "Reservation Record Administration";
    private static final String VIEW_PANEL_NAME = "Available Records";

    private JPanel recordAdminPanel;
    private BookProvider bookProvider;
    private ReservationRecordProvider recordProvider;
    private RecordTableModel recordTableModel;
    private JTextField libraryCardField;
    private JTextField isbnField;
    private JXDatePicker issueDateField;
    private JXDatePicker returnDateField;
    private JPanel recordSummaryPanel;
    private JTable recordTable;
    private ReservationRecord tableRecord;

    public RecordAdminComponent(BookProvider bookProvider, ReservationRecordProvider recordProvider) throws RemoteException {
        this.bookProvider = bookProvider;
        this.recordProvider = recordProvider;
        initComponents();
    }

    private void initComponents() throws RemoteException {
        JComponent dataViewPanel = createDataViewPanel();
        addRecordSelectionListener(new RecordSelectionListener() {
            @Override
            public void recordSelected(ReservationRecord selectedRecord) {
                tableRecord = selectedRecord;
                libraryCardField.setText(selectedRecord != null ? String.valueOf(selectedRecord.getReaderId()) : "");
                try {
                    isbnField.setText(selectedRecord != null ? bookProvider.getBookById(selectedRecord.getBookId()).getIsbn() : "");
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                issueDateField.setDate(selectedRecord != null ? selectedRecord.getIssueDate() : null);
                returnDateField.setDate(selectedRecord != null ? selectedRecord.getReturnDate() : null);
            }
        });
        RecordSummaryComponent recordSummaryComponent = new RecordSummaryComponent();
        recordSummaryPanel = recordSummaryComponent.getRecordSummaryPanel();
        libraryCardField = recordSummaryComponent.getLibraryCardField();
        isbnField = recordSummaryComponent.getIsbnField();
        issueDateField = recordSummaryComponent.getIssueDateField();
        returnDateField = recordSummaryComponent.getReturnDateField();
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteRecordListener());
        recordAdminPanel = new JPanel();
        recordAdminPanel.setName(PANEL_NAME);
        recordAdminPanel.setLayout(new BorderLayout());
        recordAdminPanel.add(dataViewPanel, BorderLayout.NORTH);
        recordAdminPanel.add(recordSummaryPanel, BorderLayout.CENTER);
        recordAdminPanel.add(deleteButton, BorderLayout.SOUTH);
    }

    private JComponent createDataViewPanel() throws RemoteException {
        recordTableModel = new RecordTableModel(recordProvider.readAll(), bookProvider);
        recordTable = new JTable(recordTableModel);
        recordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return new JScrollPane(recordTable);
    }

    public JPanel getRecordAdminPanel() {
        return recordAdminPanel;
    }

    private class RefreshButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            List<ReservationRecord> records;
            int rowCount = recordTableModel.getRowCount();
            recordTableModel.setTableData(Collections.<ReservationRecord>emptyList());
            recordTableModel.fireTableRowsDeleted(0, Math.max(0, rowCount - 1));
            try {
                records = recordProvider.readAll();
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            recordTableModel.setTableData(records);
            recordTableModel.fireTableRowsInserted(0, Math.max(0, records.size() - 1));
        }
    }

    public void addRecordSelectionListener(final RecordSelectionListener selectionListener) {
        final ListSelectionModel model = recordTable.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                ReservationRecord selectedRecord = null;
                if (!lsm.isSelectionEmpty()) {
                    int selectedRow = lsm.getMinSelectionIndex();
                    selectedRecord = recordTableModel.getTableData().get(selectedRow);
                }
                selectionListener.recordSelected(selectedRecord);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Test reader admin component");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        final BookDAO bookStore = new BookDAO("bookStore");
        bookStore.loadStorage();
        final ReservationRecordDAO recordDAO = new ReservationRecordDAO("reservationStore");
        recordDAO.loadStorage();
        final ReaderDAO readerDAO = new ReaderDAO("readerStore");
        readerDAO.loadStorage();
        final ReservationRecordProviderImpl reservationRecordProvider = new ReservationRecordProviderImpl(bookStore, readerDAO, recordDAO);
        final RecordAdminComponent recordAdminComponent = new RecordAdminComponent(new BookProviderImpl(bookStore, reservationRecordProvider), reservationRecordProvider);
        frame.setContentPane(recordAdminComponent.getRecordAdminPanel());
        frame.setVisible(true);
        frame.pack();
    }

    private class DeleteRecordListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                recordProvider.deleteRecord(tableRecord);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }
}

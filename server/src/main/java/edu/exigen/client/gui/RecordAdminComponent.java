package edu.exigen.client.gui;

import edu.exigen.client.entities.ReservationRecord;
import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.ReservationRecordProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class RecordAdminComponent {
    private static final String PANEL_NAME = "Reservation Record Administration";
    private static final String VIEW_PANEL_NAME = "Available Records";
    private static final String REFRESH_BUTTON_TEXT = "Refresh";

    private JPanel recordAdminPanel;
    private BookProvider bookProvider;
    private ReservationRecordProvider recordProvider;
    private RecordTableModel recordTableModel;
    private JTextField libraryCardField;
    private JTextField isbnField;
    private JTextField issueDateField;
    private JTextField returnDateField;
    private JPanel recordSummaryPanel;

    public RecordAdminComponent(BookProvider bookProvider, ReservationRecordProvider recordProvider) throws RemoteException {
        this.bookProvider = bookProvider;
        this.recordProvider = recordProvider;
        initComponents();
    }

    private void initComponents() throws RemoteException {
        JPanel dataViewPanel = createDataViewPanel();
        recordSummaryPanel = new RecordSummaryComponent().getRecordSummaryPanel();
        JButton deleteButton = new JButton("Delete");
        recordAdminPanel = new JPanel();
        recordAdminPanel.setName(PANEL_NAME);
        recordAdminPanel.setLayout(new BorderLayout());
        recordAdminPanel.add(dataViewPanel, BorderLayout.NORTH);
        recordAdminPanel.add(recordSummaryPanel, BorderLayout.CENTER);
        recordAdminPanel.add(deleteButton, BorderLayout.SOUTH);
    }

    private JPanel createDataViewPanel() throws RemoteException {
        JPanel dataViewPanel = new JPanel();
        dataViewPanel.setBorder(BorderFactory.createTitledBorder(VIEW_PANEL_NAME));
        recordTableModel = new RecordTableModel(recordProvider.readAll(), bookProvider);
        JTable readerTable = new JTable(recordTableModel);
        readerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        readerTable.setPreferredScrollableViewportSize(new Dimension(600, 300));
        JScrollPane scrollPane = new JScrollPane(readerTable);
        JButton refreshButton = new JButton(REFRESH_BUTTON_TEXT);
        refreshButton.addActionListener(new RefreshButtonListener());
        dataViewPanel.add(scrollPane, BorderLayout.CENTER);
        dataViewPanel.add(refreshButton, BorderLayout.SOUTH);
        return dataViewPanel;
    }


    public JPanel getRecordAdminPanel() {
        return recordAdminPanel;
    }

    public JPanel getRecordSummaryPanel() {
        return recordSummaryPanel;
    }

    private class RefreshButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            List<ReservationRecord> records;
            try {
                records = recordProvider.readAll();
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            recordTableModel.setTableData(records);
            recordTableModel.fireTableRowsInserted(0, records.size() - 1);
        }
    }
}

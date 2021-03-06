package edu.exigen.client.gui;

import edu.exigen.entities.Reader;
import edu.exigen.server.ProvidersHolder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReaderSearchComponent {
    private static final String PANEL_NAME = "Reader Search";
    private static final String SEARCH_LABEL = "Search: ";

    private JPanel readerSearchPanel;
    private JTextField searchField;

    private ReaderTableModel readerTableModel;
    private JTable readerTable;
    private ProvidersHolder providersHolder;

    public ReaderSearchComponent(ProvidersHolder providersHolder) throws RemoteException {
        this.providersHolder = providersHolder;
        initComponents();
    }

    private void initComponents() throws RemoteException {
        JPanel dataEnterPanel = createDataEnterPanel();
        readerTableModel = new ReaderTableModel(providersHolder.getReaderProvider().readAll());
        readerTable = new JTable(readerTableModel);
        readerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(readerTable);
        readerTable.setPreferredScrollableViewportSize(new Dimension(600, 300));
        readerSearchPanel = new JPanel();
        readerSearchPanel.setBorder(BorderFactory.createTitledBorder(PANEL_NAME));
        readerSearchPanel.setLayout(new BorderLayout());
        readerSearchPanel.add(dataEnterPanel, BorderLayout.NORTH);
        readerSearchPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getReaderSearchPanel() {
        return readerSearchPanel;
    }

    public JPanel createDataEnterPanel() {
        JLabel searchLabel = new JLabel(SEARCH_LABEL);
        searchField = new JTextField();
        searchField.registerKeyboardAction(new SearchListener(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
        JPanel dataEnterPanel = new JPanel();
        dataEnterPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        dataEnterPanel.add(searchLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        dataEnterPanel.add(searchField, c);
        return dataEnterPanel;
    }


    private class SearchListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            List<Reader> readers;
            int rowCount = readerTableModel.getRowCount();
            readerTableModel.setTableData(Collections.<Reader>emptyList());
            readerTableModel.fireTableRowsDeleted(0, Math.max(0, rowCount - 1));
            try {
                if (!"".equals(searchField.getText())) {
                    readers = providersHolder.getReaderProvider().searchReaders(searchField.getText());
                } else {
                    readers = providersHolder.getReaderProvider().readAll();
                }
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            readerTableModel.setTableData(readers);
            readerTableModel.fireTableRowsInserted(0, Math.max(0, readers.size() - 1));
        }
    }

    public void addReaderSelectionListener(final ReaderSelectionListener selectionListener) {
        final ListSelectionModel model = readerTable.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                Reader selectedReader = null;
                if (!lsm.isSelectionEmpty()) {
                    int selectedRow = lsm.getMinSelectionIndex();
                    selectedReader = readerTableModel.getTableData().get(selectedRow);
                }
                selectionListener.readerSelected(selectedReader);
            }
        });
    }
}

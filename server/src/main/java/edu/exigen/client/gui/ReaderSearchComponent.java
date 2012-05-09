package edu.exigen.client.gui;

import edu.exigen.client.entities.Reader;
import edu.exigen.server.provider.ReaderProvider;

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
public class ReaderSearchComponent {
    private static final String PANEL_NAME = "Reader Search";
    private static final String SEARCH_LABEL = "Search: ";
    private static final String SEARCH_BUTTON_TEXT = "Search";

    private JPanel readerSearchPanel;
    private JTextField searchField;

    private ReaderProvider readerProvider;
    private ReaderTableModel readerTableModel;

    public ReaderSearchComponent(ReaderProvider readerProvider) throws RemoteException {
        this.readerProvider = readerProvider;
        initComponents();
    }

    private void initComponents() throws RemoteException {
        JPanel dataEnterPanel = createDataEnterPanel();
        readerTableModel = new ReaderTableModel(readerProvider.readAll());
        JTable readerTable = new JTable(readerTableModel);
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
        JButton searchButton = new JButton(SEARCH_BUTTON_TEXT);
        searchButton.addActionListener(new SearchButtonListener());
        JPanel dataEnterPanel = new JPanel();
        dataEnterPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.EAST;
        c.weighty = 1;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        dataEnterPanel.add(searchLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        dataEnterPanel.add(searchField, c);
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 2;
        c.weightx = 0.5;
        dataEnterPanel.add(searchButton, c);
        return dataEnterPanel;
    }


    private class SearchButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            List<Reader> readers;
            try {
                if (!"".equals(searchField.getText())) {
                    readers = readerProvider.searchReaders(searchField.getText());
                } else {
                    readers = readerProvider.readAll();
                }
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            readerTableModel.setTableData(readers);
            readerTableModel.fireTableRowsInserted(0, readers.size() - 1);
        }
    }
}

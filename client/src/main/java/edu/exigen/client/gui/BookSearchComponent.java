package edu.exigen.client.gui;

import edu.exigen.entities.Book;
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
public class BookSearchComponent {
    private static final String PANEL_NAME = "Book Search";
    private static final String SEARCH_LABEL = "Search: ";

    private JPanel bookSearchPanel;
    private JTextField searchField;

    private ProvidersHolder providersHolder;
    private BookTableModel bookTableModel;
    private JTable bookTable;

    public BookSearchComponent(ProvidersHolder providersHolder) throws RemoteException {
        this.providersHolder = providersHolder;
        initComponents();
    }

    private void initComponents() throws RemoteException {
        JPanel dataEnterPanel = createDataEnterPanel();
        bookTableModel = new BookTableModel(providersHolder.getBookProvider().readAll());
        bookTable = new JTable(bookTableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookSearchPanel = new JPanel();
        bookSearchPanel.setBorder(BorderFactory.createTitledBorder(PANEL_NAME));
        bookSearchPanel.setLayout(new BorderLayout());
        bookSearchPanel.add(dataEnterPanel, BorderLayout.NORTH);
        bookSearchPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getBookSearchPanel() {
        return bookSearchPanel;
    }

    public JPanel createDataEnterPanel() {
        JLabel searchLabel = new JLabel(SEARCH_LABEL);
        searchField = new JTextField();
        searchField.registerKeyboardAction(new SearchListener(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
        JPanel dataEnterPanel = new JPanel();
        dataEnterPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.weighty = 1;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
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
            List<Book> books;
            int rowCount = bookTableModel.getRowCount();
            bookTableModel.setTableData(Collections.<Book>emptyList());
            bookTableModel.fireTableRowsDeleted(0, Math.max(0, rowCount - 1));
            try {
                if (!"".equals(searchField.getText())) {
                    books = providersHolder.getBookProvider().searchBooks(searchField.getText());
                } else {
                    books = providersHolder.getBookProvider().readAll();
                }
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            bookTableModel.setTableData(books);
            bookTableModel.fireTableRowsInserted(0, Math.max(0, books.size() - 1));
        }
    }

    public void addBookSelectionListener(final BookSelectionListener selectionListener) {
        final ListSelectionModel model = bookTable.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                Book selectedBook = null;
                if (!lsm.isSelectionEmpty()) {
                    int selectedRow = lsm.getMinSelectionIndex();
                    selectedBook = bookTableModel.getTableData().get(selectedRow);
                }
                selectionListener.bookSelected(selectedBook);
            }

        });
    }
}

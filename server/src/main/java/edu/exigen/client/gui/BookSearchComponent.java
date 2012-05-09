package edu.exigen.client.gui;

import edu.exigen.client.entities.Book;
import edu.exigen.server.provider.BookProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookSearchComponent {
    private static final String PANEL_NAME = "Book Search";
    private static final String SEARCH_LABEL = "Search: ";
    private static final String GET_ALL_BUTTON_TEXT = "Get ALL";
    private static final String SEARCH_BUTTON_TEXT = "Search";

    private JPanel bookSearchPanel;
    private JTextField searchField;
    private JButton getAllButton;
    private JButton searchButton;
    private JTable bookTable;

    private BookProvider bookProvider;
    private BookTableModel bookTableModel;

    public BookSearchComponent(BookProvider bookProvider) {
        this.bookProvider = bookProvider;
        initComponents();
    }

    private void initComponents() {
        JPanel dataEnterPanel = createDataEnterPanel();
        bookTableModel = new BookTableModel(new ArrayList<Book>());
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookTable.setPreferredScrollableViewportSize(new Dimension(600, 300));
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
        searchButton = new JButton(SEARCH_BUTTON_TEXT);
        searchButton.addActionListener(new SearchButtonListener());
        getAllButton = new JButton(GET_ALL_BUTTON_TEXT);
        getAllButton.addActionListener(new GetALLButtonListener());
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
        c.weighty = 0;
        c.gridy = 2;
        dataEnterPanel.add(getAllButton, c);
        return dataEnterPanel;
    }

    public Book getSelectedBook() {
        return new Book();
    }


    private class SearchButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            List<Book> books;
            try {
                books = bookProvider.searchBooks(searchField.getText());
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            bookTableModel.setTableData(books);
        }
    }

    private class GetALLButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            List<Book> books;
            try {
                books = bookProvider.readAll();
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            bookTableModel.setTableData(books);
            bookTableModel.fireTableRowsInserted(0, books.size() - 1);
        }
    }
}

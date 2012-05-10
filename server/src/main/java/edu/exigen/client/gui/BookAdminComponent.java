package edu.exigen.client.gui;

import edu.exigen.client.entities.Book;
import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.LibraryProviderException;
import edu.exigen.server.provider.ReservationRecordProvider;

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
public class BookAdminComponent {
    private static final String ADMIN_PANEL_NAME = "Book Administration";
    private static final String CREATE_BUTTON_NAME = "Create";
    private static final String UPDATE_BUTTON_NAME = "Update";
    private static final String DELETE_BUTTON_NAME = "Delete";

    private BookProvider bookProvider;
    private ReservationRecordProvider reservationRecordProvider;
    private BookSearchComponent searchComponent;
    private Book tableBook;
    private JTextField isbnField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField topicField;
    private JTextField yearField;
    private JTextField countField;
    private JPanel adminPanel;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField reservedBookCountField;

    public BookAdminComponent(BookProvider bookProvider, ReservationRecordProvider reservationRecordProvider) throws RemoteException {
        this.bookProvider = bookProvider;
        this.reservationRecordProvider = reservationRecordProvider;
        searchComponent = new BookSearchComponent(bookProvider);
        initComponents();
    }

    public void initComponents() {
        adminPanel = new JPanel();
        adminPanel.setName(ADMIN_PANEL_NAME);
        JPanel searchPanel = searchComponent.getBookSearchPanel();
        JPanel bookAdminPanel = createAdminPanel();
        searchComponent.addBookSelectionListener(new BookSelectionListener() {
            @Override
            public void bookSelected(Book selectedBook) {
                isbnField.setText(selectedBook != null ? selectedBook.getIsbn() : "");
                titleField.setText(selectedBook != null ? selectedBook.getTitle() : "");
                authorField.setText(selectedBook != null ? selectedBook.getAuthor() : "");
                topicField.setText(selectedBook != null ? selectedBook.getTopic() : "");
                yearField.setText(selectedBook != null ? String.valueOf(selectedBook.getYear()) : "");
                countField.setText(selectedBook != null ? String.valueOf(selectedBook.getCount()) : "");
                try {
                    reservedBookCountField.setText(selectedBook != null ? String.valueOf
                            (reservationRecordProvider.getReservedBookCount(selectedBook.getId())) : "");
                } catch (RemoteException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                tableBook = selectedBook;
            }
        });
        adminPanel.setLayout(new BorderLayout());
        adminPanel.add(searchPanel, BorderLayout.NORTH);
        adminPanel.add(bookAdminPanel, BorderLayout.CENTER);
        JPanel buttonPanel = createButtonPanel();
        adminPanel.add(buttonPanel, BorderLayout.SOUTH);
        adminPanel.setBorder(BorderFactory.createTitledBorder(ADMIN_PANEL_NAME));
    }

    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridBagLayout());
        List<JComponent> adminComponents = new ArrayList<JComponent>();
        JLabel isbn = new JLabel("ISBN :");
        adminComponents.add(isbn);
        isbnField = new JTextField();
        adminComponents.add(isbnField);
        JLabel titleLabel = new JLabel("Title :");
        adminComponents.add(titleLabel);
        titleField = new JTextField();
        adminComponents.add(titleField);
        JLabel authorLabel = new JLabel("Author :");
        adminComponents.add(authorLabel);
        authorField = new JTextField();
        adminComponents.add(authorField);
        JLabel topicLabel = new JLabel("Topic:");
        adminComponents.add(topicLabel);
        topicField = new JTextField();
        adminComponents.add(topicField);
        JLabel yearLabel = new JLabel("Year:");
        adminComponents.add(yearLabel);
        yearField = new JTextField();
        adminComponents.add(yearField);
        JLabel countLabel = new JLabel("Count of copies:");
        adminComponents.add(countLabel);
        countField = new JTextField();
        adminComponents.add(countField);
        JLabel countOfReservedLabel = new JLabel("Count of reserved copies");
        adminComponents.add(countOfReservedLabel);
        reservedBookCountField = new JTextField();
        reservedBookCountField.setEnabled(false);
        adminComponents.add(reservedBookCountField);
        fillAdminPanel(adminComponents, adminPanel);
        return adminPanel;
    }

    private void fillAdminPanel(List<JComponent> components, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        for (int i = 0; i < components.size(); i++) {
            panel.add(components.get(i), c);
            if (i % 2 == 0) {
                c.gridx = 1;
            } else {
                c.gridy += 1;
                c.gridx = 0;
            }
        }
    }


    public JPanel getAdminPanel() {
        return adminPanel;
    }


    private JPanel createButtonPanel() {
        createButton = new JButton(CREATE_BUTTON_NAME);
        createButton.addActionListener(new CreateButtonListener());
        updateButton = new JButton(UPDATE_BUTTON_NAME);
        updateButton.addActionListener(new UpdateButtonActionListener());
        deleteButton = new JButton(DELETE_BUTTON_NAME);
        deleteButton.addActionListener(new DeleteBookListener());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        return buttonPanel;
    }

    private class CreateButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Book book = new Book();
            book.setIsbn(isbnField.getText());
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setTopic(topicField.getText());
            try {
                book.setYear(Integer.parseInt(yearField.getText()));
                book.setCount(Integer.parseInt(countField.getText()));
            } catch (IllegalArgumentException iae) {
                throw new RuntimeException(iae.getMessage(), iae);
            }
            try {
                bookProvider.createBook(book);
            } catch (LibraryProviderException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
        }
    }

    private class UpdateButtonActionListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Book newBook = new Book();
            newBook.setId(tableBook.getId());
            newBook.setIsbn(isbnField.getText());
            newBook.setTitle(titleField.getText());
            newBook.setAuthor(authorField.getText());
            newBook.setTopic(topicField.getText());
            newBook.setYear(Integer.parseInt(yearField.getText()));
            newBook.setCount(Integer.parseInt(countField.getText()));
            try {
                bookProvider.updateBook(tableBook, newBook);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }

    private class DeleteBookListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                bookProvider.deleteBooks(tableBook, Integer.parseInt(countField.getText()));
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
                //JOptionPane.showMessageDialog(adminPanel, ex.getMessage(), "Library client", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}

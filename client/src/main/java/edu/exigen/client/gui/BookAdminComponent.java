package edu.exigen.client.gui;

import edu.exigen.entities.Book;
import edu.exigen.server.ProvidersHolder;

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
    private static final String ISBN = "ISBN:";
    private static final String TITLE = "Title:";
    private static final String AUTHOR = "Author:";
    private static final String TOPIC = "Topic:";
    private static final String YEAR = "Year:";
    private static final String COUNT_OF_COPIES = "Count of copies:";
    private static final String COUNT_OF_RESERVED_COPIES = "Count of reserved copies:";

    private ProvidersHolder providersHolder;
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

    public BookAdminComponent(ProvidersHolder providersHolder) throws RemoteException {
        this.providersHolder = providersHolder;
        searchComponent = new BookSearchComponent(providersHolder);
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
                            (providersHolder.getRecordProvider().getReservedBookCount(selectedBook.getId())) : "");
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
        JLabel isbn = new JLabel(ISBN);
        adminComponents.add(isbn);
        isbnField = new JTextField();
        adminComponents.add(isbnField);
        JLabel titleLabel = new JLabel(TITLE);
        adminComponents.add(titleLabel);
        titleField = new JTextField();
        adminComponents.add(titleField);
        JLabel authorLabel = new JLabel(AUTHOR);
        adminComponents.add(authorLabel);
        authorField = new JTextField();
        adminComponents.add(authorField);
        JLabel topicLabel = new JLabel(TOPIC);
        adminComponents.add(topicLabel);
        topicField = new JTextField();
        adminComponents.add(topicField);
        JLabel yearLabel = new JLabel(YEAR);
        adminComponents.add(yearLabel);
        yearField = new JTextField();
        adminComponents.add(yearField);
        JLabel countLabel = new JLabel(COUNT_OF_COPIES);
        adminComponents.add(countLabel);
        countField = new JTextField();
        adminComponents.add(countField);
        JLabel countOfReservedLabel = new JLabel(COUNT_OF_RESERVED_COPIES);
        adminComponents.add(countOfReservedLabel);
        reservedBookCountField = new JTextField();
        reservedBookCountField.setEditable(false);
        adminComponents.add(reservedBookCountField);
        fillAdminPanel(adminComponents, adminPanel);
        return adminPanel;
    }

    private void fillAdminPanel(List<JComponent> components, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < components.size(); i++) {
            panel.add(components.get(i), c);
            if (i % 2 == 0) {
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                c.gridx = 1;
            } else {
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.WEST;
                c.weightx = 0;
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
        createButton.addActionListener(new CreateBookListener());
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

    private class CreateBookListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Book book = new Book();
            checkISBNField();
            book.setIsbn(isbnField.getText());
            checkTitleField();
            book.setTitle(titleField.getText());
            checkAuthorField();
            book.setAuthor(authorField.getText());
            checkTopicField();
            book.setTopic(topicField.getText());
            String countFieldValue = countField.getText();
            checkCountValue(countFieldValue);
            book.setCount(Integer.parseInt(countFieldValue));
            try {
                book.setYear(Integer.parseInt(yearField.getText()));
            } catch (IllegalArgumentException iae) {
                throw new RuntimeException("Set valid year data");
            }
            try {
                providersHolder.getBookProvider().createBook(book);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
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
            checkSelectedBook();
            newBook.setId(tableBook.getId());
            checkISBNField();
            newBook.setIsbn(isbnField.getText());
            checkTitleField();
            newBook.setTitle(titleField.getText());
            checkAuthorField();
            newBook.setAuthor(authorField.getText());
            checkTopicField();
            newBook.setTopic(topicField.getText());
            try {
                newBook.setYear(Integer.parseInt(yearField.getText()));
            } catch (IllegalArgumentException iae) {
                throw new RuntimeException("Set valid year data");
            }
            checkCountValue(countField.getText());
            newBook.setCount(Integer.parseInt(countField.getText()));
            try {
                providersHolder.getBookProvider().updateBook(tableBook, newBook);
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
            checkSelectedBook();
            checkCountValue(countField.getText());
            try {
                if (tableBook != null) {
                    providersHolder.getBookProvider().deleteBooks(tableBook, Integer.parseInt(countField.getText()));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }

    private void checkISBNField() {
        if ("".equals(isbnField.getText())) {
            throw new RuntimeException("Enter ISBN data");
        }
    }

    private void checkTitleField() {
        if ("".equals(titleField.getText())) {
            throw new RuntimeException("Enter title data");
        }
    }

    private void checkAuthorField() {
        if ("".equals(authorField.getText())) {
            throw new RuntimeException("Enter author data");
        }
    }

    private void checkTopicField() {
        if ("".equals(topicField.getText())) {
            throw new RuntimeException("Enter topic data");
        }
    }

    private void checkCountValue(String countFieldValue) {
        try {
            if (Integer.parseInt(countField.getText()) < 0) {
                throw new RuntimeException("Count must be > 0");
            }
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Set valid count value > 0 ");
        }
    }

    private void checkSelectedBook() {
        if (tableBook == null) {
            throw new RuntimeException("Select book from table");
        }
    }
}

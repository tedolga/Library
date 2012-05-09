package edu.exigen.client.gui;

import edu.exigen.client.entities.Book;
import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.LibraryProviderException;

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
    private BookSearchComponent searchComponent;
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

    public BookAdminComponent(BookProvider bookProvider) {
        this.bookProvider = bookProvider;
        searchComponent = new BookSearchComponent(bookProvider);
        initComponents();
    }

    public void initComponents() {
        adminPanel = new JPanel();
        JPanel searchPanel = searchComponent.getBookSearchPanel();
        JPanel bookAdminPanel = createAdminPanel();
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
        isbnField = new JTextField(20);
        adminComponents.add(isbnField);
        JLabel titleLabel = new JLabel("Title :");
        adminComponents.add(titleLabel);
        titleField = new JTextField(20);
        adminComponents.add(titleField);
        JLabel authorLabel = new JLabel("Author :");
        adminComponents.add(authorLabel);
        authorField = new JTextField(20);
        adminComponents.add(authorField);
        JLabel topicLabel = new JLabel("Topic:");
        adminComponents.add(topicLabel);
        topicField = new JTextField(20);
        adminComponents.add(topicField);
        JLabel yearLabel = new JLabel("Year:");
        adminComponents.add(yearLabel);
        yearField = new JTextField(20);
        adminComponents.add(yearField);
        JLabel countLabel = new JLabel("Count:");
        adminComponents.add(countLabel);
        countField = new JTextField(20);
        adminComponents.add(countField);
        fillAdminPanel(adminComponents, adminPanel);
        return adminPanel;
    }

    private void fillAdminPanel(List<JComponent> components, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.EAST;
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        for (int i = 0; i < components.size(); i++) {
            panel.add(components.get(i), c);
            if (i % 2 == 0) {
                c.fill = GridBagConstraints.WEST;
                c.gridx = 1;

            } else {
                c.fill = GridBagConstraints.EAST;
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
        deleteButton = new JButton(DELETE_BUTTON_NAME);
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
}

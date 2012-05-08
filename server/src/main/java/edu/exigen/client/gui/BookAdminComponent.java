package edu.exigen.client.gui;

import edu.exigen.server.provider.BookProvider;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookAdminComponent {
    private static final String ADMIN_PANEL_NAME = "Book Administration";

    private BookProvider bookProvider;
    private BookSearchComponent searchComponent;
    private JTextField isbnField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField topicField;
    private JTextField yearField;
    private JTextField countField;
    private JPanel adminPanel;

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
    }

    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel();
        adminPanel.setBorder(BorderFactory.createTitledBorder(ADMIN_PANEL_NAME));
        adminPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel isbn = new JLabel("ISBN :");
        c.fill = GridBagConstraints.WEST;
        c.weighty = 1;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        adminPanel.add(isbn, c);
        isbnField = new JTextField(20);
        c.weightx = 1;
        c.gridx = 1;
        adminPanel.add(isbnField, c);
        JLabel titleLabel = new JLabel("Title :");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        adminPanel.add(titleLabel, c);
        titleField = new JTextField(20);
        c.weightx = 1;
        c.gridx = 1;
        adminPanel.add(titleField, c);
        JLabel authorLabel = new JLabel("Author :");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        adminPanel.add(authorLabel, c);
        authorField = new JTextField(20);
        c.weightx = 1;
        c.gridx = 1;
        adminPanel.add(authorField, c);

        JLabel topicLabel = new JLabel("Topic:");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        adminPanel.add(topicLabel, c);
        topicField = new JTextField(20);
        c.weightx = 1;
        c.gridx = 1;
        adminPanel.add(topicField, c);

        JLabel yearLabel = new JLabel("Year:");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 4;
        adminPanel.add(yearLabel, c);
        yearField = new JTextField(20);
        c.weightx = 1;
        c.gridx = 1;
        adminPanel.add(yearField, c);

        JLabel countLabel = new JLabel("Count:");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 5;
        adminPanel.add(countLabel, c);
        countField = new JTextField(20);
        c.weightx = 1;
        c.gridx = 1;
        adminPanel.add(countField, c);
        return adminPanel;
    }

    public JPanel getAdminPanel() {
        return adminPanel;
    }

}

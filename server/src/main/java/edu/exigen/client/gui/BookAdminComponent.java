package edu.exigen.client.gui;

import edu.exigen.server.provider.BookProvider;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        for (int i = 0; i < components.size(); i++) {
            panel.add(components.get(i), c);
            if (i % 2 == 0) {
                c.fill = GridBagConstraints.WEST;
                c.gridx = 1;
                c.weightx = 1;

            } else {
                c.fill = GridBagConstraints.EAST;
                c.gridy += 1;
                c.gridx = 0;
                c.weightx = 0.5;
            }
        }
    }

    public JPanel getAdminPanel() {
        return adminPanel;
    }

}

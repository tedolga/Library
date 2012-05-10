package edu.exigen.client.gui;

import edu.exigen.client.entities.Book;
import edu.exigen.client.entities.Reader;
import edu.exigen.server.provider.ReaderProvider;
import edu.exigen.server.provider.ReservationRecordProvider;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReaderAdminComponent {

    private static final String ADMIN_PANEL_NAME = "Reader Administration";
    private static final String CREATE_BUTTON_NAME = "Create";
    private static final String UPDATE_BUTTON_NAME = "Update";
    private static final String DELETE_BUTTON_NAME = "Delete";

    private static final String FIRST_NAME = "First Name:";
    private static final String LAST_NAME = "Last Name:";
    private static final String ADDRESS = "Address:";
    private static final String DATE_OF_BIRTH = "Date of birth:";
    private static final String CURRENTLY_RESERVED_BOOKS = "Currently reserved books:";

    private ReaderProvider readerProvider;
    private ReaderSearchComponent searchComponent;
    private Reader tableReader;
    private String[] readerBooksInfo = new String[0];
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JXDatePicker dateOfBirthField;
    private JPanel adminPanel;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JList reservedBooksList;
    private ReservationRecordProvider recordProvider;

    public ReaderAdminComponent(ReaderProvider readerProvider, ReservationRecordProvider recordProvider) throws RemoteException {
        this.readerProvider = readerProvider;
        this.recordProvider = recordProvider;
        searchComponent = new ReaderSearchComponent(readerProvider);
        initComponents();
    }

    public void initComponents() {
        adminPanel = new JPanel();
        adminPanel.setName(ADMIN_PANEL_NAME);
        JPanel searchPanel = searchComponent.getReaderSearchPanel();
        JPanel readerAdminPanel = createAdminPanel();
        searchComponent.addReaderSelectionListener(new ReaderSelectionListener() {
            @Override
            public void readerSelected(Reader selectedReader) {
                tableReader = selectedReader;
                firstNameField.setText(selectedReader != null ? selectedReader.getFirstName() : "");
                lastNameField.setText(selectedReader != null ? selectedReader.getLastName() : "");
                addressField.setText(selectedReader != null ? selectedReader.getAddress() : "");
                dateOfBirthField.setDate(selectedReader != null ? selectedReader.getDateOfBirth() : new Date());
                java.util.List<Book> readerBooks;
                try {
                    readerBooks = recordProvider.getReservedReaderBooks(tableReader);
                    readerBooksInfo = new String[readerBooks.size()];
                    for (int i = 0; i < readerBooks.size(); i++) {
                        readerBooksInfo[i] = readerBooks.get(i).getIsbn() + " " + readerBooks.get(i).getTitle();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                    //JOptionPane.showMessageDialog(adminPanel, e.getMessage(), "Library client", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        adminPanel.setLayout(new BorderLayout());
        adminPanel.add(searchPanel, BorderLayout.NORTH);
        adminPanel.add(readerAdminPanel, BorderLayout.CENTER);
        JPanel buttonPanel = createButtonPanel();
        adminPanel.add(buttonPanel, BorderLayout.SOUTH);
        adminPanel.setBorder(BorderFactory.createTitledBorder(ADMIN_PANEL_NAME));
    }

    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridBagLayout());
        java.util.List<JComponent> adminComponents = new ArrayList<JComponent>();
        JLabel firstNameLabel = new JLabel(FIRST_NAME);
        adminComponents.add(firstNameLabel);
        firstNameField = new JTextField();
        adminComponents.add(firstNameField);
        JLabel lastNameLabel = new JLabel(LAST_NAME);
        adminComponents.add(lastNameLabel);
        lastNameField = new JTextField();
        adminComponents.add(lastNameField);
        JLabel addressLabel = new JLabel(ADDRESS);
        adminComponents.add(addressLabel);
        addressField = new JTextField();
        adminComponents.add(addressField);
        JLabel dateOfBirthLabel = new JLabel(DATE_OF_BIRTH);
        adminComponents.add(dateOfBirthLabel);
        dateOfBirthField = new JXDatePicker();
        adminComponents.add(dateOfBirthField);
        JLabel reservedBooksLabel = new JLabel(CURRENTLY_RESERVED_BOOKS);
        adminComponents.add(reservedBooksLabel);
        reservedBooksList = new JList(readerBooksInfo);
        reservedBooksList.setEnabled(false);
        adminComponents.add(reservedBooksList);
        fillAdminPanel(adminComponents, adminPanel);
        return adminPanel;
    }

    private void fillAdminPanel(java.util.List<JComponent> components, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
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
        updateButton.addActionListener(new UpdateButtonListener());
        deleteButton = new JButton(DELETE_BUTTON_NAME);
        deleteButton.addActionListener(new DeleteButtonListener());
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
            Reader reader = new Reader();
            reader.setFirstName(firstNameField.getText());
            reader.setLastName(lastNameField.getText());
            reader.setAddress(addressField.getText());
            reader.setDateOfBirth(dateOfBirthField.getDate());
            try {
                readerProvider.createReader(reader);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
                //JOptionPane.showMessageDialog(adminPanel, ex.getMessage(), "Library client", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Reader newReader = new Reader();
            newReader.setId(tableReader.getId());
            newReader.setFirstName(firstNameField.getText());
            newReader.setLastName(lastNameField.getText());
            newReader.setAddress(addressField.getText());
            try {
                newReader.setDateOfBirth(dateOfBirthField.getDate());
                readerProvider.updateReader(tableReader, newReader);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
                //JOptionPane.showMessageDialog(adminPanel, ex.getMessage(), "Library client", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                readerProvider.deleteReader(tableReader);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
                //JOptionPane.showMessageDialog(adminPanel, ex.getMessage(), "Library client", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}

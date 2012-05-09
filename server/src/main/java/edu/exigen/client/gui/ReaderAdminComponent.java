package edu.exigen.client.gui;

import edu.exigen.LibraryConstraints;
import edu.exigen.client.entities.Reader;
import edu.exigen.server.provider.LibraryProviderException;
import edu.exigen.server.provider.ReaderProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class ReaderAdminComponent {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(LibraryConstraints.LIBRARY_DATE_PATTERN);
    private static final String ADMIN_PANEL_NAME = "Reader Administration";
    private static final String CREATE_BUTTON_NAME = "Create";
    private static final String UPDATE_BUTTON_NAME = "Update";
    private static final String DELETE_BUTTON_NAME = "Delete";

    private ReaderProvider readerProvider;
    private ReaderSearchComponent searchComponent;
    private Reader tableReader;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField dateOfBirthField;
    private JPanel adminPanel;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;

    public ReaderAdminComponent(ReaderProvider readerProvider) throws RemoteException {
        this.readerProvider = readerProvider;
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
                firstNameField.setText(selectedReader != null ? selectedReader.getFirstName() : "");
                lastNameField.setText(selectedReader != null ? selectedReader.getLastName() : "");
                addressField.setText(selectedReader != null ? selectedReader.getAddress() : "");
                dateOfBirthField.setText(selectedReader != null ? dateFormat.format(selectedReader.getDateOfBirth()) : "");
                tableReader = selectedReader;
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
        JLabel firstNameLabel = new JLabel("First Name :");
        adminComponents.add(firstNameLabel);
        firstNameField = new JTextField(20);
        adminComponents.add(firstNameField);
        JLabel lastNameLabel = new JLabel("Last Name :");
        adminComponents.add(lastNameLabel);
        lastNameField = new JTextField(20);
        adminComponents.add(lastNameField);
        JLabel addressLabel = new JLabel("Address:");
        adminComponents.add(addressLabel);
        addressField = new JTextField(20);
        adminComponents.add(addressField);
        JLabel dateOfBirthLabel = new JLabel("Date of birth:");
        adminComponents.add(dateOfBirthLabel);
        dateOfBirthField = new JTextField(20);
        adminComponents.add(dateOfBirthField);
        fillAdminPanel(adminComponents, adminPanel);
        return adminPanel;
    }

    private void fillAdminPanel(java.util.List<JComponent> components, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.EAST;
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
            try {
                reader.setDateOfBirth(dateFormat.parse(dateOfBirthField.getText()));
            } catch (ParseException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            }
            try {
                readerProvider.createReader(reader);
            } catch (LibraryProviderException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
            } catch (RemoteException e1) {
                throw new RuntimeException(e1.getMessage(), e1);
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
                newReader.setDateOfBirth(dateFormat.parse(dateOfBirthField.getText()));
                readerProvider.updateReader(tableReader, newReader);
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
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
            }
        }
    }
}

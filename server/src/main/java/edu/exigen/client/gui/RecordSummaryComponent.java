package edu.exigen.client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class RecordSummaryComponent {
    private JTextField libraryCardField;
    private JTextField isbnField;
    private JTextField issueDateField;
    private JTextField returnDateField;
    private JPanel recordSummaryPanel;

    public RecordSummaryComponent() {
        initializeComponents();
    }

    private void initializeComponents() {
        recordSummaryPanel = new JPanel();
        recordSummaryPanel.setLayout(new GridBagLayout());
        java.util.List<JComponent> panelComponents = new ArrayList<JComponent>();
        JLabel libraryCardLabel = new JLabel("Library card :");
        panelComponents.add(libraryCardLabel);
        libraryCardField = new JTextField();
        panelComponents.add(libraryCardField);
        JLabel isbnLabel = new JLabel("Book ISBN");
        panelComponents.add(isbnLabel);
        isbnField = new JTextField();
        panelComponents.add(isbnField);
        JLabel issueDateLabel = new JLabel("Issue Date");
        panelComponents.add(issueDateLabel);
        issueDateField = new JTextField();
        panelComponents.add(issueDateField);
        JLabel returnDateLabel = new JLabel("Return Date");
        panelComponents.add(returnDateLabel);
        returnDateField = new JTextField();
        panelComponents.add(returnDateField);
        fillSummaryPanel(panelComponents, recordSummaryPanel);
    }

    private void fillSummaryPanel(List<JComponent> components, JPanel panel) {
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

    public JTextField getLibraryCardField() {
        return libraryCardField;
    }

    public JTextField getIsbnField() {
        return isbnField;
    }

    public JTextField getIssueDateField() {
        return issueDateField;
    }

    public JTextField getReturnDateField() {
        return returnDateField;
    }


    public JPanel getRecordSummaryPanel() {
        return recordSummaryPanel;
    }

}
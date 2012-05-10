package edu.exigen.client.gui;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class RecordSummaryComponent {

    private static final String RETURN_DATE = "Return Date:";
    private static final String ISSUE_DATE = "Issue Date:";
    private static final String BOOK_ISBN = "Book ISBN:";
    private static final String LIBRARY_CARD = "Library card:";

    private JTextField libraryCardField;
    private JTextField isbnField;
    private JXDatePicker issueDateField;
    private JXDatePicker returnDateField;
    private JPanel recordSummaryPanel;

    public RecordSummaryComponent() {
        initializeComponents();
    }

    private void initializeComponents() {
        recordSummaryPanel = new JPanel();
        recordSummaryPanel.setLayout(new GridBagLayout());
        java.util.List<JComponent> panelComponents = new ArrayList<JComponent>();
        JLabel libraryCardLabel = new JLabel(LIBRARY_CARD);
        panelComponents.add(libraryCardLabel);
        libraryCardField = new JTextField();
        panelComponents.add(libraryCardField);
        JLabel isbnLabel = new JLabel(BOOK_ISBN);
        panelComponents.add(isbnLabel);
        isbnField = new JTextField();
        panelComponents.add(isbnField);
        JLabel issueDateLabel = new JLabel(ISSUE_DATE);
        panelComponents.add(issueDateLabel);
        issueDateField = new JXDatePicker();
        panelComponents.add(issueDateField);
        JLabel returnDateLabel = new JLabel(RETURN_DATE);
        panelComponents.add(returnDateLabel);
        returnDateField = new JXDatePicker();
        panelComponents.add(returnDateField);
        fillSummaryPanel(panelComponents, recordSummaryPanel);
    }

    private void fillSummaryPanel(List<JComponent> components, JPanel panel) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.EAST;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;

        for (int i = 0; i < components.size(); i++) {
            panel.add(components.get(i), c);
            if (i % 2 == 0) {
                c.gridx = 1;
                c.weightx = 1;
                c.fill = GridBagConstraints.HORIZONTAL;
            } else {
                c.gridy += 1;
                c.gridx = 0;
                c.weightx = 0;
                c.anchor = GridBagConstraints.WEST;
                c.fill = GridBagConstraints.NONE;
            }
        }
    }

    public JTextField getLibraryCardField() {
        return libraryCardField;
    }

    public JTextField getIsbnField() {
        return isbnField;
    }

    public JXDatePicker getIssueDateField() {
        return issueDateField;
    }

    public JXDatePicker getReturnDateField() {
        return returnDateField;
    }

    public JPanel getRecordSummaryPanel() {
        return recordSummaryPanel;
    }
}

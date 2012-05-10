package edu.exigen.client.gui;

import edu.exigen.entities.Book;
import edu.exigen.entities.Reader;
import edu.exigen.server.ProvidersHolder;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookReservationComponent {

    private static final String ADMIN_PANEL_NAME = "Book Reservation";
    private static final String CREATE_BUTTON_NAME = "Reserve Book";

    private Book book;
    private ProvidersHolder providersHolder;
    private Reader reader;
    private JPanel bookSearchPanel;
    private JPanel readerSearchPanel;
    private JPanel recordSummaryPanel;
    private JButton reserveButton;
    private JXDatePicker returnDateField;
    private JPanel reservationPanel;

    public BookReservationComponent(ProvidersHolder providersHolder) throws RemoteException {
        this.providersHolder = providersHolder;
        initComponents();
    }

    public void initComponents() throws RemoteException {
        BookSearchComponent bookSearchComponent = new BookSearchComponent(providersHolder.getBookProvider());
        bookSearchPanel = bookSearchComponent.getBookSearchPanel();
        final ReaderSearchComponent readerSearchComponent = new ReaderSearchComponent(providersHolder.getReaderProvider());
        readerSearchPanel = readerSearchComponent.getReaderSearchPanel();
        final RecordSummaryComponent recordSummaryComponent = new RecordSummaryComponent();
        recordSummaryPanel = recordSummaryComponent.getRecordSummaryPanel();
        recordSummaryComponent.getIssueDateField().setEnabled(false);
        recordSummaryComponent.getIssueDateField().setDate(new Date());
        returnDateField = recordSummaryComponent.getReturnDateField();
        bookSearchComponent.addBookSelectionListener(new BookSelectionListener() {
            @Override
            public void bookSelected(Book selectedBook) {
                book = selectedBook;
                recordSummaryComponent.getIsbnField().setText(selectedBook != null ? selectedBook.getIsbn() : "<Please, select the Book>");
            }
        });
        readerSearchComponent.addReaderSelectionListener(new ReaderSelectionListener() {
            @Override
            public void readerSelected(Reader selectedReader) {
                reader = selectedReader;
                recordSummaryComponent.getLibraryCardField().setText(selectedReader != null ? String.valueOf(selectedReader
                        .getId()) : "<Please, select the Reader>");
            }
        });
        reserveButton = new JButton(CREATE_BUTTON_NAME);
        reserveButton.addActionListener(new ReserveButtonListener());
        reservationPanel = new JPanel();
        reservationPanel.setName(ADMIN_PANEL_NAME);
        reservationPanel.setBorder(BorderFactory.createTitledBorder(ADMIN_PANEL_NAME));
        reservationPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        reservationPanel.add(bookSearchPanel, c);
        c.gridy = 1;
        reservationPanel.add(readerSearchPanel, c);
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        reservationPanel.add(recordSummaryPanel, c);
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        reservationPanel.add(reserveButton, c);
    }

    public JPanel getReservationPanel() {
        return reservationPanel;
    }

    private class ReserveButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Date currentDate = returnDateField.getDate();
                if (reader == null) {
                    throw new IllegalArgumentException("Please, select the reader.");
                }
                if (book == null) {
                    throw new IllegalArgumentException("Please, select the book.");
                }
                if (currentDate == null) {
                    throw new IllegalArgumentException("Please, set return date.");
                }
                if (currentDate.before(new Date())) {
                    throw new IllegalArgumentException("Date of return must be after current date");
                }
                providersHolder.getRecordProvider().createRecord(reader.getId(), book.getId(), returnDateField.getDate());
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }
}

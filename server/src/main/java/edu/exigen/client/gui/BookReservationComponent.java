package edu.exigen.client.gui;

import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.ReaderProvider;
import edu.exigen.server.provider.ReservationRecordProvider;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookReservationComponent {
    private static final String ADMIN_PANEL_NAME = "Book Reservation";
    private static final String CREATE_BUTTON_NAME = "Reserve Book";

    private JPanel bookSearchPanel;
    private JPanel readerSearchPanel;
    private JPanel recordSummaryPanel;
    private BookProvider bookProvider;
    private ReaderProvider readerProvider;
    private ReservationRecordProvider reservationRecordProvider;
    private JPanel reservationPanel;


    public BookReservationComponent(BookProvider bookProvider, ReaderProvider readerProvider, ReservationRecordProvider
            reservationRecordProvider) throws RemoteException {
        this.bookProvider = bookProvider;
        this.readerProvider = readerProvider;
        initComponents();
    }

    public void initComponents() throws RemoteException {
        bookSearchPanel = new BookSearchComponent(bookProvider).getBookSearchPanel();
        readerSearchPanel = new ReaderSearchComponent(readerProvider).getReaderSearchPanel();
        recordSummaryPanel = new RecordSummaryComponent().getRecordSummaryPanel();
        JButton reserveButton = new JButton(CREATE_BUTTON_NAME);
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
        reservationPanel.add(bookSearchPanel, BorderLayout.NORTH);
        reservationPanel.add(readerSearchPanel, BorderLayout.CENTER);
        reservationPanel.add(recordSummaryPanel, BorderLayout.CENTER);
        reservationPanel.add(reserveButton, BorderLayout.SOUTH);
    }

    public JPanel getReservationPanel() {
        return reservationPanel;
    }
}

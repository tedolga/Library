package edu.exigen.client.gui;

import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;
import edu.exigen.server.provider.BookProvider;
import edu.exigen.server.provider.BookProviderImpl;
import edu.exigen.server.provider.ReservationRecordProviderImpl;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class BookSearchComponentFrame extends JFrame {

    private static final int FRAME_WIDHT = 800;
    private static final int FRAME_HEIGHT = 600;
    private JPanel searchPanel;

    public BookSearchComponentFrame(JPanel panel) throws HeadlessException {
        super();
        frameInit(panel);
    }

    public void frameInit(JPanel panel) {
        super.frameInit();
        setSize(FRAME_WIDHT, FRAME_HEIGHT);
        searchPanel = panel;
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BookDAO bookDAO = new BookDAO("book.xml");
                ReaderDAO readerDAO = new ReaderDAO("readers.xml");
                ReservationRecordDAO recordDAO = new ReservationRecordDAO("records.xml");
                try {
                    ReservationRecordProviderImpl recordProvider = new ReservationRecordProviderImpl(bookDAO, readerDAO, recordDAO);
                    BookProvider provider = new BookProviderImpl(bookDAO, recordProvider);
                    BookSearchComponent searchComponent = new BookSearchComponent(provider);
                    final BookSearchComponentFrame testFrame = new BookSearchComponentFrame(searchComponent.getBookSearchPanel());
                    testFrame.setLocationRelativeTo(null);
                    testFrame.setVisible(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

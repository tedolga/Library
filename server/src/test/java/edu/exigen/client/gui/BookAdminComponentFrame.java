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
public class BookAdminComponentFrame extends JFrame {

    private static final int FRAME_WIDHT = 800;
    private static final int FRAME_HEIGHT = 600;
    private JPanel adminPanel;

    public BookAdminComponentFrame(JPanel panel) throws HeadlessException {
        super();
        frameInit(panel);
    }

    public void frameInit(JPanel panel) {
        super.frameInit();
        setSize(FRAME_WIDHT, FRAME_HEIGHT);
        adminPanel = panel;
        setLayout(new BorderLayout());
        add(adminPanel, BorderLayout.NORTH);
    }


    public static void main(String[] args) {
        final BookDAO bookDAO = new BookDAO("book.xml");
        final ReaderDAO readerDAO = new ReaderDAO("readers.xml");
        final ReservationRecordDAO recordDAO = new ReservationRecordDAO("records.xml");
        ReservationRecordProviderImpl recordProvider = null;
        try {
            recordProvider = new ReservationRecordProviderImpl(bookDAO, readerDAO, recordDAO);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        BookProvider provider = null;
        try {
            provider = new BookProviderImpl(bookDAO, recordProvider);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        final BookAdminComponent adminComponent = new BookAdminComponent(provider);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                final BookAdminComponentFrame testFrame = new BookAdminComponentFrame(adminComponent.getAdminPanel());
                testFrame.setVisible(true);
            }
        });

    }
}

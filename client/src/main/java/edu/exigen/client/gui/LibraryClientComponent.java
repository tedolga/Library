package edu.exigen.client.gui;

import edu.exigen.client.LibraryClient;

import javax.swing.*;
import java.rmi.RemoteException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class LibraryClientComponent {

    private static final String LIBRARY_CLIENT_NAME = "Library Client";
    private static final int FRAME_WIDHT = 900;
    private static final int FRAME_HEIGHT = 700;
    private BookAdminComponent bookAdminComponent;
    private ReaderAdminComponent readerAdminComponent;
    private RecordAdminComponent recordAdminComponent;
    private BookReservationComponent bookReservationComponent;
    private JFrame libraryClientFrame;

    public LibraryClientComponent(LibraryClient libraryClient) throws RemoteException {
        bookAdminComponent = new BookAdminComponent(libraryClient.getProvidersHolder());
        readerAdminComponent = new ReaderAdminComponent(libraryClient.getProvidersHolder());
        recordAdminComponent = new RecordAdminComponent(libraryClient.getProvidersHolder());
        bookReservationComponent = new BookReservationComponent(libraryClient.getProvidersHolder());
        libraryClientFrame = new JFrame(LIBRARY_CLIENT_NAME);
        initFrameComponents();
    }

    private void initFrameComponents() {
        JTabbedPane bookAdministrationTab = new JTabbedPane();
        libraryClientFrame.setSize(FRAME_WIDHT, FRAME_HEIGHT);
        JPanel bookReservationPanel = bookReservationComponent.getReservationPanel();
        JPanel bookAdminPanel = bookAdminComponent.getAdminPanel();
        JPanel readerAdminPanel = readerAdminComponent.getAdminPanel();
        JPanel recordAdminPanel = recordAdminComponent.getRecordAdminPanel();
        bookAdministrationTab.addTab(bookReservationPanel.getName(), bookReservationPanel);
        bookAdministrationTab.addTab(bookAdminPanel.getName(), bookAdminPanel);
        bookAdministrationTab.addTab(readerAdminPanel.getName(), readerAdminPanel);
        bookAdministrationTab.addTab(recordAdminPanel.getName(), recordAdminPanel);
        libraryClientFrame.add(bookAdministrationTab);
        libraryClientFrame.setLocationRelativeTo(null);
        libraryClientFrame.setVisible(true);
    }


    public JFrame getLibraryClientFrame() {
        return libraryClientFrame;
    }
}

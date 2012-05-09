package edu.exigen.client.gui;

import edu.exigen.client.LibraryClient;

import javax.swing.*;

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
    private JFrame libraryClientFrame;

    public LibraryClientComponent(LibraryClient libraryClient) {
        bookAdminComponent = new BookAdminComponent(libraryClient.getBookProvider());
        readerAdminComponent = new ReaderAdminComponent(libraryClient.getReaderProvider());
        libraryClientFrame = new JFrame(LIBRARY_CLIENT_NAME);
        initFrameComponents();
    }

    private void initFrameComponents() {
        JTabbedPane bookAdministrationTab = new JTabbedPane();
        libraryClientFrame.setSize(FRAME_WIDHT, FRAME_HEIGHT);
        JPanel bookAdminPanel = bookAdminComponent.getAdminPanel();
        JPanel readerAdminPanel = readerAdminComponent.getAdminPanel();
        bookAdministrationTab.addTab(bookAdminPanel.getName(), bookAdminPanel);
        bookAdministrationTab.addTab(readerAdminPanel.getName(), readerAdminPanel);
        libraryClientFrame.add(bookAdministrationTab);
        libraryClientFrame.setLocationRelativeTo(null);
        libraryClientFrame.setVisible(true);
    }


    public JFrame getLibraryClientFrame() {
        return libraryClientFrame;
    }
}

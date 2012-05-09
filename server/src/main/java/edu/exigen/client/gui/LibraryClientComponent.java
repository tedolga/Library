package edu.exigen.client.gui;

import edu.exigen.client.LibraryClient;

import javax.swing.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class LibraryClientComponent {

    private static final String LIBRARY_CLIENT_NAME = "Library Client";
    private static final int FRAME_WIDHT = 800;
    private static final int FRAME_HEIGHT = 600;
    private BookAdminComponent bookAdminComponent;
    private JFrame libraryClientFrame;

    public LibraryClientComponent(LibraryClient libraryClient) {
        bookAdminComponent = new BookAdminComponent(libraryClient.getBookProvider());
        libraryClientFrame = new JFrame(LIBRARY_CLIENT_NAME);
        initFrameComponents();
    }

    private void initFrameComponents() {
        libraryClientFrame.setSize(FRAME_WIDHT, FRAME_HEIGHT);
        libraryClientFrame.add(bookAdminComponent.getAdminPanel());
        libraryClientFrame.setLocationRelativeTo(null);
        libraryClientFrame.setVisible(true);
    }


    public JFrame getLibraryClientFrame() {
        return libraryClientFrame;
    }
}

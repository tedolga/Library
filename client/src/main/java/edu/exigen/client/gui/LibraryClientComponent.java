package edu.exigen.client.gui;

import edu.exigen.client.LibraryClient;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        bookAdminComponent = new BookAdminComponent(libraryClient.getBookProvider(), libraryClient.getRecordProvider());
        readerAdminComponent = new ReaderAdminComponent(libraryClient.getReaderProvider(), libraryClient.getRecordProvider());
        recordAdminComponent = new RecordAdminComponent(libraryClient.getBookProvider(), libraryClient.getRecordProvider());
        bookReservationComponent = new BookReservationComponent(libraryClient.getBookProvider(), libraryClient.getReaderProvider(),
                libraryClient.getRecordProvider());
        libraryClientFrame = new JFrame(LIBRARY_CLIENT_NAME);
        initFrameComponents();
    }

    private void initFrameComponents() {
        JTabbedPane bookAdministrationTab = new JTabbedPane();
        bookAdministrationTab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                recordAdminComponent.updateTable();
            }
        });
        libraryClientFrame.setSize(FRAME_WIDHT, FRAME_HEIGHT);
        JPanel bookReservationPanel = bookReservationComponent.getReservationPanel();
        JPanel bookAdminPanel = bookAdminComponent.getAdminPanel();
        JPanel readerAdminPanel = readerAdminComponent.getAdminPanel();
        JPanel recordAdminPanel = recordAdminComponent.getRecordAdminPanel();
        bookAdministrationTab.addTab(bookReservationPanel.getName(), bookReservationPanel);
        bookAdministrationTab.addTab(bookAdminPanel.getName(), bookAdminPanel);
        bookAdministrationTab.addTab(readerAdminPanel.getName(), readerAdminPanel);
        bookAdministrationTab.addTab(recordAdminPanel.getName(), recordAdminPanel);
        libraryClientFrame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                recordAdminComponent.updateTable();
            }
        });
        libraryClientFrame.add(bookAdministrationTab);
        libraryClientFrame.setLocationRelativeTo(null);
        libraryClientFrame.setVisible(true);
    }


    public JFrame getLibraryClientFrame() {
        return libraryClientFrame;
    }
}

package edu.exigen.servlet;

import edu.exigen.entities.Book;
import edu.exigen.server.LibraryServer;
import edu.exigen.server.dao.BookDAO;
import edu.exigen.server.dao.LibraryDAOException;
import edu.exigen.server.dao.ReaderDAO;
import edu.exigen.server.dao.ReservationRecordDAO;
import edu.exigen.server.provider.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class HelloServlet extends HttpServlet {
    private static final int SERVER_PORT = 1099;
    private LibraryServer libraryServer = new LibraryServer();

    @Override
    public void init() throws ServletException {

        BookDAO bookDAO = new BookDAO("book.xml");
        ReaderDAO readerDAO = new ReaderDAO("reader.xml");
        ReservationRecordDAO recordDAO = new ReservationRecordDAO("record.xml");
        try {
            ReservationRecordProvider reservationRecordProvider = new ReservationRecordProviderImpl(bookDAO, readerDAO, recordDAO);
            BookProvider bookProvider = new BookProviderImpl(bookDAO, reservationRecordProvider);
            ReaderProvider readerProvider = new ReaderProviderImpl(readerDAO, reservationRecordProvider);
            libraryServer.setBookProvider(bookProvider);
            libraryServer.setReaderProvider(readerProvider);
            libraryServer.setRecordProvider(reservationRecordProvider);
            libraryServer.loadServer();
            //LocateRegistry.createRegistry(SERVER_PORT);
            //libraryServer.registerProviders();
            Book book = new Book();
            book.setIsbn("12345");
            book.setTitle("book");
            book.setAuthor("author");
            book.setTopic("author");
            book.setYear(1988);
            book.setCount(1);
            libraryServer.getBookProvider().createBook(book);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parameter = request.getParameter("parameter");
        if (request.getSession() == null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("parameter", parameter);
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Заголовок</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Book " + libraryServer.getBookProvider().getBookById(Integer.parseInt(parameter)) + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (LibraryDAOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            out.close();
        }
    }
}

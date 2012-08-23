package edu.exigen.servlet;

import edu.exigen.entities.Book;
import edu.exigen.server.LibraryServer;
import edu.exigen.server.dao.LibraryDAOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    private LibraryServer libraryServer;

    @Override
    public void init() throws ServletException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        libraryServer = context.getBean(LibraryServer.class);
        try {
            libraryServer.loadServer();
            Book book = context.getBean(Book.class);
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

package edu.exigen.servlet;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class Hello implements Controller {
    private String helloString;

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        return new ModelAndView("helloServlet", "helloString", helloString);
    }

    public void setHelloString(String helloString) {
        this.helloString = helloString;
    }
}

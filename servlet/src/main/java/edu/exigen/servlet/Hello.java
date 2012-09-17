package edu.exigen.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tedikova O.
 * @version 1.0
 */

@Controller
public class Hello {

    @RequestMapping(value = "/")
    public String home() {
        System.out.println("HomeController: Passing through...");
        return "WEB-INF/jsp/hello.jsp";
    }
}




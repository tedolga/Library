package edu.exigen.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Tedikova O.
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/helloWorld")
public class HelloController {

    @RequestMapping(method = RequestMethod.GET)
    public String helloWorld(Model model) {
        return "Hello";
    }

}

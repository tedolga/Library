package edu.exigen.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Tedikova O.
 * @version 1.0
 */

@Controller
public class Hello {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "hello";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String greet(@RequestParam("personName") String personName, Model model) {
        NameHandler nameHandler = new NameHandler();
        nameHandler.setPersonName(personName);
        model.addAttribute("greeting", nameHandler.getGreet());
        return "hello";
    }

}




package edu.exigen.servlet;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Tedikova O.
 * @version 1.0
 */
@org.springframework.stereotype.Controller
public class Hello {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView helloWorld() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public ModelAndView helloWorld() {
//
//    }

}

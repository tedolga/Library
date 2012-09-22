package edu.exigen.servlet;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class NameHandler {
    private final String helloString = "Hello ";
    private String personName;


    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonName() {
        return personName;
    }

    public String getGreet() {
        return String.format("%s, %s!", helloString, personName);
    }

}

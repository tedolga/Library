package edu.exigen.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author O. Tedikova
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Reader implements Serializable {

    @XmlElement
    private String firstName;
    @XmlElement
    private String lastName;
    @XmlElement
    private String address;
    @XmlElement
    private Date dateOfBirth;

    @XmlElement
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Reader copy() {
        Reader copy = new Reader();
        copy.setId(this.getId());
        copy.setFirstName(firstName);
        copy.setLastName(lastName);
        copy.setAddress(address);
        copy.setDateOfBirth(dateOfBirth);
        return copy;
    }
}

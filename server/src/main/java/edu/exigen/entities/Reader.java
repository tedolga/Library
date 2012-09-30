package edu.exigen.entities;

import javax.persistence.*;
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
@Entity
@Table(name = "READERS")
public class Reader implements Serializable {

    @XmlElement
    @Column(name = "FIRST_NAME")
    private String firstName;
    @XmlElement
    @Column(name = "LAST_NAME")
    private String lastName;
    @XmlElement
    @Column(name = "ADDRESS")
    private String address;
    @XmlElement
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @XmlElement
    @Id
    @GeneratedValue
    @Column(name = "ID")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reader)) return false;

        Reader reader = (Reader) o;

        return id == reader.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}

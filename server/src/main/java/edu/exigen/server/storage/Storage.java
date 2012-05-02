package edu.exigen.server.storage;

import edu.exigen.client.entities.Entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */

@XmlRootElement(name = "storage")
@XmlAccessorType(XmlAccessType.FIELD)
public class Storage<T extends Entity> {

    @XmlElement
    private List<T> books;

    public List<T> getElements() {
        return books;
    }


    public void setElements(List<T> books) {
        this.books = books;
    }
}

package edu.exigen.server.storage;

import edu.exigen.client.entities.Reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */
@XmlRootElement(name = "readerStorage")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReaderStorage {

    @XmlElement(name = "reader")
    private List<Reader> readers;

    public List<Reader> getElements() {
        return readers;
    }

    public void setElements(List<Reader> readers) {
        this.readers = readers;
    }
}

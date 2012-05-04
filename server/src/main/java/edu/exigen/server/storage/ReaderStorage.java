package edu.exigen.server.storage;

import edu.exigen.client.entities.Reader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author O. Tedikova
 * @version 1.0
 */
@XmlRootElement(name = "readerStorage")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReaderStorage {
    private int id;
    @XmlElement(name = "reader")
    private List<Reader> readers = new ArrayList<Reader>();

    public List<Reader> getElements() {
        return readers;
    }

    public void setElements(List<Reader> readers) {
        this.readers = readers;
    }

    public synchronized int incrementAndGet() {
        return ++id;
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }

    public Reader getReader(int id) {
        for (Reader reader : readers) {
            if (reader.getId() == id) {
                return reader;
            }
        }
        return null;
    }

    public boolean removeReader(Reader reader) {
        return readers.remove(reader);
    }
}

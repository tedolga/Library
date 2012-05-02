package edu.exigen.server.storage;

import edu.exigen.client.entities.ReservationRecord;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */

@XmlRootElement(name = "recordStorage")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReservationRecordStorage {

    @XmlElement(name = "reservationRecord")
    private List<ReservationRecord> elements = new ArrayList<ReservationRecord>();

    public List<ReservationRecord> getElements() {
        return elements;
    }

    public void setElements(List<ReservationRecord> elements) {
        this.elements = elements;
    }
}

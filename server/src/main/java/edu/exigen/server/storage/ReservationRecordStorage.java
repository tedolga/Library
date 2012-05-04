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
    private int id;

    @XmlElement(name = "reservationRecord")
    private List<ReservationRecord> records = new ArrayList<ReservationRecord>();

    public List<ReservationRecord> getRecords() {
        return records;
    }

    public void setRecords(List<ReservationRecord> records) {
        this.records = records;
    }

    public synchronized int incrementAndGet() {
        return ++id;
    }

    public void addRecord(ReservationRecord record) {
        records.add(record);
    }

    public ReservationRecord getRecord(int id) {
        for (ReservationRecord record : records) {
            if (record.getId() == id) {
                return record;
            }
        }
        return null;
    }

    public boolean removeRecord(ReservationRecord oldRecord) {
        return records.remove(oldRecord);
    }
}

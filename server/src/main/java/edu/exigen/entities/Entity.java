package edu.exigen.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Represents basic library entity.
 *
 * @author O. Tedikova
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Entity implements Serializable {

    @XmlElement
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(this.getClass().isInstance(o))) return false;

        Entity entity = (Entity) o;

        if (id != entity.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

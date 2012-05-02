package edu.exigen.client.entities;

/**
 * Represents basic library entity.
 *
 * @author O. Tedikova
 * @version 1.0
 */
public class Entity {
    private final int id;

    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

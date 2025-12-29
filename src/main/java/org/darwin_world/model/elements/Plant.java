package org.darwin_world.model.elements;

import org.darwin_world.model.util.Vector2D;

public class Plant {
    private Vector2D position;

    public Plant(Vector2D position){
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }
}

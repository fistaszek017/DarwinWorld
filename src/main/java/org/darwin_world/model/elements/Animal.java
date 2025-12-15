package org.darwin_world.model.elements;

import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.util.Vector2D;

public class Animal {
    private MapDirection facingDirection;
    private Vector2D mapPosition;

    public Animal(MapDirection facingDirection, Vector2D mapPosition) {
        this.facingDirection = facingDirection;
        this.mapPosition = mapPosition;
    }

    public Vector2D getMapPosition() {
        return mapPosition;
    }

    public MapDirection getFacingDirection() {
        return facingDirection;
    }

    public void move(MapDirection direction) {
        //TODO
    }
}

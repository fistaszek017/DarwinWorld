package org.darwin_world.model.elements;

import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.util.Vector2D;

public class Animal {
    private MapDirection facingDirection;
    private Vector2D mapPosition;
    private int energy;
    private final int GENES_NUMBER = 8;

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

    public void move(WorldMap map) {
        //TODO
        // jak ja to widze - przekazujesz zamiar do handleMove z GlobeMap, to ci zwraca pozycje na ktora zmieniasz i potem wywolujesz move z mapy

    }



    public boolean isDead() {
        if (energy <= 0){
            return false;
        }
    }
}

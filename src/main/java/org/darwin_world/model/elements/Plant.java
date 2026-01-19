package org.darwin_world.model.elements;

import org.darwin_world.model.util.Vector2D;

public class Plant {
    private final Vector2D position;
    private int uses;

    public Plant(Vector2D position, int uses){
        this.position = position;
        this.uses = uses;
    }

    public Vector2D getPosition() {
        return position;
    }

    public boolean eat() {
        uses--;
        return uses > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return position.equals(plant.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

}

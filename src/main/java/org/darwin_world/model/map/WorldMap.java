package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.util.Vector2D;

import java.util.Map;

public interface WorldMap {

    /**
     * @throws IncorrectPositionException If the position is out of bounds of the map.
     * @param position The position to place the object at.
     * @param object The object to be placed on the map.
     */
    void place(Object object, Vector2D position) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, Vector2D oldPosition, Vector2D newPosition) throws IncorrectPositionException;

    /**
     * Return true if given position on the map is occupied.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2D position);

    /**
     * Returns true if given position is out of bounds of the map.
     * @param position Position to check.
     * @return true if position is out of map bounds, else false
     */
    boolean isOutOfBounds(Vector2D position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    Object objectAt(Vector2D position);

    /**
     * Returns all map elements in a new map (key=position, value=element).
     *
     * @return HashMap with elements.
     */
    Map<Vector2D, Object> getAnimals();
}

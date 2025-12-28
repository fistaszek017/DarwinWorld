package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.util.Vector2D;

import java.util.List;
import java.util.Map;

public interface WorldMap {

    /**
     * @throws IncorrectPositionException If the position is out of bounds of the map.
     * @param position The position to placeAnimal the animal at.
     * @param animal The animal to be placed on the map.
     */
    void placeAnimal(Animal animal, Vector2D position) throws IncorrectPositionException;

    /**
     * @throws IncorrectPositionException If the position is out of bounds of the map.
     * @param position The position to placeAnimal the plant at.
     * @param plant The plant to be placed on the map.
     */
    void placePlant(Object plant, Vector2D position) throws IncorrectPositionException;

    /**
     * Removes an animal from the map
     * @param animal animal to be removed
     * @param position position the animal is at
     * @throws IncorrectPositionException If the animal is not at the specified position
     */
    void removeAnimal(Animal animal, Vector2D position) throws IncorrectPositionException;


    /**
     * Removes a plant from the map
     * @param position position the plant is at
     * @throws IncorrectPositionException if the plant is not at the specified position
     */
    void removePlant(Vector2D position) throws IncorrectPositionException;

    /**
     * Handles moving the animals around with accounting for the globe simulation.
     * @param position Current animal position.
     * @param direction Current animal move direction.
     * @return MoveResult object containing the new position and orientation.
     */
    MoveResult handleMove(Vector2D position, MapDirection direction);


    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method throws an exception.
     * @param animal animal to move.
     * @param oldPosition current position of the animal
     * @param newPosition the position to move the animal to
     * @throws IncorrectPositionException when
     */
    void move(Animal animal, Vector2D oldPosition, Vector2D newPosition) throws IncorrectPositionException;

    /**
     * Returns true if given position is out of bounds of the map.
     * @param position Position to check.
     * @return true if position is out of map bounds, else false
     */
    boolean isInBounds(Vector2D position);

    /**
     * Returns true if there is a plant at the specified position
     * @param position position to be checked
     * @return  true if plant present
     */
    boolean plantPresent(Vector2D position);

    /**
     * Returns all animals on a specified position, if there are none it returns an empty list
     * @param position position to be checked
     * @return list with all the animals on that position
     */
    List<Animal> getAnimalsAt(Vector2D position);


    /**
     * Returns all animals in a list for easy handling.
     * @return List with all the animals.
     */
    List<Animal> getAllAnimals();

    /**
     * Return a copy of the map with all the animals in it
     * @return Map with all animals.
     */
    Map<Vector2D, List<Animal>> getAnimals();

}

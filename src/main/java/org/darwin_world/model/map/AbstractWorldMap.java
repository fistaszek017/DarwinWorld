package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.elements.Plant;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    protected final Boundary BOUNDARY;
    protected final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2D, Plant> plants = new HashMap<>();


    public AbstractWorldMap(SimulationConfig config) {
        this.BOUNDARY = new Boundary(new Vector2D(0,0), new Vector2D(config.mapWidth()-1,config.mapHeight()-1), config.mapWidth(), config.mapHeight());
    }

    @Override
    public boolean isInBounds(Vector2D position) {
        return position.follows(BOUNDARY.bottomLeft()) && position.precedes(BOUNDARY.topRight());
    }

    @Override
    public void placeAnimal(Animal animal, Vector2D position) throws IncorrectPositionException {
        if (isInBounds(position)) {
            List<Animal> objectsAtPosition = animals.computeIfAbsent(position, _ -> new ArrayList<>());
            objectsAtPosition.add(animal);
            return;
        }
        throw new IncorrectPositionException(position);
    }

    @Override
    public void removeAnimal(Animal animal, Vector2D position) throws IncorrectPositionException {
        if (isInBounds(position) && animals.containsKey(position)) {
            List<Animal> animalsList = animals.get(position);
            if (animalsList.contains(animal)) {
                animalsList.remove(animal);

                if (animalsList.isEmpty()) {
                    animals.remove(position);
                }
                return;
            }
        }
        throw new IncorrectPositionException(position);
    }

    @Override
    public void placePlant(Plant plant, Vector2D position) throws IncorrectPositionException {
        if (isInBounds(position)) {
            if (!plants.containsKey(position)) {
                plants.put(position, plant);
                return;
            }
        }
        throw new IncorrectPositionException(position);
    }

    @Override
    public void removePlant(Vector2D position) throws IncorrectPositionException {
        if (isInBounds(position) && plants.containsKey(position)) {
            plants.remove(position);
            return;
        }
        throw new IncorrectPositionException(position);
    }


    @Override
    public boolean plantPresent(Vector2D position) {
        return plants.containsKey(position);
    }


    @Override
    public void move(Animal animal, Vector2D oldPosition, Vector2D newPosition) throws IncorrectPositionException {
        List<Animal> objectsAtPosition = animals.get(oldPosition);

        if (objectsAtPosition != null && objectsAtPosition.contains(animal) && isInBounds(newPosition)) {
            objectsAtPosition.remove(animal);
            if (objectsAtPosition.isEmpty()) {
                animals.remove(oldPosition);
            }
        }
        else {
            throw new IncorrectPositionException(newPosition);
        }

        placeAnimal(animal, newPosition);
    }

    @Override
    public Map<Vector2D, List<Animal>> getAnimalsMap() {
        return new HashMap<>(animals);
    }

    @Override
    public Map<Vector2D, Plant> getPlants() {
        return new HashMap<>(plants);
    }

    @Override
    public int getOccupiedFieldsCount() {
        int numberOfFields = animals.size();
        for (Map.Entry<Vector2D, Plant> entry : plants.entrySet()) {
            if (!animals.containsKey(entry.getKey())) {
                numberOfFields++;
            }
        }
        return numberOfFields;
    }

    @Override
    public MoveResult handleMove(Vector2D position, MapDirection direction) {

        Vector2D moveVector = direction.getVector();
        Vector2D intendedPosition = position.add(moveVector);

        if (intendedPosition.y() >= BOUNDARY.height() || intendedPosition.y() < 0) {
            int newX = (BOUNDARY.width() - 1) - position.x();
            int newY = position.y();

            MapDirection newDirection = direction.rotate(4);

            return new MoveResult(new Vector2D(newX, newY), newDirection);
        }

        int correctedX = ((intendedPosition.x() % BOUNDARY.width()) + BOUNDARY.width()) % BOUNDARY.width();
        Vector2D finalPosition = new Vector2D(correctedX, intendedPosition.y());
        return new MoveResult(finalPosition, direction);
    }
}

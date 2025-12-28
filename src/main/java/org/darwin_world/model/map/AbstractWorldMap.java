package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.util.Vector2D;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    protected final Boundary BOUNDARY;
    protected final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2D, Object> plants = new HashMap<>();

    public AbstractWorldMap(int width, int height) {
        this.BOUNDARY = new Boundary(new Vector2D(0,0), new Vector2D(width-1,height-1), width, height);
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
            objectsAtPosition.sort(Comparator.comparing(Animal::getEnergy).reversed());
            return;
        }
        throw new IncorrectPositionException(position);
    }

    @Override
    public void removeAnimal(Animal animal, Vector2D position) throws IncorrectPositionException {
        if (isInBounds(position) && animals.containsKey(position) && animals.get(position).contains(animal)) {
            animals.get(position).remove(animal);
            return;
        }
        throw new IncorrectPositionException(position);
    }

    @Override
    public void placePlant(Object plant, Vector2D position) throws IncorrectPositionException {
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
    public List<Animal> getAnimalsAt(Vector2D position) {
        return animals.computeIfAbsent(position, _ -> new ArrayList<>());
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

    public List<Animal> getAllAnimals() {
        return animals.values().stream().flatMap(List::stream).toList();
    }

    @Override
    public Map<Vector2D, List<Animal>> getAnimalsMap() {
        return new HashMap<>(animals);
    }
}

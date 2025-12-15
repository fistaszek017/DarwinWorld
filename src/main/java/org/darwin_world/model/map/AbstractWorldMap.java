package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.util.Vector2D;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    protected final Boundary BOUNDARY;
    protected final Map<Vector2D, List<Object>> animals = new HashMap<>();

    public AbstractWorldMap(int width, int height) {
        this.BOUNDARY = new Boundary(new Vector2D(0,0), new Vector2D(width,height));
    }

    @Override
    public boolean isOutOfBounds(Vector2D position) {
        return position.precedes(BOUNDARY.bottomLeft()) || position.follows(BOUNDARY.topRight());
    }

    @Override
    public void place(Object object, Vector2D position) throws IncorrectPositionException {
        if (!isOutOfBounds(position)) {
            List<Object> objectsAtPosition = animals.computeIfAbsent(position, _ -> new ArrayList<>());
            objectsAtPosition.add(object);
            return;
        }
        throw new IncorrectPositionException(position);
    }

    @Override
    public Object objectAt(Vector2D position) {
        return animals.get(position);
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return animals.containsKey(position);
    }

    @Override
    public void move(Animal animal, Vector2D odlPosition, Vector2D newPosition) throws IncorrectPositionException {
        List<Object> objectsAtPosition = animals.get(newPosition);
        objectsAtPosition.remove(animal);

        if (objectsAtPosition.isEmpty()) {
            animals.remove(newPosition);
        }

        place(animal, newPosition);
    }

    @Override
    public Map<Vector2D, Object> getAnimals() {
        return new HashMap<>(animals);
    }

}

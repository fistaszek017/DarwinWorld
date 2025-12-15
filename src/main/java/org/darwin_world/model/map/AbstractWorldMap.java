package org.darwin_world.model.map;

import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.util.Vector2D;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {

    private final Boundary BOUNDARY;
    protected final Map<Vector2D, Object> map = new HashMap<>();

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
            map.put(position, object);
            return;
        }
        throw new IncorrectPositionException(position);
    }

    public Object objectAt(Vector2D position) {
        return map.get(position);
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return map.containsKey(position);
    }

    @Override
    public void move(Object object, int direction) {
        //TODO after animal implemented
    }

    @Override
    public Map<Vector2D, Object> getMap() {
        return new HashMap<>(map);
    }

}

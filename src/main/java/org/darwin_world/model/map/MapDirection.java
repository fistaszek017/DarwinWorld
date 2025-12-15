package org.darwin_world.model.map;

import org.darwin_world.model.util.Vector2D;

public enum MapDirection {
    NORTH(new Vector2D(0, 1), 0),
    NORTHEAST(new Vector2D(1, 1), 1),
    EAST(new Vector2D(1, 0), 2),
    SOUTHEAST(new Vector2D(1, -1), 3),
    SOUTH(new Vector2D(0, -1), 4),
    SOUTHWEST(new Vector2D(-1, -1), 5),
    WEST(new Vector2D(-1, 0), 6),
    NORTHWEST(new Vector2D(-1, 1), 7);

    private final Vector2D vector;
    private final int directionIndex;

    private static final MapDirection[] valuesById = new MapDirection[8];

    static {
        for (MapDirection direction : MapDirection.values()) {
            valuesById[direction.directionIndex] = direction;
        }
    }

    MapDirection(Vector2D vector, int directionIndex) {
        this.vector = vector;
        this.directionIndex = directionIndex;
    }

    public MapDirection rotate(int steps) {
        int nextIndex = (directionIndex + steps) % 8;
        return valuesById[nextIndex];
    }

    public Vector2D getVector() {
        return vector;
    }
}

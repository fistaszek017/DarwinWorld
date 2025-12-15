package org.darwin_world.model.util;

public record Vector2D(int x, int y) {

    public Vector2D add(Vector2D vector) {
        return new Vector2D(x + vector.x(), y + vector.y());
    }

    public Vector2D sub(Vector2D vector) {
        return new Vector2D(x - vector.x(), y - vector.y());
    }

    public Vector2D opposite() {
        return new Vector2D(-x, -y);
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2D(int x1, int y1)))
            return false;
        return this.x == x1 && this.y == y1;
    }

}

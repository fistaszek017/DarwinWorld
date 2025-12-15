package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.util.Vector2D;

public class GlobeMap extends AbstractWorldMap{

    public GlobeMap(int width, int height) {
        super(width, height);
    }

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

package org.darwin_world.model.map;

import org.darwin_world.model.elements.Plant;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.util.Vector2D;

import java.util.Random;

public class GlobeMap extends AbstractWorldMap{

    private static final double JUNGLE_PLANT_SPAWN_CHANCE = 0.8;
    private final int jungleBottom;
    private final int jungleTop;

    private final Boundary jungleBoundary;

    public GlobeMap(int width, int height) {
        super(width, height);

        int jungleHeight = (int) (height * 0.2);

        int normalRows = height - jungleHeight;
        this.jungleBottom = normalRows / 2;
        this.jungleTop = jungleBottom + jungleHeight;
        this.jungleBoundary = new Boundary(new Vector2D(0, jungleBottom), new Vector2D(super.BOUNDARY.width() -1, jungleTop -1), -1, -1);   //width height placeholder
    }

    @Override
    public void growPlants(int count) {
        Random rand = new Random();

        for (int i = 0; i<count; i++) {

            boolean planted = false;
            int attempts = 0;

            while (!planted && attempts < 10) {
                attempts++;
                boolean jungleSelected = rand.nextDouble() < JUNGLE_PLANT_SPAWN_CHANCE;

                int x = rand.nextInt(super.BOUNDARY.width());
                int y;
                if (jungleSelected) {
                    y = rand.nextInt(jungleBottom, jungleTop);
                }
                else {
                    if (rand.nextBoolean()){
                        y = rand.nextInt(jungleBottom);
                    }
                    else {
                        y = rand.nextInt(jungleTop, super.BOUNDARY.height());
                    }
                }

                Vector2D position = new Vector2D(x, y);

                if (!super.plantPresent(position)) {
                    try {
                        super.placePlant(new Plant(position), position);
                        planted = true;
                    } catch (IncorrectPositionException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
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

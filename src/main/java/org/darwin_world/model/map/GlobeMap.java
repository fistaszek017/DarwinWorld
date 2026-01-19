package org.darwin_world.model.map;

import org.darwin_world.model.elements.Plant;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;

import java.util.Random;

public class GlobeMap extends AbstractWorldMap{

    private static final double JUNGLE_PLANT_SPAWN_CHANCE = 0.8;
    private final int jungleBottom;
    private final int jungleTop;

    public GlobeMap(SimulationConfig config) {
        super(config);

        int jungleHeight = (int) (config.mapHeight() * 0.2);

        int normalRows = config.mapHeight() - jungleHeight;
        this.jungleBottom = normalRows / 2;
        this.jungleTop = jungleBottom + jungleHeight;
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
                        super.placePlant(new Plant(position,1), position);
                        planted = true;
                    } catch (IncorrectPositionException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}

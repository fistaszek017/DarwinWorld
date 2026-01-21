package org.darwin_world.model.map;

import org.darwin_world.model.elements.Plant;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlobeMap extends AbstractWorldMap{

    private static final double JUNGLE_PLANT_SPAWN_CHANCE = 0.8;
    private final int jungleBottom;
    private final int jungleTop;
    private final SimulationConfig config;

    public GlobeMap(SimulationConfig config) {
        super(config);

        this.config = config;

        int jungleHeight = (int) (config.mapHeight() * 0.2);

        int normalRows = config.mapHeight() - jungleHeight;
        this.jungleBottom = normalRows / 2;
        this.jungleTop = jungleBottom + jungleHeight;
    }

    @Override
    public void growPlants(int count) {
        List<Vector2D> jungleFields = new ArrayList<>();
        List<Vector2D> standardFields = new ArrayList<>();

        for (int x = 0; x < config.mapWidth(); x++) {
            for (int y = 0; y < config.mapHeight(); y++) {
                Vector2D pos = new Vector2D(x, y);
                if (plantPresent(pos)) continue;

                if (y >= jungleBottom && y < jungleTop) {
                    jungleFields.add(pos);
                } else {
                    standardFields.add(pos);
                }
            }
        }

        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            if (jungleFields.isEmpty() && standardFields.isEmpty()) break;

            boolean chooseJungle = rand.nextDouble() < JUNGLE_PLANT_SPAWN_CHANCE;

            List<Vector2D> targetList;

            if (chooseJungle) {
                if (!jungleFields.isEmpty()) targetList = jungleFields;
                else targetList = standardFields;
            } else {
                if (!standardFields.isEmpty()) targetList = standardFields;
                else targetList = jungleFields;
            }

            Vector2D position = targetList.remove(rand.nextInt(targetList.size()));
            Plant newPlant = new Plant(position, 1);

            try {
                super.placePlant(newPlant, position);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

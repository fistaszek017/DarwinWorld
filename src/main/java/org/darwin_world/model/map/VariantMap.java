package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.elements.Plant;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;

import java.util.*;

public class VariantMap extends AbstractWorldMap{

    private static final double JUNGLE_PLANT_SPAWN_CHANCE = 0.8;
    private final SimulationConfig config;

    private final Map<Vector2D, Integer> stepsCounter = new HashMap<>();
    private final Map<Vector2D, Integer> jungleCellsDuration = new HashMap<>();

    public VariantMap(SimulationConfig config) {
        super(config);

        this.config = config;
    }

    @Override
    public void placeAnimal(Animal animal, Vector2D position) throws IncorrectPositionException {
        super.placeAnimal(animal, position);

        if (animal.getEnergy() >= config.cultivationEnergyThreshold()) {
            int steps = stepsCounter.getOrDefault(position, 0) + 1;

            if (steps >= config.fertileSoilThreshold()) {
                stepsCounter.remove(position);
                jungleCellsDuration.put(position, config.fertileSoilDuration());
            }
            else  {
                stepsCounter.put(position, steps);
            }
        }
    }


    private void updateFertility(){
        jungleCellsDuration.replaceAll((_, days) -> days - 1);
        jungleCellsDuration.values().removeIf(days -> days <= 0);
    }


    @Override
    public void growPlants(int count) {
        updateFertility();
        List<Vector2D> jungleFields = new ArrayList<>();
        List<Vector2D> standardFields = new ArrayList<>();

        for (int x = 0; x < config.mapWidth(); x++) {
            for (int y = 0; y < config.mapHeight(); y++) {
                Vector2D pos = new Vector2D(x, y);

                if (plantPresent(pos)) continue;

                if (jungleCellsDuration.containsKey(pos)) {
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

            if (targetList.isEmpty()) continue;

            Vector2D position = targetList.remove(rand.nextInt(targetList.size()));

            boolean isFertile = jungleCellsDuration.containsKey(position);

            int plantLives = isFertile ? config.largePlantCapacity() : 1;

            Plant newPlant = new Plant(position, plantLives);

            try {
                super.placePlant(newPlant, position);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }




}

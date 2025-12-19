package org.darwin_world.model.simulation;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.util.Vector2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Simulation {

    private int dayCount = 0;
    private final WorldMap map;
    private final SimulationConfig config;
    private Map<Vector2D, List<Animal>> animals = new HashMap<>();

    public Simulation(WorldMap map, SimulationConfig config) {
        this.map = map;
        this.config = config;

        initializeSimulation();
    }



    private void initializeSimulation() {
        Random rand = new Random();

        //animals
        for (int i = 0; i < config.initialAnimalCount(); i++) {
            int x = rand.nextInt(config.mapWidth());
            int y = rand.nextInt(config.mapHeight());

            Vector2D position = new Vector2D(x, y);
            MapDirection direction = MapDirection.values()[rand.nextInt(8)];

            Animal animal = new Animal(direction, position) //TODO modify when animal fully implemented

            try {
                map.placeAnimal(animal, position);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }

        //TODO  plants

    }

    private int[] generateRandomGenes()
    {
        int[] geneTable = new int[config.genomeLength()];
        Random rand = new Random();
        for (int i = 0; i < config.genomeLength(); i++) {
            geneTable[i] = rand.nextInt(8);
        }

        return geneTable;
    }

    private void growPlants()
    {

    }

    private void simulateDay() {
        animals = map.getAnimals();
        dayCount++;

        growPlants();

        for (Map.Entry<Vector2D, List<Animal>> entry : animals.entrySet()) {
            Vector2D position = entry.getKey();
            List<Animal> animals = entry.getValue();

            //jedzenie
            if (map.plantPresent(position)) {
                int animalCount = animals.size();
                double nutritionalValuePerAnimal = (double) config.plantNutritionalValue() / animalCount;

                for (Animal animal : animals) {
                    animal.eat(nutritionalValuePerAnimal);
                }
            }

            //reprodukcja
            if (animals.size() > 1) {
                
            }


        }
        
    }

}

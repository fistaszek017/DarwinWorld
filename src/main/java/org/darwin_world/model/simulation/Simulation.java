package org.darwin_world.model.simulation;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.util.Vector2D;

import java.util.*;

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

            Animal animal = new Animal(direction, position, generateRandomGenes(), SimulationConfig); //TODO modify when animal fully implemented
            // jesli zostawiamy logike ciagu symulacji po stronie zwierzeta to raczej zaminilbym te
            // kilka parametrow na 1 rekord albo podal to samo co do symulacji (SimulationConfig)

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
        //
    }


    private void reproduceAnimals(Animal first, Animal second)  // zakładam że zawsze first ma >= energie od second
    {
        int totalEnergy = first.getEnergy() + second.getEnergy();

        int firstShare = (int) ((double) first.getEnergy() / totalEnergy * config.genomeLength());
        int secondShare = config.genomeLength() - firstShare;

        int[] result = new int[config.genomeLength()];

        Random rand = new Random();

        if (rand.nextBoolean()) {
            System.arraycopy(first.getGenes(), 0, result, 0, firstShare);
            System.arraycopy(second.getGenes(), config.genomeLength() - secondShare, result, firstShare, secondShare);
        }
        else {
            System.arraycopy(second.getGenes(), 0, result, 0, secondShare);
            System.arraycopy(first.getGenes(), config.genomeLength() - firstShare, result, secondShare, firstShare);
        }

        map.placeAnimal(new Animal(MapDirection.values()[rand.nextInt(8)], first.getMapPosition(), result, SimulationConfig);
    }

    private void simulateDay() {
        animals = map.getAnimals();
        dayCount++;

        growPlants();

        for (Map.Entry<Vector2D, List<Animal>> entry : animals.entrySet()) {
            try {
                Vector2D position = entry.getKey();
                List<Animal> animals = entry.getValue();
                Animal strongest = animals.getFirst();

                //usun martwych

                for (Animal animal : animals) {
                    if (animal.isDead()) {
                        map.removeAnimal(animal, position);
                    }
                }

                //jedzenie
                if (map.plantPresent(position)) {
                    strongest.eat(config.plantNutritionalValue());
                    map.removePlant(position);
                }

                //reprodukcja
                if (animals.size() > 1) {
                    for (int i = 0; i < animals.size() - 1; i += 2) {
                        Animal first = animals.get(i);
                        Animal second = animals.get(i + 1);

                        if (first.readyToReproduce() && second.readyToReproduce()) {
                            reproduceAnimals(first, second);
                        }
                        else break;

                    }
                }

            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }

}

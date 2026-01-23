package org.darwin_world.model.simulation;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.util.StatsFileManager;
import org.darwin_world.model.util.Vector2D;

import java.util.*;

public class Simulation {

    private int dayCount = 0;
    private final WorldMap map;
    private final SimulationConfig config;
    private final List<Animal> animalsList = new ArrayList<>();  //contains all animals
    private Map<Vector2D, List<Animal>> animalMap = new HashMap<>();

    //stats
    private final int cellCount;

    //

    private final GeneUtils geneUtils;
    private final StatsManager statsManager;

    public Simulation(WorldMap map, SimulationConfig config, int id) {
        this.map = map;
        this.config = config;
        this.geneUtils = new GeneUtils(config);
        this.statsManager = new StatsManager(id);

        this.cellCount = config.mapWidth() * config.mapHeight();

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

            int[] genes = geneUtils.generateRandomGenes();

            Animal animal = new Animal(direction, position, config.initialAnimalEnergyValue(), genes, config);

            try {
                map.placeAnimal(animal, position);
                animalsList.add(animal);
                statsManager.registerBirth(genes);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }

        map.growPlants(config.initialPlantCount());
        statsManager.updateAndSaveStats(dayCount, animalsList, map, cellCount);
    }

    private void reproduceAnimals(Animal first, Animal second)  { // assuming the first animal is the stronger one

        int[] childGenes = geneUtils.createChildGenotype(first, second);
        int startEnergy = config.animalReproductionCost() * 2;

        Random rand = new Random();

        Animal child = new Animal(MapDirection.values()[rand.nextInt(8)], first.getMapPosition(), startEnergy, childGenes, config);

        try {
            map.placeAnimal(child, child.getMapPosition());
            animalsList.add(child);
            statsManager.registerBirth(childGenes);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }

        first.removeEnergy(config.animalReproductionCost());
        first.addChild();
        second.removeEnergy(config.animalReproductionCost());
        second.addChild();
    }

    private void removeAnimal(Animal animal) {
        try {
            map.removeAnimal(animal, animal.getMapPosition());
            statsManager.registerDeath(animal);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeDeadAnimals() {
        Iterator<Animal> iterator = animalsList.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.isDead()) {
                removeAnimal(animal);
                iterator.remove();
            }
        }
    }

    private void moveAnimals() {
        for (Animal animal : animalsList) {
            animal.move(map);
        }
    }

    private void eatPlants() {
        for (Map.Entry<Vector2D, List<Animal>> entry : animalMap.entrySet()) {
            Vector2D position = entry.getKey();
            List<Animal> animalsAtPosition = entry.getValue();

            if (animalsAtPosition.size() > 1) {
                animalsAtPosition.sort(Comparator.comparingInt(Animal::getEnergy)
                        .thenComparingInt(Animal::getAge).thenComparingInt(Animal::getChildrenCount).reversed());      // sorting order: first has the most energy and is the oldest
            }

            if (map.plantPresent(position)) {

                Animal strongest = animalsAtPosition.getFirst();
                try {
                    map.removePlant(position);
                } catch (IncorrectPositionException e) {
                    System.out.println(e.getMessage());
                }
                strongest.eat(config.plantNutritionalValue());
            }
        }
    }

    private void reproduceAnimals() {
        for (List<Animal> animalsAtPosition : animalMap.values()) {
            for (int i = 0; i < (animalsAtPosition.size() - 1); i+=2) {     // assumes that all animals that can reproduce in a given cell will
                Animal first = animalsAtPosition.get(i);
                Animal second = animalsAtPosition.get(i + 1);
                if (first.readyToReproduce() && second.readyToReproduce()) {
                    reproduceAnimals(first, second);
                }
                else break; // if previous pair does not meet criteria, the following pairs also won't as the list is already sorted
            }
        }
    }

    private void taxAnimals() {
        Iterator<Animal> iterator = animalsList.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            animal.endDay();
            if (animal.isDead()) {
                removeAnimal(animal);
                iterator.remove();
            }
        }
    }

    public void simulateDay() {

        dayCount++;

        removeDeadAnimals();
        moveAnimals();
        animalMap = map.getAnimalsMap();
        eatPlants();
        reproduceAnimals();
        taxAnimals();
        map.growPlants(config.plantsToSpawnEachDay());

        statsManager.updateAndSaveStats(dayCount, animalsList, map, cellCount);

    }

    public SimulationStatistics getLastStats() {
        return statsManager.getLastStats();
    }
}

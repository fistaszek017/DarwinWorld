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
    private final List<Animal> animalsList = new ArrayList<>();  //contains all animals
    private Map<Vector2D, List<Animal>> animalMap = new HashMap<>();

    //stats
    private final int cells;
    private int deadAnimalsCount = 0;
    private long sumOfDeadLifeSpan = 0;

    private final List<SimulationStatistics> statsHistory = new ArrayList<>();


    public Simulation(WorldMap map, SimulationConfig config) {
        this.map = map;
        this.config = config;
        this.cells = config.mapHeight() * config.mapWidth();

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

            Animal animal = new Animal(direction, position, config.initialAnimalEnergyValue(), generateRandomGenes(), config); //TODO modify when animal fully implemented

            try {
                map.placeAnimal(animal, position);
                animalsList.add(animal);
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


    private void reproduceAnimals(Animal first, Animal second)  { // assuming the first animal is the stronger one
        int totalEnergy = first.getEnergy() + second.getEnergy();

        int firstShare = (int) ((double) first.getEnergy() / totalEnergy * config.genomeLength());
        int secondShare = config.genomeLength() - firstShare;
        int startEnergy = config.animalReproductionCost() * 2;      //TODO ?? i assume this is correct, might need rethinking
        int[] result = new int[config.genomeLength()];

        Random rand = new Random();

        // baseline genes
        if (rand.nextBoolean()) {
            System.arraycopy(first.getGenes(), 0, result, 0, firstShare);
            System.arraycopy(second.getGenes(), config.genomeLength() - secondShare, result, firstShare, secondShare);
        }
        else {
            System.arraycopy(second.getGenes(), 0, result, 0, secondShare);
            System.arraycopy(first.getGenes(), config.genomeLength() - firstShare, result, secondShare, firstShare);
        }

        // mutations (quick solution)
        int mutations = rand.nextInt(config.minMutationCount(), config.maxMutationCount() + 1);
        Set<Integer> used = new HashSet<>();

        while (mutations > 0) {
            Integer index = rand.nextInt(config.genomeLength());
            if (!used.contains(index)) {
                used.add(index);
                result[index] = (result[index] + 1 + rand.nextInt(7)) % 8;
                mutations--;
            }
        }

        Animal child = new Animal(MapDirection.values()[rand.nextInt(8)], first.getMapPosition(), startEnergy, result, config); //TODO naprawka po poprawkach animala

        try {
            map.placeAnimal(child, child.getMapPosition());
            animalsList.add(child);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }

        first.removeEnergy(config.animalReproductionCost());
        first.addChild();
        second.removeEnergy(config.animalReproductionCost());
        second.addChild();
    }

    private void removeDeadAnimals() {
        Iterator<Animal> iterator = animalsList.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.isDead()) {
                deadAnimalsCount++;
                sumOfDeadLifeSpan += animal.getAge();
                try {
                    map.removeAnimal(animal, animal.getMapPosition());
                } catch (IncorrectPositionException e) {
                    System.out.println(e.getMessage());
                }
                iterator.remove();
            }
        }
    }

    private void moveAnimals() {
        for (Animal animal : animalsList) {
            animal.removeEnergy(config.animalDailyEnergyLoss());
            animal.move(map);
        }
    }

    private void eatPlants() {
        for (Map.Entry<Vector2D, List<Animal>> entry : animalMap.entrySet()) {
            Vector2D position = entry.getKey();
            List<Animal> animalsAtPosition = entry.getValue();

            if (map.plantPresent(position)) {

                if (animalsAtPosition.size() > 1) {
                    animalsAtPosition.sort(Comparator.comparingInt(Animal::getEnergy)
                            .thenComparingInt(Animal::getAge).reversed());      // sorting order: first has the most energy and is the oldest
                }

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

    private void growPlants() {

    }

    private void updateStats() {
        int currentAnimalCount = animalsList.size();
        int currentPlantCount = map.getPlants().size();

        double avgEnergy = animalsList.stream().mapToInt(Animal::getEnergy).average().orElse(0.0);
        double avgChildren = animalsList.stream().mapToInt(Animal::getChildrenCount).average().orElse(0.0);

        double avgLifeSpan = deadAnimalsCount == 0 ? 0.0 :
                (double)  sumOfDeadLifeSpan / deadAnimalsCount;

        //TODO freeFields and most popular genome

        statsHistory.add(new SimulationStatistics(dayCount, currentAnimalCount, currentPlantCount, 0, avgEnergy, avgLifeSpan, avgChildren, null));
    }

    private void simulateDay() {

        dayCount++;

        removeDeadAnimals();
        moveAnimals();
        animalMap = map.getAnimalsMap();
        eatPlants();
        reproduceAnimals();
        growPlants();

        updateStats();

    }
}

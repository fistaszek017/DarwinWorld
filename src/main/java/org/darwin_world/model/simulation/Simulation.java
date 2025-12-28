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
    private List<Animal> animalsList = new ArrayList<>();   //contains all animals

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


        animalsList = map.getAllAnimals();
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


    private void reproduceAnimals(Animal first, Animal second)  // zakładam że zawsze first ma >= energie od second
    {
        int totalEnergy = first.getEnergy() + second.getEnergy();

        int firstShare = (int) ((double) first.getEnergy() / totalEnergy * config.genomeLength());
        int secondShare = config.genomeLength() - firstShare;
        int startEnergy = config.animalReproductionCost() * 2;      //TODO ?? czy tak czy nie mnożymy * 2?
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
                result[index] = (result[index] + 1 + rand.nextInt(7) % 8);
                mutations--;
            }
        }

        map.placeAnimal(new Animal(MapDirection.values()[rand.nextInt(8)], first.getMapPosition(), result, startEnergy, SimulationConfig);       //TODO naprawka po poprawkach animala
        first.removeEnergy(config.animalReproductionCost());
        second.removeEnergy(config.animalReproductionCost());
    }

    private void removeDeadAnimals() {

    }

    private void moveAnimals() {

    }

    private void eatPlants() {

    }

    private void reproduceAnimals() {

    }

    private void growPlants() {

    }

    private void simulateDay() {

        dayCount++;

        removeDeadAnimals();
        moveAnimals();
        eatPlants();
        reproduceAnimals();
        growPlants();

    }
}

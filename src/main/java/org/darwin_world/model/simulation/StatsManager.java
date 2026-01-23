package org.darwin_world.model.simulation;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.util.StatsFileManager;

import java.util.*;

public class StatsManager {
    private final StatsFileManager statsWriter;
    private final List<SimulationStatistics> statsHistory = new ArrayList<>();
    private final Map<List<Integer>, Integer> genomeCount = new HashMap<>();

    private int deadAnimalsCount = 0;
    private long sumOfDeadLifeSpan = 0;

    public StatsManager(int simulationID) {
        this.statsWriter = new StatsFileManager(simulationID);
    }

    public void registerBirth(int[] genes) {
        List<Integer> genesList = Arrays.stream(genes).boxed().toList();
        genomeCount.merge(genesList, 1, Integer::sum);
    }

    public void registerDeath(Animal animal) {
        deadAnimalsCount++;
        sumOfDeadLifeSpan += animal.getAge();

        List<Integer> genesList = Arrays.stream(animal.getGenes()).boxed().toList();
        genomeCount.merge(genesList, -1, Integer::sum);
        if (genomeCount.get(genesList) <= 0) {
            genomeCount.remove(genesList);
        }
    }

    public void updateAndSaveStats(int day, List<Animal> animals, WorldMap map, int totalCells) {
        int currentPlantCount = map.getPlants().size();
        int freeFields = totalCells - map.getOccupiedFieldsCount();

        double avgEnergy = animals.stream().mapToInt(Animal::getEnergy).average().orElse(0.0);
        double avgChildren = animals.stream().mapToInt(Animal::getChildrenCount).average().orElse(0.0);
        double avgLifeSpan = deadAnimalsCount == 0 ? 0.0 : (double) sumOfDeadLifeSpan / deadAnimalsCount;

        List<Integer> genomeList = genomeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(new ArrayList<>());
        int[] mostPopularGenome = genomeList.stream().mapToInt(Integer::intValue).toArray();

        SimulationStatistics dailyStats = new SimulationStatistics(
                day, animals.size(), currentPlantCount, freeFields,
                avgEnergy, avgLifeSpan, avgChildren, mostPopularGenome
        );

        statsHistory.add(dailyStats);
        statsWriter.saveStat(dailyStats);
    }

    public SimulationStatistics getLastStats() {
        return statsHistory.isEmpty() ? null : statsHistory.getLast();
    }

    public void close() {
        statsWriter.close();
    }

}

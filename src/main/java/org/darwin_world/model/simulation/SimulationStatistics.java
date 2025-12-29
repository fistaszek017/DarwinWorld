package org.darwin_world.model.simulation;

public record SimulationStatistics(
        int day,
        int animalCount,
        int plantCount,
        int freeFields,
        double avgEnergy,
        double avgLifeSpan,
        double avgChildrenCount,
        int[] mostPopularGenome
) {
}

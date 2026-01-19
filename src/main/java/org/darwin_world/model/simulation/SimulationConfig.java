package org.darwin_world.model.simulation;

public record SimulationConfig(
        int mapWidth,
        int mapHeight,
        int initialPlantCount,
        int plantNutritionalValue,
        int plantsToSpawnEachDay,
        int initialAnimalCount,
        int initialAnimalEnergyValue,
        int animalDailyEnergyLoss,
        int animalWellFedThreshold,
        int animalReproductionCost,
        int minMutationCount,
        int maxMutationCount,
        int genomeLength,
        int delay,
        boolean variant,
        //dotyczy tylko variantu
        int cultivationEnergyThreshold,
        int fertileSoilThreshold,
        int fertileSoilDuration,
        int largePlantCapacity) {
}

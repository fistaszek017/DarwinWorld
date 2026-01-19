package org.darwin_world.model.simulation;

import org.darwin_world.model.elements.Plant;
import org.darwin_world.model.map.GlobeMap;
import org.darwin_world.model.util.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    private static final SimulationConfig config = new SimulationConfig(
            10, 10,
            10,
            5,
            2,
            5,
            20,
            1,
            10,
            5,
            0,
            2,
            5,
            500,
            false,
            0,
            0,
            0,
            0
    );

    @Test
    void simulationInitializationCorrect() {
        //given
        GlobeMap map = new GlobeMap(config);

        //when
        Simulation simulation = new Simulation(map, config,1);

        //then
        Map<Vector2D, Plant> plants = map.getPlants();
        SimulationStatistics stats = simulation.getLastStats();

        assertTrue(plants.size() >= 9 && plants.size() <= config.initialPlantCount());
        assertEquals(config.initialAnimalCount(), stats.animalCount());

    }
}
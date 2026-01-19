package org.darwin_world;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.simulation.SimulationEngine;

public class Main {
    static void main() {
        System.out.println("dziala");

        SimulationConfig config = new SimulationConfig(
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
                true,
                1,
                1,
                1,
                1
        );


        SimulationEngine engine = new SimulationEngine(config,1);

        engine.addObserver(((stats, map) -> {
            System.out.println("Dzien: " + stats.day() + " Anim: " + stats.animalCount() + " Plnt: " + stats.plantCount());
        }));

        Thread thread = new Thread(engine);
        thread.start();

    }
}

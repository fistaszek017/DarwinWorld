package org.darwin_world.model.simulation;

import org.darwin_world.model.map.GlobeMap;
import org.darwin_world.model.map.VariantMap;
import org.darwin_world.model.map.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements Runnable{

    private final Simulation simulation;
    private final WorldMap map;
    private final List<SimulationChangeListener> simulationChangeListeners = new ArrayList<SimulationChangeListener>();
    private volatile boolean running = false;

    private final int delay; // ms

    public SimulationEngine(SimulationConfig config, int id) {
        if (config.variant()) {
            this.map = new VariantMap(config);
        }
        else {
            this.map = new GlobeMap(config);
        }
        this.simulation = new Simulation(map, config, id);
        this.delay = config.delay();
    }

    public void addObserver(SimulationChangeListener listener) {
        simulationChangeListeners.add(listener);
    }

    public void runSync() {
        simulation.simulateDay();
        notifyObservers();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            runSync();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }

    private void notifyObservers() {
        SimulationStatistics stats = simulation.getLastStats();
        for (SimulationChangeListener listener : simulationChangeListeners) {
            listener.simulationChanged(stats, map);
        }
    }


}

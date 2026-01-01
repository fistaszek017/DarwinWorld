package org.darwin_world.model.simulation;

import org.darwin_world.model.map.WorldMap;

public interface SimulationChangeListener {
    void simulationChanged(SimulationStatistics stats, WorldMap map);
}

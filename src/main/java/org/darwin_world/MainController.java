package org.darwin_world;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.simulation.Simulation;
import org.darwin_world.model.simulation.SimulationConfig;

public class MainController {

    @FXML private Canvas canvas;
    @FXML private Label statsLabel;

    private Simulation simulation;
    private SimulationRenderer renderer;

    private AnimationTimer timer;
    private boolean running = false;

    public void init(Simulation simulation, WorldMap map, SimulationConfig config) {
        this.simulation = simulation;
        this.renderer = new SimulationRenderer(canvas, map);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    simulation.simulateDay();
                    renderer.render();
                    updateStats();
                }
            }
        };
        timer.start();

        renderer.render();
        updateStats();
    }

    @FXML
    private void onStart() {
        running = true;
    }

    @FXML
    private void onStop() {
        running = false;
    }

    @FXML
    private void onNextDay() {
        simulation.simulateDay();
        renderer.render();
        updateStats();
    }

    private void updateStats() {
        var s = simulation.getLastStats();
        statsLabel.setText(
                "Day: " + s.day() +
                        " | Animals: " + s.animalCount() +
                        " | Plants: " + s.plantCount() +
                        " | Free fields: " + s.freeFields()
        );
    }
}

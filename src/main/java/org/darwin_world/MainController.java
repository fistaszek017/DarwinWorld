package org.darwin_world;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.simulation.*;

import java.util.Arrays;

public class MainController implements SimulationChangeListener {

    public static double roundTo2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @FXML private Canvas canvas;
    @FXML private Label statsLabel1;
    @FXML private Label statsLabel2;

    private SimulationEngine engine;
    private SimulationRenderer renderer;

    private Thread engineThread;

    public void init(SimulationConfig config, int id) {
        this.engine = new SimulationEngine(config, id);
        WorldMap map = engine.getMap();

        this.renderer = new SimulationRenderer(canvas, map);

        engine.addObserver(this);

        renderer.render();
    }

    @FXML
    private void onStart() {
        if (engineThread == null || !engineThread.isAlive()) {
            engineThread = new Thread(engine);
            engineThread.setDaemon(true);
            engineThread.start();
        }
    }

    @FXML
    private void onStop() {
        engine.stop();
    }

    @FXML
    private void onNextDay() {
        engine.runSync();
    }

    @Override
    public void simulationChanged(SimulationStatistics stats, WorldMap map) {
        Platform.runLater(() -> {
            renderer.render();
            updateStats(stats);
        });
    }

    private void updateStats(SimulationStatistics s) {
        statsLabel1.setText(
                "Day: " + s.day() +
                        " | Animals: " + s.animalCount() +
                        " | Plants: " + s.plantCount() +
                        " | Free fields: " + roundTo2(s.freeFields()) +
                        " | Average energy: " + roundTo2(s.avgEnergy())
        );
        statsLabel2.setText(
                    "Average life span: " + roundTo2(s.avgLifeSpan()) +
                    " | Average children count: " + roundTo2(s.avgChildrenCount()) +
                    " | Most popular genome: " + Arrays.toString(s.mostPopularGenome())
        );
    }
}

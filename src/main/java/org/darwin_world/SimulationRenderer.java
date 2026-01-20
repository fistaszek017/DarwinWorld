package org.darwin_world;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.darwin_world.model.map.WorldMap;

public class SimulationRenderer {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final WorldMap map;

    public SimulationRenderer(Canvas canvas, WorldMap map) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.map = map;
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double cellW = canvas.getWidth() / map.getWidth();
        double cellH = canvas.getHeight() / map.getHeight();

        // plants
        map.getPlants().forEach((pos, plant) -> {
            gc.setFill(Color.GREEN);
            gc.fillRect(pos.x() * cellW, pos.y() * cellH, cellW, cellH);
        });

        // animals
        map.getAnimalsMap().forEach((pos, animals) -> {
            gc.setFill(Color.RED);
            gc.fillOval(pos.x() * cellW, pos.y() * cellH, cellW, cellH);
        });
    }
}

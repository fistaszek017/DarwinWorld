package org.darwin_world;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.map.WorldMap;

import java.util.Comparator;

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
        gc.setFill(Color.LIGHTYELLOW);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double cellW = canvas.getWidth() / map.getWidth();
        double cellH = canvas.getHeight() / map.getHeight();

        // plants
        map.getPlants().forEach((pos, plant) -> {
            gc.setFill(Color.LIGHTGREEN);
            if (plant.getUses() > 1){
                gc.setFill(Color.DARKGREEN);
            }
            gc.fillRect(pos.x() * cellW, pos.y() * cellH, cellW, cellH);
        });

        // animals
        map.getAnimalsMap().forEach((pos, animals) -> {

            double maxEnergy = animals.stream()
                    .mapToDouble(Animal::getEnergy)
                    .max()
                    .orElse(1);

            int count = animals.size();
            int grid = (int) Math.ceil(Math.sqrt(count)); // fit every animal inside cell
            double size = cellW / grid; // one circle size

            int i = 0;

            for (Animal a : animals) {

                double ratio = a.getEnergy() / maxEnergy;

                double highR = 1.0;
                double highG = 0.8;
                double highB = 0.0;

                double lowR = 1.0;
                double lowG = 0.0;
                double lowB = 0.0;

                double r = lowR * (1 - ratio) + highR * ratio;
                double g = lowG * (1 - ratio) + highG * ratio;
                double b = lowB * (1 - ratio) + highB * ratio;

                gc.setFill(new Color(r, g, b, 1.0));

                int row = i / grid;
                int col = i % grid;

                double x = pos.x() * cellW + col * size;
                double y = pos.y() * cellH + row * size;

                gc.fillOval(x, y, size, size);

                i++;
            }
        });




    }
}

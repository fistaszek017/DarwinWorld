package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.exceptions.IncorrectPositionException;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobeMapTest {

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
            false
    );

    @Test
    void isAmountOfGeneratedPlantsCorrect() {
        //given
        GlobeMap map = new GlobeMap(config.mapWidth(), config.mapHeight());

        //when
        map.growPlants(10);

        //then
        assertEquals(10, map.getPlants().size());
    }

    @Test
    void isMoveHandlingCorrect() {
        //given
        GlobeMap map = new GlobeMap(config.mapWidth(), config.mapHeight());

        Animal a1 = new Animal(MapDirection.NORTH, new Vector2D(9,9), 67, new int[]{0} , config);   // going around the pole vertically
        Animal a2 = new Animal(MapDirection.EAST, new Vector2D(9,9), 67, new int[]{0} , config);    // wrapping around the globe horizontally
        try {
            map.placeAnimal(a1, a1.getMapPosition());
            map.placeAnimal(a2, a2.getMapPosition());
        } catch (IncorrectPositionException e) {
            throw new RuntimeException(e);
        }

        //when
        a1.move(map);
        a2.move(map);

        //then
        assertEquals(new Vector2D(0,9), a1.getMapPosition());
        assertEquals(MapDirection.SOUTH, a1.getFacingDirection());

        assertEquals(new Vector2D(0,9), a2.getMapPosition());
        assertEquals(MapDirection.EAST, a2.getFacingDirection());
    }
}
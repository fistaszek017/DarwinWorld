package org.darwin_world.model.map;

import org.darwin_world.model.elements.Animal;
import org.darwin_world.model.elements.Plant;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class AbstractWorldMapTest {


    private static AbstractWorldMap createMap() {
        return new AbstractWorldMap(config) {
            @Override
            public void growPlants(int count) {
                return;
            }

            @Override
            public MoveResult handleMove(Vector2D position, MapDirection direction) {
                return null;
            }
        };
    }


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
    void isInBoundsCorrect() {
        //given
        AbstractWorldMap map = createMap();

        //then
        assertTrue(map.isInBounds(new Vector2D(9,9)));
        assertFalse(map.isInBounds(new Vector2D(10,10)));
    }

    @Test
    void placeAnimalCorrect() {
        //given
        AbstractWorldMap map = createMap();
        Vector2D position = new Vector2D(0,0);
        Animal a1 =  new Animal(MapDirection.NORTH, position, 67, new int[]{0}, config);

        //when & then
        assertDoesNotThrow(() -> map.placeAnimal(a1, position));
        Map<Vector2D, List<Animal>> retMap = map.getAnimalsMap();

        assertTrue(retMap.containsKey(position));
        assertTrue(retMap.get(position).contains(a1));

    }

    @Test
    void removeAnimalCorrect() {
        //given
        AbstractWorldMap map = createMap();
        Vector2D position = new Vector2D(0,0);
        Animal a1 =  new Animal(MapDirection.NORTH, position, 67, new int[]{0}, config);
        Animal a2 =  new Animal(MapDirection.NORTH, position, 67, new int[]{0}, config);
        assertDoesNotThrow(() -> map.placeAnimal(a1, position));
        assertDoesNotThrow(() -> map.placeAnimal(a2, position));

        //when & then
        assertDoesNotThrow(() -> map.removeAnimal(a1, position));
        Map<Vector2D, List<Animal>> retMap = map.getAnimalsMap();

        assertTrue(retMap.containsKey(position));
        assertFalse(retMap.get(position).contains(a1));

        assertDoesNotThrow(() -> map.removeAnimal(a2, position));
        retMap = map.getAnimalsMap();
        assertFalse(retMap.containsKey(position));  // if last animal in list removed, check if the key was also removed
    }

    @Test
    void placePlantCorrect() {
        //given
        AbstractWorldMap map = createMap();
        Vector2D position = new Vector2D(0,0);

        Plant p1 = new Plant(position,1);

        //when & then
        assertDoesNotThrow(() -> map.placePlant(p1, position));
        Map<Vector2D, Plant> retMap = map.getPlants();

        assertTrue(retMap.containsKey(position));
        assertEquals(retMap.get(position), p1);
    }

    @Test
    void removePlantCorrect() {
        //given
        AbstractWorldMap map = createMap();
        Vector2D position = new Vector2D(0,0);
        Plant p1 = new Plant(position,1);
        assertDoesNotThrow(() -> map.placePlant(p1, position));

        //when & then
        assertDoesNotThrow(() -> map.removePlant(position));
        Map<Vector2D, Plant> retMap = map.getPlants();
        assertFalse(retMap.containsKey(position));
    }

    @Test
    void moveCorrect() {
        //given
        AbstractWorldMap map = createMap();
        Vector2D position1 = new Vector2D(0,0);
        Vector2D position2 = new Vector2D(1,1);
        Animal a1 = new Animal(MapDirection.NORTH, position1, 67, new int[]{0}, config);
        assertDoesNotThrow(() -> map.placeAnimal(a1, position1));

        //when & then
        assertDoesNotThrow(() -> map.move(a1, position1, position2));
        Map<Vector2D, List<Animal>> retMap = map.getAnimalsMap();
        assertFalse(retMap.containsKey(position1));
        assertTrue(retMap.containsKey(position2));
        assertEquals(retMap.get(position2).getFirst(), a1);
    }
}
package org.darwin_world.model.elements;

import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.util.Vector2D;

public class Animal {
    private final MapDirection facingDirection;
    private final Vector2D mapPosition;
    private int energy;
    private final int genesNumber;
    private final int lostEnergy;
    private final int reproductionEnergy;
    private final int mutationNumber;
    private final int fedEnergy;

    public Animal(MapDirection facingDirection, Vector2D mapPosition, int startEnergy, int genesNumber, int lostEnergy, int reproductionEnergy, int mutationNumber, int fedEnergy) {
        this.facingDirection = facingDirection;
        this.mapPosition = mapPosition;
        this.energy = startEnergy; //energia startowa
        this.genesNumber = genesNumber; //liczba genów
        this.lostEnergy = lostEnergy; //energia tracona pod koniec dnia
        this.reproductionEnergy = reproductionEnergy; //energia zużywana przy reprodukcji
        this.mutationNumber = mutationNumber; //liczba mutacji
        this.fedEnergy = fedEnergy; //energia, aby zwierzak był gotowy do rozmnażania
    }

    public Vector2D getMapPosition() {
        return mapPosition;
    }

    public MapDirection getFacingDirection() {
        return facingDirection;
    }

    public void move(WorldMap map) {
        //TODO
        // jak ja to widze - przekazujesz zamiar do handleMove z GlobeMap, to ci zwraca pozycje na ktora zmieniasz i potem wywolujesz move z mapy

    }



    public boolean isDead() {
        return energy <= 0;
    }
}

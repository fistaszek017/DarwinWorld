package org.darwin_world.model.elements;

import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.MoveResult;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Animal {
    private MapDirection facingDirection;
    private Vector2D mapPosition;
    private int energy;
    private final int[] genes; //generalnie do przekminienia - czy chcemy geny jako tablice intów czy string - ja chyba jestem za pierwszą opcją, ale nwm
    private final int lostEnergy;
    private final int fedEnergy;
    private int age;

    private int childrenCount = 0;

    public Animal(MapDirection facingDirection, Vector2D mapPosition, int startEnergy ,int[] genes, SimulationConfig config) {
        this.facingDirection = facingDirection;
        this.mapPosition = mapPosition;
        this.energy = startEnergy; //energia startowa
        this.genes = genes; //geny - ich liczba
        this.lostEnergy = config.animalDailyEnergyLoss(); //energia tracona pod koniec dnia
        this.fedEnergy = config.animalWellFedThreshold(); //energia, aby zwierzak był gotowy do rozmnażania
        this.age = 0;
    }

    public Vector2D getMapPosition() {
        return mapPosition;
    }

    public MapDirection getFacingDirection() {
        return facingDirection;
    }

    public int[] getGenes() {
        return genes;
    }

    public void move(WorldMap map) {
        int todaysGene = this.genes[age %genes.length];
        this.facingDirection = this.facingDirection.rotate(todaysGene);
        MoveResult result = map.handleMove(this.mapPosition, this.facingDirection);
        Vector2D oldPosition = this.mapPosition;

        try {
            map.move(this, oldPosition, result.position());
        } catch (Exception e) {
            System.out.println("Wrong move from " + oldPosition + " to " + result.position());
            return;
            // nie przesuwa animala
        }

        this.mapPosition = result.position();
        this.facingDirection = result.direction();
    }

    public void eat(int amount) {
        this.energy += amount;
    }
    public void removeEnergy(int amount) {
        this.energy -= amount;
    }
    public void endDay() {
        energy -= lostEnergy;
        this.age++;
    }

    public boolean readyToReproduce(){
        return energy >= fedEnergy && energy >= 2 * lostEnergy;
    }       // assuming wellFedThreshold >= reproductionCost

    public int getEnergy() {
        return energy;
    }


    public boolean isDead() {
        return energy <= 0;
    }

    public int getAge() {
        return age;
    }

    public void addChild() {
        childrenCount++;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

}

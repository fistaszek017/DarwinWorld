package org.darwin_world.model.elements;

import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.MoveResult;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.util.Vector2D;

import java.util.Arrays;

public class Animal {
    private MapDirection facingDirection;
    private Vector2D mapPosition;
    private int energy;
    private final int[] genes; //generalnie do przekminienia - czy chcemy geny jako tablice intów czy string - ja chyba jestem za pierwszą opcją, ale nwm
    private final int lostEnergy;
    private final int reproductionEnergy;
    private final int mutationNumber;
    private final int fedEnergy;
    private int day;

    public Animal(MapDirection facingDirection, Vector2D mapPosition, int startEnergy, int[] genes, int lostEnergy, int reproductionEnergy, int mutationNumber, int fedEnergy) {
        this.facingDirection = facingDirection;
        this.mapPosition = mapPosition;
        this.energy = startEnergy; //energia startowa
        this.genes = genes; //geny - ich liczba
        this.lostEnergy = lostEnergy; //energia tracona pod koniec dnia
        this.reproductionEnergy = reproductionEnergy; //energia zużywana przy reprodukcji
        this.mutationNumber = mutationNumber; //liczba mutacji
        this.fedEnergy = fedEnergy; //energia, aby zwierzak był gotowy do rozmnażania
        this.day = 0;
    }

    public Vector2D getMapPosition() {
        return mapPosition;
    }

    public MapDirection getFacingDirection() {
        return facingDirection;
    }

    public void move(WorldMap map) {
        //TODO //zostawiam ten komentarz dla potomnych (póki co) xd
        // jak ja to widze - przekazujesz zamiar do handleMove z GlobeMap, to ci zwraca pozycje na ktora zmieniasz i potem wywolujesz move z mapy
        int todaysGene = this.genes[day%genes.length];
        this.facingDirection = this.facingDirection.rotate(todaysGene);
        MoveResult result = map.handleMove(this.mapPosition, this.facingDirection);
        this.mapPosition = result.position();
        this.facingDirection = result.direction();
    }

    public void eatPlant(WorldMap map) {
        //jesli pozycja na mapie jest wsrod zbioru pozycji w Simulation, to zwieksz energie o fedEnergy
    }

    public int[] handleMutation(int[] finalGenes){
        //losuje liczbe mutacji
        return finalGenes;
    }

    public void endDay() {
        energy -= lostEnergy;
        this.day++;
    }

    public boolean readyToReproduce(){
        return energy >= reproductionEnergy;
    }

    public boolean reproduction(Animal animal2, Animal newAnimal){
        if (this.readyToReproduce() || animal2.readyToReproduce()){
            return false;
        }
        int [] genes1 = this.genes;
        int [] genes2 = animal2.genes;
        //udzial genow w nowym genotypie
        int animal1GenesNumber = this.energy/ (this.energy + animal2.energy);
        int animal2GenesNumber = this.energy + animal2.energy - animal1GenesNumber;
        //losuje strone
        double side = Math.random()*2;
        int[] finalGenes = new int[genes1.length];
        if (side < 1){
            System.arraycopy(genes1, 0, finalGenes, 0, animal1GenesNumber);
            System.arraycopy(genes2, 0, finalGenes, animal1GenesNumber, animal2GenesNumber);
        }
        else{
            System.arraycopy(genes1, 0, finalGenes, 0, animal2GenesNumber);
            System.arraycopy(genes2, 0, finalGenes, animal2GenesNumber, animal1GenesNumber);
        }
        //mutacje
        finalGenes = handleMutation(finalGenes);
        
        //energia na reprodukcje
        this.energy -= reproductionEnergy;
        animal2.energy -= reproductionEnergy;
        //tworzy zwierzaka
        newAnimal.energy = reproductionEnergy;

        return true;
    }

    public int getEnergy() {
        return energy;
    }


    public boolean isDead() {
        return energy <= 0;
    }
}

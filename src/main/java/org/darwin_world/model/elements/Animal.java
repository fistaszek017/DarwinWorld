package org.darwin_world.model.elements;

import org.darwin_world.model.map.MapDirection;
import org.darwin_world.model.map.MoveResult;
import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.simulation.SimulationConfig;
import org.darwin_world.model.util.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Animal {
    private MapDirection facingDirection;
    private Vector2D mapPosition;
    private int energy;
    private final int[] genes; //generalnie do przekminienia - czy chcemy geny jako tablice intów czy string - ja chyba jestem za pierwszą opcją, ale nwm
    private final int lostEnergy;
    private final int reproductionEnergy;
    private final int minMutationNumber;
    private final int maxMutationNumber;
    private final int fedEnergy;
    private int day;

    private int childrenCount = 0;

    public Animal(MapDirection facingDirection, Vector2D mapPosition, int startEnergy ,int[] genes, SimulationConfig config) {
        this.facingDirection = facingDirection;
        this.mapPosition = mapPosition;
        // --- WAZNE PYTANIE - czy startEnergy to to samo co reproductionEnergy ---
        this.energy = startEnergy; //energia startowa
        this.genes = genes; //geny - ich liczba
        this.lostEnergy = config.animalDailyEnergyLoss(); //energia tracona pod koniec dnia ||| mozliwe ze niepotrzebne
        this.reproductionEnergy = config.animalReproductionCost(); //energia zużywana przy reprodukcji  ||| tak samo
        this.minMutationNumber = config.minMutationCount(); //minimalna liczba mutacji ||| tak samo
        this.maxMutationNumber = config.maxMutationCount(); //maksymalna liczba mutacji ||| tak samo
        this.fedEnergy = config.animalWellFedThreshold(); //energia, aby zwierzak był gotowy do rozmnażania
        this.day = 0;
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
        int todaysGene = this.genes[day%genes.length];
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


    public int[] handleMutation(int[] finalGenes){      // nie w animalu, juz w symulacji
        //losuje liczbe mutacji
        int mutationNumber = (int) ((Math.random()*(this.maxMutationNumber-this.minMutationNumber))+this.minMutationNumber);
        //losuje miejsca w genomie gdzie zwierzaki mutuja
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= this.genes.length; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        List<Integer> changedGenes = numbers.subList(0, mutationNumber);
        //dla kazdej zmiany w genomie losuje nowy gen
        for (int i=0; i<changedGenes.size();i++){
            int newGene = (int) (Math.random()*8);
            int x = changedGenes.get(i);
            if (finalGenes[x] == newGene){
                i--;
                continue;
            }
            finalGenes[x] = newGene;
        }
        return finalGenes;
    }

    public void endDay() {
        energy -= lostEnergy;
        this.day++;
    }

    public boolean readyToReproduce(){
        return energy >= reproductionEnergy;
    }

    public boolean reproduct(Animal animal2){   // niepotrzebne w animalu, logika w symulacji
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
        //Animal newAnimal = new Animal(this.facingDirection, this.mapPosition, reproductionEnergy, finalGenes, this.lostEnergy, this.reproductionEnergy, this.minMutationNumber, this.maxMutationNumber, this.fedEnergy);
        /// --- TODO - dodać zwierzaka do symulacji ---
        return true;
    }

    public int getEnergy() {
        return energy;
    }


    public boolean isDead() {
        return energy <= 0;
    }

    public int getAge() {
        return day;
    }

    public void addChild() {
        childrenCount++;
    }

    public int getChildrenCount() {
        return childrenCount;
    }
}

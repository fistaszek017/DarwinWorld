package org.darwin_world.model.simulation;

import org.darwin_world.model.elements.Animal;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeneUtils {
    private final SimulationConfig config;
    private final Random rand =  new Random();

    public GeneUtils(SimulationConfig config) {
        this.config = config;
    }

    public int[] generateRandomGenes()
    {
        int[] geneTable = new int[config.genomeLength()];
        for (int i = 0; i < config.genomeLength(); i++) {
            geneTable[i] = rand.nextInt(8);
        }

        return geneTable;
    }

    public int[] createChildGenotype(Animal first, Animal second) {
        int totalEnergy = first.getEnergy() + second.getEnergy();
        int firstShare = (int) ((double) first.getEnergy() / totalEnergy * config.genomeLength());
        int secondShare = config.genomeLength() - firstShare;
        int[] result = new int[config.genomeLength()];

        // baseline genes
        if (rand.nextBoolean()) {
            System.arraycopy(first.getGenes(), 0, result, 0, firstShare);
            System.arraycopy(second.getGenes(), config.genomeLength() - secondShare, result, firstShare, secondShare);
        }
        else {
            System.arraycopy(second.getGenes(), 0, result, 0, secondShare);
            System.arraycopy(first.getGenes(), config.genomeLength() - firstShare, result, secondShare, firstShare);
        }
        // mutations
        int mutations = rand.nextInt(config.minMutationCount(), config.maxMutationCount() + 1);
        Set<Integer> used = new HashSet<>();

        while (mutations > 0) {
            Integer index = rand.nextInt(config.genomeLength());
            if (!used.contains(index)) {
                used.add(index);
                result[index] = (result[index] + 1 + rand.nextInt(7)) % 8;
                mutations--;
            }
        }

        return result;
    }
}

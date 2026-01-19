package org.darwin_world.model.simulation;

import java.util.Arrays;
import java.util.stream.Collectors;

public record SimulationStatistics(
        int day,
        int animalCount,
        int plantCount,
        int freeFields,
        double avgEnergy,
        double avgLifeSpan,
        double avgChildrenCount,
        int[] mostPopularGenome
) {
    public static String getHeaders() {
        return "Day;Animals;Plants;FreeFields;AvgEnergy;AvgLifeSpan;AvgChildren;MostPopularGenome";
    }

    public String toCsvLine() {
        String genomeString = (mostPopularGenome == null || mostPopularGenome.length == 0) ? "-"
                : Arrays.stream(mostPopularGenome).mapToObj(String::valueOf).collect(Collectors.joining(" "));

        return String.format(
                "%d;%d;%d;%d;%.2f;%.2f;%.2f;%s",
                day,
                animalCount,
                plantCount,
                freeFields,
                avgEnergy,
                avgLifeSpan,
                avgChildrenCount,
                genomeString
        );
    }
}

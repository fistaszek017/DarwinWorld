package org.darwin_world.model.util;

import org.darwin_world.model.simulation.SimulationStatistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatsFileManager {
    private BufferedWriter writer;

    public StatsFileManager(int id) {
        String fileName = "staty/stats_" + id + ".csv";
        File plik = new File(fileName);

        try {

            if (plik.getParentFile() != null) {
                plik.getParentFile().mkdirs();
            }
            this.writer = new BufferedWriter(new FileWriter(fileName));
            writeHeader();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeHeader() {
        try {
            writer.write(SimulationStatistics.getHeaders());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveStat(SimulationStatistics stat) {
        if (writer == null) return;
        try {
            writer.write(stat.toCsvLine());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

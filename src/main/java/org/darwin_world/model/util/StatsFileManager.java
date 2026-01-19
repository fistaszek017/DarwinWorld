package org.darwin_world.model.util;

import org.darwin_world.model.simulation.SimulationStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class StatsFileManager {
    private BufferedWriter writer;

    public StatsFileManager(int id) {
        String fileName = "stats_" + id + ".csv";

        try {
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

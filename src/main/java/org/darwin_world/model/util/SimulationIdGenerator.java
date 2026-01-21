package org.darwin_world.model.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SimulationIdGenerator {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    public static int nextId() {
        return COUNTER.getAndIncrement();
    }
}


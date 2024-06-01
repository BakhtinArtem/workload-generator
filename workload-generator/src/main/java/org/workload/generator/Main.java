package org.workload.generator;


import org.workload.generator.argumentParser.Configuration;
import org.workload.generator.mode.OneShot;
import org.workload.generator.mode.Throughput;

public class Main {

    public static void main(String[] args) {
        final Configuration configuration = Configuration.parseArguments(args);
        switch (configuration.getMode()) {
            case oneShot -> Executor.execute(new OneShot(), configuration);
            case throughput -> Executor.execute(new Throughput(), configuration);
            default -> throw new RuntimeException("Unsupported mode type.");
        }
    }
}

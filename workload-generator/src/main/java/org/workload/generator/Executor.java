package org.workload.generator;

import org.workload.generator.argumentParser.Configuration;
import org.workload.generator.mode.IGeneratorMode;

public class Executor {
    public static void execute (IGeneratorMode generator, Configuration configuration) {
        generator.execute(configuration);
    }
}

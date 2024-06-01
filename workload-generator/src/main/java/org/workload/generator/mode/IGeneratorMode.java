package org.workload.generator.mode;

import org.workload.generator.argumentParser.Configuration;

public interface IGeneratorMode {
    void execute(Configuration configuration);
}

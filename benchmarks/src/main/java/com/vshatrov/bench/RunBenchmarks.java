package com.vshatrov.bench;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Viktor Shatrov.
 */
public class RunBenchmarks {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Deserialization.class.getSimpleName())
                .include(Serialization.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(15)
                .output("profile.txt")
                .forks(3)
                .build();

        new Runner(options).run();
    }
}

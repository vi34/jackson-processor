package com.vshatrov.bench;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * @author Viktor Shatrov.
 */
public class RunBenchmarks {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Deserialization.class.getSimpleName())
                //.include(Serialization.class.getSimpleName())
                .warmupIterations(4)
                .timeUnit(TimeUnit.NANOSECONDS)
                .mode(Mode.AverageTime)
                .measurementIterations(9)
                .output("profile.txt")
                .forks(3)
                .build();

        new Runner(options).run();
    }
}

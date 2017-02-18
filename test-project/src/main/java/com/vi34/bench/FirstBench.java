package com.vi34.bench;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vi34.PojoSerializer;
import com.vi34.entities.Pojo;
import com.vi34.entities.Pojo2;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created by vi34 on 16/02/2017.
 */
@State(Scope.Thread)
public class FirstBench {

    ObjectMapper mapper;
    MappingJsonFactory factory;
    Pojo pojo;
    Pojo2 pojo2;

    public FirstBench() {
        factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);


    }

    @Setup(Level.Trial)
    public void setup() {
        mapper = new ObjectMapper(factory);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Pojo.class, new PojoSerializer());
        mapper.registerModule(module);
        pojo = new Pojo(12345, "pojo", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8 ,9 ,0), false, 3.1, 999, 'a');
        pojo2 = new Pojo2(12345, "pojo2", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8 ,9 ,0), false, 3.1, 999, 'a');
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String customSerializer() throws JsonProcessingException {
        return mapper.writeValueAsString(pojo);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String reflection() throws JsonProcessingException {
        return mapper.writeValueAsString(pojo2);
    }



    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(FirstBench.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(options).run();
    }
}

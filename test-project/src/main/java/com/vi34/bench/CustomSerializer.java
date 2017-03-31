package com.vi34.bench;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vi34.serializers.ComplexSerializer;
import com.vi34.serializers.PojoSerializer;
import com.vi34.entities.Pojo;
import com.vi34.entities.Complex;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by vi34 on 16/02/2017.
 */
@State(Scope.Thread)
public class CustomSerializer {

    ObjectMapper mapper;
    MappingJsonFactory factory;
    Pojo pojo;
    Complex complex;

    public CustomSerializer() {
        factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);

    }

    @Setup(Level.Trial)
    public void setup() {
        mapper = new ObjectMapper(factory);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Pojo.class, new PojoSerializer());
        module.addSerializer(Complex.class, new ComplexSerializer());
        mapper.registerModule(module);
        pojo = Pojo.makePojo();
        complex = Complex.makeComplex(7);
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String customSerializer() throws JsonProcessingException {
        return mapper.writeValueAsString(pojo);
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String complex() throws JsonProcessingException {
        return mapper.writeValueAsString(complex);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(CustomSerializer.class.getSimpleName())
                .include(ReflectionBench.class.getSimpleName())
                .include(AfterBurner.class.getSimpleName())
                .warmupIterations(5)
                .output("profile.txt")
                .forks(1)
                .build();

        new Runner(options).run();
    }
}

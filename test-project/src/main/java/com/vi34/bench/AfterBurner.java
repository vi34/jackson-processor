package com.vi34.bench;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.vi34.entities.Complex;
import com.vi34.entities.Pojo;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by vi34 on 16/02/2017.
 */
@State(Scope.Thread)
public class AfterBurner {

    ObjectMapper mapper;
    MappingJsonFactory factory;
    Pojo pojo;
    Complex complex;

    public AfterBurner() {
        factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    @Setup(Level.Trial)
    public void setup() {
        mapper = new ObjectMapper(factory);
        mapper.registerModule(new AfterburnerModule());
        pojo = Pojo.makePojo();
        complex = Complex.makeComplex(7);
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String afterBurner() throws JsonProcessingException {
        return mapper.writeValueAsString(pojo);
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String complex() throws JsonProcessingException {
        return mapper.writeValueAsString(complex);
    }

}

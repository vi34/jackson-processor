package com.vshatrov.bench;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.vshatrov.model.media.MediaItem;
import com.vshatrov.serializers.*;
import com.vshatrov.model.Pojo;
import com.vshatrov.model.Complex;
import com.vshatrov.serializers.MediaItemSerializer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author Viktor Shatrov.
 */
@State(Scope.Thread)
public class Serialization {

    ObjectMapper mapper;
    MappingJsonFactory factory;
    MediaItem mediaItem;

    @Param({"afterBurner", "handWritten", "reflection", "processor"})
    String method;

    public Serialization() {
        factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    @Setup(Level.Iteration)
    public void setup() {
        mapper = new ObjectMapper(factory);
        SimpleModule handModule = new SimpleModule();
        handModule.addSerializer(Pojo.class, new PojoSerializer());
        handModule.addSerializer(Complex.class, new ComplexSerializer());
        handModule.addSerializer(MediaItem.class, new MediaItemSerializer());
        SimpleModule procModule = new SimpleModule();

        switch (method) {
            case "afterBurner": mapper.registerModule(new AfterburnerModule());
                break;
            case "handWritten": mapper.registerModule(handModule); break;
            case "processor": mapper.registerModule(procModule); break;
            default: // reflection
        }

        mediaItem = MediaItem.buildItem();
    }


    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String media() throws JsonProcessingException {
        return mapper.writeValueAsString(mediaItem);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Serialization.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(4)
                .forks(1)
                .build();

        new Runner(options).run();
    }
}

package com.vi34.bench;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.vi34.*;
import com.vi34.entities.PrivateComplex;
import com.vi34.entities.PrivatePojo;
import com.vi34.entities.media.MediaItem;
import com.vi34.serializers.*;
import com.vi34.entities.Pojo;
import com.vi34.entities.Complex;
import com.vi34.serializers.MediaItemSerializer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

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
    PrivatePojo privatePojo;
    PrivateComplex privateComplex;
    MediaItem mediaItem;

    @Param({"afterBurner", "custom", "reflection", "processor"})
    String method;

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
        //module.addSerializer(PrivatePojo.class, new PPojoSerializer());
        //module.addSerializer(PrivateComplex.class, new PComplexSerializer());
        module.addSerializer(MediaItem.class, new MediaItemSerializer());
        SimpleModule procModule = new SimpleModule();
        procModule.addSerializer(MediaItem.class, new com.vi34.MediaItemSerializer());
        switch (method) {
            case "afterBurner": mapper.registerModule(new AfterburnerModule());
                break;
            case "custom": mapper.registerModule(module); break;
            case "processor": mapper.registerModule(procModule); break;
            default:
        }

        privatePojo = PrivatePojo.makePrivatePojo();
        pojo = Pojo.makePojo();
        complex = Complex.makeComplex(7);
        privateComplex = PrivateComplex.makeComplex(7);
        mediaItem = MediaItem.buildItem();
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String pojo() throws JsonProcessingException {
        return mapper.writeValueAsString(pojo);
    }

   /* @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String complex() throws JsonProcessingException {
        return mapper.writeValueAsString(complex);
    }
*/
    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String media() throws JsonProcessingException {
        return mapper.writeValueAsString(mediaItem);
    }

   /* @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String privatePojo() throws JsonProcessingException {
        return mapper.writeValueAsString(privatePojo);
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String privateComplex() throws JsonProcessingException {
        return mapper.writeValueAsString(privateComplex);
    }*/

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(CustomSerializer.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(12)
                .output("profile.txt")
                .forks(2)
                .threads(2)
                .build();

        new Runner(options).run();
    }
}

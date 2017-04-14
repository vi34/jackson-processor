package com.vi34.bench;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.vi34.deserializers.MediaItemDeserializer;
import com.vi34.deserializers.MediaItemDeserializerNode;
import com.vi34.entities.Pojo;
import com.vi34.entities.media.MediaItem;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by vi34 on 16/02/2017.
 */
@State(Scope.Thread)
public class Deserialization {

    ObjectMapper mapper;
    MappingJsonFactory factory;
    Pojo pojo;
    MediaItem mediaItem;

    @Param({"afterBurner", "custom", "reflection", "processor"})
    String method;

    public Deserialization() {
        factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    @Setup(Level.Trial)
    public void setup() {
        mapper = new ObjectMapper(factory);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MediaItem.class, new MediaItemDeserializer());
        SimpleModule procModule = new SimpleModule();
        procModule.addDeserializer(MediaItem.class, new MediaItemDeserializerNode());

        switch (method) {
            case "afterBurner": mapper.registerModule(new AfterburnerModule());
                break;
            case "custom": mapper.registerModule(module); break;
            case "processor": mapper.registerModule(procModule); break;
            default:
        }

        mediaItem = MediaItem.buildItem();
    }

   /* @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String pojo() throws JsonProcessingException {
        return mapper.writeValueAsString(pojo);
    }*/

   /* @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String complex() throws JsonProcessingException {
        return mapper.writeValueAsString(complex);
    }
*/
    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public MediaItem media() throws IOException {
        return mapper.readValue(json, MediaItem.class);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Deserialization.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(12)
                .output("profile.txt")
                .forks(2)
                .threads(2)
                .build();

        new Runner(options).run();
    }

    private final String json = "{\n" +
            "  \"media\" : {\n" +
            "    \"player\" : \"JAVA\",\n" +
            "    \"uri\" : \"http://javaone.com/keynote.mpg\",\n" +
            "    \"title\" : \"Javaone Keynote\",\n" +
            "    \"width\" : 640,\n" +
            "    \"height\" : 480,\n" +
            "    \"format\" : \"video/mpeg4\",\n" +
            "    \"duration\" : 180000000000000000,\n" +
            "    \"size\" : 58982400,\n" +
            "    \"copyright\" : \"None\",\n" +
            "    \"persons\" : [ \"Bill Gates\", \"Steve Jobs\" ]\n" +
            "  },\n" +
            "  \"images\" : [ {\n" +
            "    \"uri\" : \"http://javaone.com/keynote_large.jpg\",\n" +
            "    \"title\" : \"Javaone Keynote\",\n" +
            "    \"width\" : 1024,\n" +
            "    \"height\" : 768,\n" +
            "    \"size\" : \"LARGE\"\n" +
            "  }, {\n" +
            "    \"uri\" : \"http://javaone.com/keynote_small.jpg\",\n" +
            "    \"title\" : \"Javaone Keynote\",\n" +
            "    \"width\" : 320,\n" +
            "    \"height\" : 240,\n" +
            "    \"size\" : \"SMALL\"\n" +
            "  } ]\n" +
            "}";
}

package com.vshatrov.bench;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.vshatrov.deserializers.ComplexDeserializer;
import com.vshatrov.deserializers.MediaItemDeserializer;
import com.vshatrov.deserializers.PojoDeserializer;
import com.vshatrov.entities.Complex;
import com.vshatrov.entities.Pojo;
import com.vshatrov.entities.media.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Viktor Shatrov.
 */
@State(Scope.Thread)
public class Deserialization {

    private ObjectMapper mapper;
    private MappingJsonFactory factory;
    private String pojo;
    private String complex;
    private String mediaItem;


    @Param({"afterBurner", "handWritten", "reflection", "processor"})
    String method;

    @Param({"true", "false"})
    boolean orderedProperties;

    public Deserialization() {
        factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    @Setup(Level.Iteration)
    public void setup() {
        mapper = new ObjectMapper(factory);
        SimpleModule handModule = new SimpleModule();
        handModule.addDeserializer(MediaItem.class, new MediaItemDeserializer());
        handModule.addDeserializer(Pojo.class, new PojoDeserializer());
        handModule.addDeserializer(Complex.class, new ComplexDeserializer());
        SimpleModule procModule = new SimpleModule();
        //procModule.addDeserializer(MediaItem.class, new com.vshatrov.entities.media.MediaItemDeserializer());
        //procModule.addDeserializer(Image.class, new ImageDeserializer());
        //procModule.addDeserializer(Media.class, new MediaDeserializer());
        //procModule.addDeserializer(Pojo.class, new com.vshatrov.entities.PojoDeserializer());
        //procModule.addDeserializer(Complex.class, new com.vshatrov.entities.ComplexDeserializer());

        switch (method) {
            case "afterBurner": mapper.registerModule(new AfterburnerModule());
                break;
            case "handWritten": mapper.registerModule(handModule); break;
            case "processor": mapper.registerModule(procModule); break;
            default:
        }

        pojo = orderedProperties ? pojo_json_ord : pojo_json_unord;
        complex = orderedProperties ? complex_json_ord : complex_json_unord;
        mediaItem = orderedProperties ? media_json_ord : media_json_unord;
    }

   /* @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Pojo pojo() throws IOException {
        return mapper.readValue(pojo, Pojo.class);
    }*/

   /* @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Complex complex() throws IOException {
        return mapper.readValue(complex, Complex.class);
    }*/

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public MediaItem media() throws IOException {
        return mapper.readValue(mediaItem, MediaItem.class);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Deserialization.class.getSimpleName())
                .warmupIterations(4)
                .measurementIterations(12)
                .output("profile.txt")
                .forks(1)
                .build();

        new Runner(options).run();
    }


    private final String pojo_json_ord = "{\n" +
            "    \"i1\" : -1620089756,\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"Ilist\" : [ 1634586975, -1165621910, 142149694 ],\n" +
            "    \"bool\" : false,\n" +
            "    \"aDouble\" : 0.054497929429155656,\n" +
            "    \"prInt\" : 856158868,\n" +
            "    \"aChar\" : \"a\"\n" +
            "  }";

    private final String pojo_json_unord = "{\n" +
            "    \"aDouble\" : 0.2859571260997221,\n" +
            "    \"i1\" : -925158517,\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"Ilist\" : [ 1089056773, -748283615, 1606668356 ],\n" +
            "    \"bool\" : false,\n" +
            "    \"prInt\" : -842678340,\n" +
            "    \"aChar\" : \"a\"\n" +
            "  }";

    private final String complex_json_ord = "{\n" +
            "  \"name\" : \"complex\",\n" +
            "  \"num\" : 3,\n" +
            "  \"pojos\" : [ {\n" +
            "    \"i1\" : -1620089756,\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"Ilist\" : [ 1634586975, -1165621910, 142149694 ],\n" +
            "    \"bool\" : false,\n" +
            "    \"aDouble\" : 0.054497929429155656,\n" +
            "    \"prInt\" : 856158868,\n" +
            "    \"aChar\" : \"a\"\n" +
            "  }, {\n" +
            "    \"i1\" : -1298516356,\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"Ilist\" : [ 1237268752, 589497299, 982121578 ],\n" +
            "    \"bool\" : false,\n" +
            "    \"aDouble\" : 0.21233588174133644,\n" +
            "    \"prInt\" : 1306471947,\n" +
            "    \"aChar\" : \"a\"\n" +
            "  }, {\n" +
            "    \"i1\" : -678824366,\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"Ilist\" : [ 2001182228, 356022029, -1115526333 ],\n" +
            "    \"bool\" : false,\n" +
            "    \"aDouble\" : 0.22110820373728446,\n" +
            "    \"prInt\" : 1424888919,\n" +
            "    \"aChar\" : \"a\"\n" +
            "  } ]\n" +
            "}";

    private final String complex_json_unord = "{\n" +
            "  \"num\" : 3,\n" +
            "  \"name\" : \"complex\",\n" +
            "  \"pojos\" : [ {\n" +
            "    \"aChar\" : \"a\",\n" +
            "    \"i1\" : -925158517,\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"Ilist\" : [ 1089056773, -748283615, 1606668356 ],\n" +
            "    \"prInt\" : -842678340,\n" +
            "    \"bool\" : false,\n" +
            "    \"aDouble\" : 0.2859571260997221\n" +
            "  }, {\n" +
            "    \"aDouble\" : 0.48392402805399104,\n" +
            "    \"i1\" : -185981464,\n" +
            "    \"prInt\" : 1607198796,\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"bool\" : false,\n" +
            "    \"Ilist\" : [ 1320542450, -273370807, 1741761670 ],\n" +
            "    \"aChar\" : \"a\"\n" +
            "  }, {\n" +
            "    \"aDouble\" : 0.13098313080271007,\n" +
            "    \"i1\" : 1726344294,\n" +
            "    \"prInt\" : -1788360557,\n" +
            "    \"bool\" : false,\n" +
            "    \"Ilist\" : [ 37905116, -664660260, -1094483597 ],\n" +
            "    \"Str\" : \"pojo\",\n" +
            "    \"aChar\" : \"a\"\n" +
            "  } ]\n" +
            "}";

    private final String media_json_ord = "{\n" +
            "  \"media\" : {\n" +
            "    \"uri\" : \"http://javaone.com/keynote.mpg\",\n" +
            "    \"title\" : \"Javaone Keynote\",\n" +
            "    \"width\" : 640,\n" +
            "    \"height\" : 480,\n" +
            "    \"format\" : \"video/mpeg4\",\n" +
            "    \"duration\" : 180000000000000000,\n" +
            "    \"size\" : 58982400,\n" +
            "    \"bitrate\" : 262144,\n" +
            "    \"persons\" : [ \"Bill Gates\", \"Steve Jobs\" ],\n" +
            "    \"player\" : \"JAVA\",\n" +
            "    \"copyright\" : \"None\"\n" +
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

    private final String media_json_unord = "{\n" +
            "  \"images\" : [ {\n" +
            "    \"title\" : \"Javaone Keynote\",\n" +
            "    \"uri\" : \"http://javaone.com/keynote_large.jpg\",\n" +
            "    \"width\" : 1024,\n" +
            "    \"height\" : 768,\n" +
            "    \"size\" : \"LARGE\"\n" +
            "  }, {\n" +
            "    \"height\" : 240,\n" +
            "    \"uri\" : \"http://javaone.com/keynote_small.jpg\",\n" +
            "    \"title\" : \"Javaone Keynote\",\n" +
            "    \"width\" : 320,\n" +
            "    \"size\" : \"SMALL\"\n" +
            "  } ],\n" +
            "  \"media\" : {\n" +
            "    \"height\" : 480,\n" +
            "    \"width\" : 640,\n" +
            "    \"player\" : \"JAVA\",\n" +
            "    \"uri\" : \"http://javaone.com/keynote.mpg\",\n" +
            "    \"title\" : \"Javaone Keynote\",\n" +
            "    \"format\" : \"video/mpeg4\",\n" +
            "    \"bitrate\": 132,\n" +
            "    \"duration\" : 180000000000000000,\n" +
            "    \"size\" : 58982400,\n" +
            "    \"copyright\" : \"None\",\n" +
            "    \"persons\" : [ \"Bill Gates\", \"Steve Jobs\" ]\n" +
            "  }\n" +
            "}";
}

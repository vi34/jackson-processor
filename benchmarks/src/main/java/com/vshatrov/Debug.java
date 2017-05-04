package com.vshatrov;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;
import com.vshatrov.deserializers.*;
import com.vshatrov.deserializers.MediaItemDeserializer;
import com.vshatrov.entities.Pojo;
import com.vshatrov.entities.Complex;
import com.vshatrov.entities.media.*;
import com.vshatrov.serializers.ComplexSerializer;
import com.vshatrov.serializers.PojoSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Viktor Shatrov
 */

public class Debug {

    private static File testFile = new File("test.json");

    public static void main(String[] args) throws IOException {
        BufferedWriter writer = Files.newWriter(testFile, Charset.defaultCharset());
        MappingJsonFactory factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        SimpleModule module = new SimpleModule();
        //module.addSerializer(Pojo.class, new com.vshatrov.entities.PojoSerializer());
        //module.addSerializer(Complex.class, new com.vshatrov.entities.ComplexSerializer());
        //module.addSerializer(MediaItem.class, new MediaItemSerializer());
        //module.addDeserializer(Pojo.class, new com.vshatrov.entities.PojoDeserializer());
        //module.addDeserializer(Complex.class, new com.vshatrov.entities.ComplexDeserializer());
        module.addSerializer(Complex.class, new ComplexSerializer());
        module.addDeserializer(MediaItem.class, new MediaItemDeserializer());
       // module.addDeserializer(Image.class, new ImageDeserializer());
        //module.addDeserializer(Media.class, new MediaDeserializer());
        //module.addDeserializer(Pojo.class, new com.vshatrov.entities.PojoDeserializer());
        mapper.registerModule(module);

      /*  Pojo pojo = Pojo.makePojo();

        String s = mapper.writeValueAsString(pojo);
        System.out.println(s);

        Pojo read = mapper.readValue(s, Pojo.class);
        System.out.println(read.equals(pojo));*/
        Complex complex = Complex.makeComplex(3);

        String s = mapper.writeValueAsString(complex);
        System.out.println(s);

        Complex read = mapper.readValue(s, Complex.class);
        System.out.println(read.equals(complex));
/*
        MediaItem mediaItem = MediaItem.buildItem();

        String s = mapper.writeValueAsString(mediaItem);
        System.out.println(s);

        MediaItem read = mapper.readValue(s, MediaItem.class);
        System.out.println(read.equals(mediaItem));*/

    }
}

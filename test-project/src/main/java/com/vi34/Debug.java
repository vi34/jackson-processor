package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;
import com.vi34.deserializers.PojoDeserializer;
import com.vi34.entities.Pojo;
import com.vi34.entities.Complex;
import com.vi34.serializers.ComplexSerializer;
import com.vi34.serializers.PojoSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by vi34 on 13/08/16.
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
        module.addSerializer(Pojo.class, new PojoSerializer());
        module.addSerializer(Complex.class, new ComplexSerializer());
        module.addDeserializer(Pojo.class, new PojoDeserializer());
        mapper.registerModule(module);

        Pojo pojo = Pojo.makePojo();

        String s = mapper.writeValueAsString(pojo);
        System.out.println(s);

        Pojo read = mapper.readValue(s, Pojo.class);
        System.out.println(read.equals(pojo));


    }
}

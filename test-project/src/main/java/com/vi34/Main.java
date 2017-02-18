package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;
import com.vi34.annotations.Json;
import com.vi34.entities.Pojo;
import com.vi34.entities.Pojo2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by vi34 on 13/08/16.
 */

public class Main {

    private static File testFile = new File("test.json");

    public static void main(String[] args) throws IOException {
        BufferedWriter writer = Files.newWriter(testFile, Charset.defaultCharset());
        MappingJsonFactory factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Pojo.class, new PojoSerializer());
        mapper.registerModule(module);


        Pojo pojo = new Pojo(1, "test", Arrays.asList(3, 4 ,5 ,1), false, 3.1, 999, 'a');
        mapper.writeValue(System.out, pojo);

        Pojo2 pojo2 = new Pojo2(2, "test2", Arrays.asList(3, 4, 5, 1), false, 3.1, 999, 'a');
        mapper.writeValue(System.out, pojo2);

        mapper.writeValue(writer, pojo);
        mapper.writeValue(writer, pojo2);

        //Pojo pojo2 = mapper.readValue(Files.newReader(testFile, Charset.defaultCharset()), Pojo.class);
        //System.out.println(pojo2);

    }
}

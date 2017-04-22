package com.vshatrov;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;
import com.vshatrov.prototypes.ArrayDeserializer;
import com.vshatrov.prototypes.PrimitiveArrayDeser;
import com.vshatrov.raw.Array;
import com.vshatrov.raw.Enums;
import com.vshatrov.raw.Pojo;
import com.vshatrov.raw.PrimitiveArray;
import org.junit.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static com.vshatrov.Compilation.loadDeserializer;

/**
 * @author Viktor Shatrov.
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
        module.addDeserializer(Array.class, new ArrayDeserializer());
        mapper.registerModule(module);

        int[] ints = {1, 5, 10, 2, 9, 8, 1, 1, 3};
        Pojo[] pojos = {new Pojo(1, 3.2), new Pojo(2, 5.2), new Pojo(3, 4.2)};
        Enums.En[] ens = {Enums.En.ONE, Enums.En.TWO, Enums.En.THREE};

        Array val = new Array(ints, pojos, Arrays.asList(1 ,2,5), Arrays.asList(new Pojo(5, 6)), ens);

        String json = mapper.writeValueAsString(val);
        Array read = mapper.readValue(json, Array.class);
        Assert.assertEquals(val, read);

    }

}

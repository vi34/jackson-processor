package com.vshatrov;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;
import com.vshatrov.prototypes.*;
import com.vshatrov.prototypes.ArrayDeserializer;
import com.vshatrov.prototypes.MapsDeserializer;
import com.vshatrov.raw.*;
import org.junit.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
        module.addDeserializer(Maps.class, new MapsDeserializer());
        mapper.registerModule(module);

        Map<String, String> props = new HashMap<>();
        props.put("a", "1");
        props.put("b", "2");
        props.put("c", "3");

        HashMap<Integer, String> h = new HashMap<>();
        h.put(1, "abc");
        h.put(2, "cde");
        h.put(3, "efg");

        TreeMap<String, Pojo> t = new TreeMap<>();
        t.put("pojo", new Pojo(1, 0.4));
        t.put("pojo2", new Pojo(2, 0.6));

        Maps val = new Maps(props, h, t);


        String json = mapper.writeValueAsString(val);
        Maps read = mapper.readValue(json, Maps.class);
        Assert.assertEquals(val, read);

    }

}

package com.vshatrov;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vshatrov.raw.*;
import com.vshatrov.raw.annotations.Renaming;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.vshatrov.Compilation.loadDeserializer;
import static com.vshatrov.Compilation.loadSerializer;

/**
 * @author Viktor Shatrov.
 */
public class AnnotationsTest {

    private static MappingJsonFactory factory = new MappingJsonFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUp() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        Compilation.classLoader = new URLClassLoader(new URL[]{Compilation.targetFile.toURI().toURL()});
        Compilation.compile(Paths.get("./src/main/test/com/vshatrov/raw/annotations").toFile().listFiles());
    }

    @Before
    public void cleanModules() {
        mapper = new ObjectMapper(factory);
    }


    @Test
    public void renaming_serialization() throws IOException {
        Assert.assertTrue(loadSerializer(Renaming.class, mapper));
        renaming();
    }

    @Test
    public void renaming_deserialization() throws IOException {
        Assert.assertTrue(loadDeserializer(Renaming.class, mapper));
        renaming();
    }

    private void renaming() throws IOException {
        Renaming renaming = new Renaming(4, 12, "George");
        check(renaming, Renaming.class);
    }


    private <T> void check(T val, Class<T> clazz) throws IOException {
        String json = mapper.writeValueAsString(val);
        T read = mapper.readValue(json, clazz);
        Assert.assertEquals(val, read);
    }

}

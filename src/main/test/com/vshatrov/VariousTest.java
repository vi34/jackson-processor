package com.vshatrov;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vshatrov.processor.APTModule;
import com.vshatrov.raw.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Viktor Shatrov.
 */
public class VariousTest {

    private static MappingJsonFactory factory = new MappingJsonFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUp() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Compilation.compileDir(Paths.get("./src/main/test/com/vshatrov/raw/"));
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT);
        APTModule module = new APTModule();
        module.setClassLoader(Compilation.classLoader);
        mapper.registerModule(module);
    }


    @Test
    public void simple_polymorphism() throws IOException {
        Animal animal = Cat.make();
        round_trip_check(animal, Animal.class);

        animal = Dog.make();
        round_trip_check(animal, Animal.class);

        List<Animal> animalList = Arrays.asList(Cat.make(), Dog.make(), Dog.make(), Cat.make());
        round_trip_check(animalList, List.class);
    }


    private <T> void round_trip_check(T val, Class<T> clazz) throws IOException {
        String json = mapper.writeValueAsString(val);
        T read = mapper.readValue(json, clazz);
        Assert.assertEquals(val, read);
    }

}

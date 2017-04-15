package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vi34.raw.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.vi34.Compilation.load;

/**
 * Created by vi34 on 18/02/2017.
 */
public class SerializationTest {

    private static MappingJsonFactory factory = new MappingJsonFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUp() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        Compilation.classLoader = new URLClassLoader(new URL[]{Compilation.targetFile.toURI().toURL()});
        Compilation.compile(Paths.get("./src/main/test/com/vi34/raw/").toFile().listFiles());
    }

    @Test
    public void withString() throws IOException {
        Assert.assertTrue(load(WithString.class, mapper));

        WithString val = new WithString(1, 1.2, "str", "name");

        check(val, WithString.class);
    }


    @Test
    public void pojo() throws IOException {
        Assert.assertTrue(load(Pojo.class, mapper));

        Pojo val = new Pojo(13, 0.4);

        check(val, Pojo.class);
    }

    @Test
    public void complex() throws IOException {
        Assert.assertTrue(load(Complex.class, mapper));

        Complex val = new Complex(1, new Pojo(1, 2));

        check(val, Complex.class);
    }

    @Test
    public void boxing() throws IOException {
        Assert.assertTrue(load(Boxing.class, mapper));

        Boxing val = new Boxing(1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true,
                1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true);

        check(val, Boxing.class);
    }

    @Test
    public void getters() throws IOException {
        Assert.assertTrue(load(Getters.class, mapper));

        Getters val = new Getters(1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true,
                1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true);

        check(val, Getters.class);
    }

    @Test
    public void arrayAndList() throws IOException {
        Assert.assertTrue(load(Array.class, mapper));
        int[] ints = {1, 5, 10, 2, 9, 8, 1, 1, 3};
        Pojo[] pojos = {new Pojo(1, 3.2), new Pojo(2, 5.2), new Pojo(3, 4.2)};
        Array val = new Array(ints, pojos, Arrays.asList(1 ,2,5), Arrays.asList(new Pojo(5, 6)));

        check(val, Array.class);
    }

    @Test
    public void enums() throws IOException {
        Assert.assertTrue(load(Enums.class, mapper));

        Enums val = new Enums(Enums.En.TWO);

        check(val, Enums.class);
    }

    @Test
    public void resolveUnknown() throws IOException {
        Assert.assertTrue(load(Resolve.class, mapper));

        Resolve val = new Resolve("resolve", new UnknownClass(999, "unknown"));

        check(val, Resolve.class);
    }



    private <T> void check(T val, Class<T> clazz) throws IOException {
        String json = mapper.writeValueAsString(val);
        T read = mapper.readValue(json, clazz);
        Assert.assertEquals(val, read);
    }

}
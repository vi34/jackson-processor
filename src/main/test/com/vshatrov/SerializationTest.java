package com.vshatrov;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vshatrov.raw.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.*;

import static com.vshatrov.Compilation.loadSerializer;

/**
 *
 * Round-trip tests to check serialization uses standard Jackson deserializer.
 *
 * Common test:
 * 1) test data serialized to json
 * 2) json deserialized to object
 * 3) result compared with initial object
 *
 * @author Viktor Shatrov.
 */
public class SerializationTest {

    private static MappingJsonFactory factory = new MappingJsonFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUp() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        Compilation.compileDir(Paths.get("./src/main/test/com/vshatrov/raw/"));
    }

    @Test
    public void withString() throws IOException {
        Assert.assertTrue(loadSerializer(WithString.class, mapper));

        WithString val = new WithString(1, 1.2, "str", "name");

        round_trip_check(val, WithString.class);
    }


    @Test
    public void pojo() throws IOException {
        Assert.assertTrue(loadSerializer(Pojo.class, mapper));

        Pojo val = new Pojo(13, 0.4);

        round_trip_check(val, Pojo.class);
    }

    @Test
    public void complex() throws IOException {
        Assert.assertTrue(loadSerializer(Complex.class, mapper));

        Complex val = Complex.make();

        round_trip_check(val, Complex.class);
    }

    @Test
    public void boxing() throws IOException {
        Assert.assertTrue(loadSerializer(Boxing.class, mapper));

        Boxing val = new Boxing(1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true,
                1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true);

        round_trip_check(val, Boxing.class);
    }

    @Test
    public void getters() throws IOException {
        Assert.assertTrue(loadSerializer(Accessors.class, mapper));

        Accessors val = new Accessors(1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true,
                1, 2.0, 'c', 321312421441241441L, 0.3f, (short) 11, (byte) 120, true);

        round_trip_check(val, Accessors.class);
    }

    @Test
    public void arrayAndList() throws IOException {
        Assert.assertTrue(loadSerializer(Array.class, mapper));
        int[] ints = {1, 5, 10, 2, 9, 8, 1, 1, 3};
        Pojo[] pojos = {new Pojo(1, 3.2), new Pojo(2, 5.2), new Pojo(3, 4.2)};
        Enums.En[] ens = {Enums.En.ONE, Enums.En.TWO, Enums.En.THREE};

        Array val = new Array(ints, pojos, Arrays.asList(1 ,2,5), Arrays.asList(new Pojo(5, 6)), ens);

        round_trip_check(val, Array.class);
    }

    @Test
    public void enums() throws IOException {
        Assert.assertTrue(loadSerializer(Enums.class, mapper));

        Enums val = new Enums(Enums.En.TWO);

        round_trip_check(val, Enums.class);
    }

    @Test
    public void resolveUnknown() throws IOException {
        Assert.assertTrue(loadSerializer(Resolve.class, mapper));

        Resolve val = Resolve.make();

        round_trip_check(val, Resolve.class);
    }

    @Test
    public void resolveNull() throws IOException {
        Assert.assertTrue(loadSerializer(Resolve.class, mapper));

        Resolve val = null;

        round_trip_check(val, Resolve.class);

        val = new Resolve("resolve", null, null);
        round_trip_check(val, Resolve.class);

        Assert.assertTrue(loadSerializer(Complex.class, mapper));

        Complex c = new Complex(3, null);
        round_trip_check(c, Complex.class);

        Assert.assertTrue(loadSerializer(Array.class, mapper));

        Pojo[] pojos = {new Pojo(1, 0.2), null};
        Array arr = new Array(null, pojos, null, Arrays.asList(pojos), null);
        round_trip_check(arr, Array.class);

    }

    @Test
    public void statics() throws IOException {
        Assert.assertTrue(loadSerializer(Static.class, mapper));

        Static val = new Static(1, 2);

        round_trip_check(val, Static.class);
    }


    @Test
    public void maps() throws IOException {
        Assert.assertTrue(loadSerializer(Maps.class, mapper));

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
        round_trip_check(val, Maps.class);
    }


    @Test
    public void complex_generic_structures() throws IOException{
        Assert.assertTrue(loadSerializer(ComplexStructures.class, mapper));

        ComplexStructures val = ComplexStructures.make();
        round_trip_check(val, ComplexStructures.class);
    }

    /*@Test
    public void keyObjectMaps() throws IOException {
         Assert.assertTrue(loadSerializer(KeyObjectMap.class, mapper));

        Map<Pojo, Pojo> kobj = new HashMap<>();
        kobj.put(new Pojo(1, 3.2), new Pojo(2, 0.4));
        kobj.put(new Pojo(1, 332.21), new Pojo(2, 0.6));

        KeyObjectMap val = new KeyObjectMap(kobj);
        round_trip_check(val, KeyObjectMap.class);
    }*/


    /**
     * Checks serialization correctness by deserializing result with standard Jackson
     */
    private <T> void round_trip_check(T val, Class<T> clazz) throws IOException {
        String json = mapper.writeValueAsString(val);
        T read = mapper.readValue(json, clazz);
        Assert.assertEquals(val, read);
    }

}

package com.vshatrov;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vshatrov.prototypes.ObjectTypeChangeDeserializer;
import com.vshatrov.raw.*;
import com.vshatrov.raw.annotations.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

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

    @Test
    public void alternatives_properNames() throws IOException {
        Assert.assertTrue(loadDeserializer(Alternatives.class, mapper));
        String ordinary_json = "{\"name\": \"Alex\", \"age\" : 1, \"pojo\": {\"i1\": 1, \"a2\": 2}}";
        String name_json = "{\"fullname\": \"Alex\", \"age\" : 1, \"pojo\": {\"i1\": 1, \"a2\": 2}}";
        String other_json = "{\"Name\": \"Alex\", \"old\" : 1, \"Pojjo\": {\"i1\": 1, \"a2\": 2}}";
        String other2_json = "{\"nAme\": \"Alex\", \"old\" : 1, \"Pojjo\": {\"i1\": 1, \"a2\": 2}}";

        Alternatives alternatives = new Alternatives("Alex", 1, new Pojo(1, 2));

        Alternatives read = mapper.readValue(ordinary_json, Alternatives.class);
        Assert.assertEquals(alternatives, read);

        read = mapper.readValue(name_json, Alternatives.class);
        Assert.assertEquals(alternatives, read);

        read = mapper.readValue(other_json, Alternatives.class);
        Assert.assertEquals(alternatives, read);

        read = mapper.readValue(other2_json, Alternatives.class);
        Assert.assertEquals(alternatives, read);

    }

    @Test
    public void ordering() throws IOException {
        Assert.assertTrue(loadSerializer(Order.class, mapper));
        Order order = new Order();
        order.setI1(1);
        order.setI2(2);
        order.setIgnored(999);
        order.i3 = 3;
        order.i4 = 4;

        String s = mapper.writeValueAsString(order);
        String[] props = s.split(",");
        for (Integer i = 1; i < props.length + 1; i++) {
            Assert.assertTrue(props[i - 1].contains(i.toString()));
        }
    }


    @Test
    public void primitive_type_change() throws IOException {
        Assert.assertTrue(loadDeserializer(PrimitiveTypeChange.class, mapper));

        String ordinary_json = "{\"name\": \"new\", \"pojo\": {\"i1\": 1, \"a2\": 2}}";
        String old_json = "{\"name\": \"old\", \"pojo\": 13 }";

        PrimitiveTypeChange source  = new PrimitiveTypeChange("new", new Pojo(1, 2));

        PrimitiveTypeChange read = mapper.readValue(ordinary_json, PrimitiveTypeChange.class);
        Assert.assertEquals(source, read);

        source  = new PrimitiveTypeChange("old", new Pojo(13, 0));

        read = mapper.readValue(old_json, PrimitiveTypeChange.class);
        Assert.assertEquals(source, read);

    }

    @Test
    public void object_type_change() throws IOException {
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT);
        //  Assert.assertTrue(loadDeserializer(ObjectTypeChange.class, mapper));

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ObjectTypeChange.class, new ObjectTypeChangeDeserializer());
        mapper.registerModule(module);

        ObjectTypeChange source = new ObjectTypeChange("new", new Complex(-1, new Pojo(2, 3.5)));

        ObjectTypeChange read = mapper.readValue(ordinary_object, ObjectTypeChange.class);
        Assert.assertEquals(source, read);

        source  = new ObjectTypeChange("old", new Complex(0, new Pojo(2, 3.5)));

        read = mapper.readValue(old_object, ObjectTypeChange.class);
        Assert.assertEquals(source, read);

    }

    private String old_object = "{\n" +
            "  \"com.vshatrov.raw.annotations.ObjectTypeChange\" : {\n" +
            "    \"name\" : \"old\",\n" +
            "    \"object\" : {\n" +
            "      \"com.vshatrov.raw.Pojo\" : {\n" +
            "        \"i1\" : 2,\n" +
            "        \"a2\" : 3.5\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private String ordinary_object = "{\n" +
            "  \"com.vshatrov.raw.annotations.ObjectTypeChange\" : {\n" +
            "    \"name\" : \"new\",\n" +
            "    \"object\" : {\n" +
            "      \"com.vshatrov.raw.Complex\" : {\n" +
            "        \"i1\" : -1,\n" +
            "        \"pojo\" : {\n" +
            "          \"com.vshatrov.raw.Pojo\" : {\n" +
            "            \"i1\" : 2,\n" +
            "            \"a2\" : 3.5\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";




    private <T> void check(T val, Class<T> clazz) throws IOException {
        String json = mapper.writeValueAsString(val);
        T read = mapper.readValue(json, clazz);
        Assert.assertEquals(val, read);
    }

}

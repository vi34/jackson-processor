package com.vshatrov;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.vshatrov.raw.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.vshatrov.Compilation.loadSerializer;

/**
 * @author Viktor Shatrov.
 */
public class SchemaTest {

    String schemaDir = "./json-schema";


    private static MappingJsonFactory factory = new MappingJsonFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);
    private static JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();


    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUp() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        Compilation.compileDir(Paths.get("./src/main/test/com/vshatrov/raw/"));
    }

    @Test
    public void schema_and_generators_conformity() throws IOException, ProcessingException {
        Assert.assertTrue(loadSerializer(Pojo.class,  mapper));
        Assert.assertTrue(loadSerializer(Complex.class, mapper));

        JsonSchema jsonSchema = schemaFactory.getJsonSchema(Paths.get(schemaDir, "Pojo-schema.json").toUri().toString());

        Pojo sample = new Pojo(1, 0.44);
        JsonNode jsonNode = mapper.valueToTree(sample);

        ProcessingReport re = jsonSchema.validate(jsonNode);
        if (!re.isSuccess()) System.out.println(re);
        Assert.assertTrue(re.isSuccess());

        jsonSchema = schemaFactory.getJsonSchema(Paths.get(schemaDir, "Complex-schema.json").toUri().toString());

        Complex val = new Complex(12, sample);
        jsonNode = mapper.valueToTree(val);

        re = jsonSchema.validate(jsonNode);
        if (!re.isSuccess()) System.out.println(re);
        Assert.assertTrue(re.isSuccess());


        jsonSchema = schemaFactory.getJsonSchema(Paths.get(schemaDir, "Array-schema.json").toUri().toString());

        int[] ints = {1, 5, 10, 2, 9, 8, 1, 1, 3};
        Pojo[] pojos = {new Pojo(1, 3.2), new Pojo(2, 5.2), new Pojo(3, 4.2)};
        Enums.En[] ens = {Enums.En.ONE, Enums.En.TWO, Enums.En.THREE};

        Collections arr = new Collections();
        arr.ints = ints;
        arr.pojos = pojos;
        arr.lInts = Arrays.asList(1, 2, 5);
        arr.lPojos = Arrays.asList(new Pojo(5, 6));
        arr.ens = ens;
        jsonNode = mapper.valueToTree(arr);

        re = jsonSchema.validate(jsonNode);
        if (!re.isSuccess()) System.out.println(re);
        Assert.assertTrue(re.isSuccess());
    }


    @Test
    public void maps() throws IOException, ProcessingException {
        Assert.assertTrue(loadSerializer(Maps.class,  mapper));

        JsonSchema jsonSchema = schemaFactory.getJsonSchema(Paths.get(schemaDir, "Maps-schema.json").toUri().toString());

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

        Maps maps = new Maps(props, h, t);
        JsonNode jsonNode = mapper.valueToTree(maps);


        ProcessingReport re = jsonSchema.validate(jsonNode);
        if (!re.isSuccess()) System.out.println(re);
        Assert.assertTrue(re.isSuccess());
    }


}

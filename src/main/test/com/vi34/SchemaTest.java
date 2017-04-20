package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.vi34.raw.Array;
import com.vi34.raw.Complex;
import com.vi34.raw.Enums;
import com.vi34.raw.Pojo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.vi34.Compilation.load;

/**
 * Created by vi34 on 18/02/2017.
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
        Compilation.classLoader = new URLClassLoader(new URL[]{Compilation.targetFile.toURI().toURL()});
        Compilation.compile(Paths.get("./src/main/test/com/vi34/raw/").toFile().listFiles());
    }

    @Test
    public void schema_and_generators_conformity() throws IOException, ProcessingException {
        Assert.assertTrue(load(Pojo.class,  mapper));
        Assert.assertTrue(load(Complex.class, mapper));

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

        Array arr = new Array(ints, pojos, Arrays.asList(1 ,2,5), Arrays.asList(new Pojo(5, 6)), ens);
        jsonNode = mapper.valueToTree(arr);

        re = jsonSchema.validate(jsonNode);
        if (!re.isSuccess()) System.out.println(re);
        Assert.assertTrue(re.isSuccess());
    }

}

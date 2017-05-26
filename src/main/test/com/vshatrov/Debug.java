package com.vshatrov;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;
import com.vshatrov.prototypes.*;
import com.vshatrov.raw.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Paths;

/**
 * @author Viktor Shatrov.
 */

public class Debug {

    private static File testFile = new File("test.json");

    public static void main(String[] args) throws IOException {
        Compilation.classLoader = new URLClassLoader(new URL[]{Compilation.targetFile.toURI().toURL()});
        Compilation.compileDir(Paths.get("./src/main/test/com/vshatrov/raw/"));
        BufferedWriter writer = Files.newWriter(testFile, Charset.defaultCharset());
        MappingJsonFactory factory = new MappingJsonFactory();
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Pojo.class, new PojoTypedSerializer());
        mapper.registerModule(module);
        //Compilation.loadDeserializer(Pojo.class, mapper);
        //Compilation.loadDeserializer(PolyPojo.class, mapper);
        //Compilation.loadDeserializer(Complex.class, mapper);

        mapper.writeValue(writer, Complex.make());

        Complex pojo = mapper.readValue(testFile, Complex.class);
        System.out.println(pojo);


    }

}

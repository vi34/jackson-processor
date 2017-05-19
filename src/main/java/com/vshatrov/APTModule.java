package com.vshatrov;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Shatrov.
 */
public class APTModule extends SimpleModule {

    @Override
    public String getModuleName() {
        return "Annotation Processor Module";
    }

    @Override
    public Version version() {
        return new Version(1, 0, 0, "", "com.vshatrov", "jackson-processor");
    }

    @Override
    public void setupModule(SetupContext context) {
        try {
            Files.lines(Paths.get("GeneratedSerializers.txt")).forEach(l -> {
                try {
                    String[] classes = l.split(":");
                    Class<?> ser = Class.forName(classes[0]);
                    Class<?> type = Class.forName(classes[1]);
                    addSerializer((JsonSerializer<?>) ser.newInstance());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            });

            Files.lines(Paths.get("GeneratedDeserializers.txt")).forEach(l -> {
                try {
                    String[] classes = l.split(":");
                    Class<?> deser = Class.forName(classes[0]);
                    Class<?> type = Class.forName(classes[1]);
                    JsonDeserializer<Object> jsonDeserializer = (JsonDeserializer<Object>) deser.newInstance();
                    addDeserializer((Class<Object>)jsonDeserializer.handledType(), jsonDeserializer);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setupModule(context);
    }
}

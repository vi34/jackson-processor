package com.vshatrov;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Optional module to register all generated serializers/deserializers in Jackson.
 * @author Viktor Shatrov.
 */
public class APTModule extends SimpleModule {

    private ClassLoader classLoader;

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

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
            Class<?> module = loadClass("com.vshatrov.generated.Module");
            Method serializers = module.getMethod("serializers");
            String[] sers = (String[]) serializers.invoke(null);
            for (String l : sers) {
                try {
                    String[] classes = l.split(":");
                    Class<?> ser = loadClass(classes[0]);
                    addSerializer((JsonSerializer<?>) ser.newInstance());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }

            Method deserializers = module.getMethod("deserializers");
            String[] desers = (String[]) deserializers.invoke(null);
            for (String l : desers) {
                try {
                    String[] classes = l.split(":");
                    Class<?> deser = loadClass(classes[0]);
                    JsonDeserializer<Object> jsonDeserializer = (JsonDeserializer<Object>) deser.newInstance();
                    addDeserializer((Class<Object>)jsonDeserializer.handledType(), jsonDeserializer);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        super.setupModule(context);
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        if (classLoader == null) {
            return Class.forName(className);
        } else {
            return classLoader.loadClass(className);
        }
    }
}

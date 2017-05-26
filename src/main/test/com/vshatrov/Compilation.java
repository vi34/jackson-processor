package com.vshatrov;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.tools.javac.util.List;
import com.vshatrov.generation.DeserializerGenerator;
import com.vshatrov.generation.SerializerGenerator;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Viktor Shatrov.
 */
public class Compilation {
    public static Random random = new Random();

    public static final String TARGET_DIR = "./target/generated/processor/";
    public static File targetFile = new File(TARGET_DIR);
    public static URLClassLoader classLoader;

    public static void compileDir(Path dir) throws IOException {
        compile(Files.list(dir)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .toArray(File[]::new));
    }

    /**
     * Compiles provided files with annotation processor.
     * @throws IOException
     */
    public static void compile(File... files) throws IOException {
        deleteDir(targetFile);
        targetFile.mkdirs();
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector< JavaFileObject > diagnostics = new DiagnosticCollector<>();
        final StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null );
        final Iterable< ? extends JavaFileObject> sources = manager.getJavaFileObjectsFromFiles(Arrays.asList(files));
        manager.setLocation(StandardLocation.SOURCE_OUTPUT, Arrays.asList(targetFile));
        manager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(targetFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, List.of("-AAUTO_REGISTRATION=false"), null, sources);
        task.setProcessors(Arrays.asList(new JacksonProcessor()));
        System.out.println("Compilation succesful: " + task.call());
        for(final Diagnostic< ? extends JavaFileObject > diagnostic: diagnostics.getDiagnostics() ) {
            if (diagnostic.getSource() != null) {
                System.err.format("%s, line %d in %s",
                        diagnostic.getMessage(null),
                        diagnostic.getLineNumber(),
                        diagnostic.getSource().getName());
            } else {
                System.err.println(diagnostic.getMessage(null));
            }
        }
        manager.close();
        if (classLoader == null) {
            classLoader = new URLClassLoader(new URL[]{Compilation.targetFile.toURI().toURL()});
        }
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }


    /**
     * Finds and registers generated serializer in mapper for specified type.
     * @return true if serializer loaded succesfully
     */
    public static <T> boolean loadSerializer(Class<? extends T> type, ObjectMapper mapper) {
        try {
            String className = "com.vshatrov" + SerializerGenerator.PACKAGE_MODIFIER
                    + "." + type.getSimpleName()  + SerializerGenerator.SUFFIX;
            SimpleModule module = new SimpleModule();
            module.addSerializer(type, (JsonSerializer<T>) classLoader.loadClass(className).newInstance());
            mapper.registerModule(module);
            return true;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println(type.toString() + " load failed");
            return false;
        }
    }

    /**
     * Finds and registers generated deserializer in mapper for specified type.
     * @return true if serializer loaded succesfully
     */
    public static <T> boolean loadDeserializer(Class<T> type, ObjectMapper mapper) {
        try {
            String className = "com.vshatrov" + DeserializerGenerator.PACKAGE_MODIFIER
                                + "." + type.getSimpleName() + DeserializerGenerator.SUFFIX;
            SimpleModule module = new SimpleModule();
            module.addDeserializer(type, (JsonDeserializer<T>) classLoader.loadClass(className).newInstance());
            mapper.registerModule(module);
            return true;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println(type.toString() + " load failed");
            return false;
        }
    }
}

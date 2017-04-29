package com.vshatrov;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vshatrov.generation.DeserializerGenerator;
import com.vshatrov.generation.SerializerGenerator;
import org.apache.commons.io.FileUtils;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Viktor Shatrov.
 */
public class Compilation {

    public static final String TARGET_DIR = "./target/generated-sources/annotations/";
    public static File targetFile = new File(TARGET_DIR);
    static URLClassLoader classLoader;

    public static void compileDir(Path dir) throws IOException {
        compile(Files.list(dir)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .toArray(File[]::new));
    }

    public static void compile(File... files) throws IOException {
        FileUtils.deleteDirectory(targetFile);
        targetFile.mkdir();
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector< JavaFileObject > diagnostics = new DiagnosticCollector<>();
        final StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null );
        final Iterable< ? extends JavaFileObject> sources = manager.getJavaFileObjectsFromFiles(Arrays.asList(files));
        manager.setLocation(StandardLocation.SOURCE_OUTPUT,
                Arrays.asList(targetFile));
        manager.setLocation(StandardLocation.CLASS_OUTPUT,
                Arrays.asList(targetFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sources);
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
    }

    static <T> boolean loadSerializer(Class<? extends T> type, ObjectMapper mapper) {
        try {
            String className = type.getPackage().getName() + SerializerGenerator.PACKAGE_MODIFIER
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

    static <T> boolean loadDeserializer(Class<T> type, ObjectMapper mapper) {
        try {
            String className = type.getPackage().getName() + DeserializerGenerator.PACKAGE_MODIFIER
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

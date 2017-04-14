package com.vi34;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vi34.generation.SerializerGenerator;
import org.apache.commons.io.FileUtils;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * Created by vi34 on 18/02/2017.
 */
public class Compilation {

    public static final String TARGET_DIR = "./target/generated-sources/annotations/";
    public static File targetFile = new File(TARGET_DIR);
    static URLClassLoader classLoader;

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
        //Arrays.asList(new File("./src/main/test/generated")));
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

    static <T> boolean load(Class<? extends T> type, ObjectMapper mapper) {
        try {
            String className = "com.vi34." + type.getSimpleName() + SerializerGenerator.SUFFIX;
            SimpleModule module = new SimpleModule();
            module.addSerializer(type ,(StdSerializer<T>) classLoader.loadClass(className).newInstance());
            mapper.registerModule(module);
            return true;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println(type.toString() + " load failed");
            return false;
        }
    }
}

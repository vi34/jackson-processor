package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vi34.raw.Complex;
import com.vi34.raw.Pojo;
import com.vi34.raw.WithString;
import org.junit.jupiter.api.Assertions;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by vi34 on 18/02/2017.
 */
public class Compilation {

    public static final String TARGET_DIR = "./target/generated-sources/annotations/";
    private static File targetFile = new File(TARGET_DIR);
    static MappingJsonFactory factory = new MappingJsonFactory();
    static ObjectMapper mapper = new ObjectMapper(factory);
    static URLClassLoader classLoader;
    private static SimpleModule module;


    public static void compile(File... files) throws IOException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector< JavaFileObject > diagnostics = new DiagnosticCollector<>();
        final StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null );
        final Iterable< ? extends JavaFileObject> sources = manager.getJavaFileObjectsFromFiles(Arrays.asList(files));
        classLoader = new URLClassLoader(new URL[]{targetFile.toURI().toURL()});
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

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            setUp();
            pojo();
            complex();
            withString();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @SuppressWarnings("unchecked")
    public static void setUp() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        compile(Paths.get("./src/main/test/com/vi34/raw/").toFile().listFiles());
        module = new SimpleModule();
        //load("com.vi34.PojoSerializer", module, Pojo.class);
        mapper.registerModule(module);
    }

    private static <T> boolean load(String className, Class<? extends T> type) {
        try {
            SimpleModule module = new SimpleModule();
            module.addSerializer(type ,(StdSerializer<T>) classLoader.loadClass(className).newInstance());
            mapper.registerModule(module);
            return true;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println(type.toString() + " load failed");
            return false;
        }
    }

    public static void withString() throws IOException {
        if (!load("com.vi34.WithStringSerializer", WithString.class)) {
            return;
        }
        WithString val = new WithString(1,1.2, "str", "name");

        mapper.writeValue(new File("tmp"), val);
        WithString read = mapper.readValue(new File("tmp"), WithString.class);
        Assertions.assertEquals(val, read);

        System.out.println("WithString ok");
    }

    public static void pojo() throws IOException {
        if (!load("com.vi34.PojoSerializer", Pojo.class)) {
            return;
        }
        Pojo pojo = new Pojo(13, 0.4);

        mapper.writeValue(new File("tmp"), pojo);
        Pojo read = mapper.readValue(new File("tmp"), Pojo.class);
        Assertions.assertEquals(pojo, read);

        System.out.println("Pojo ok");
    }

    public static void complex() throws IOException {
        if (!load("com.vi34.ComplexSerializer", Complex.class)) {
            return;
        }
        Complex complex = new Complex(1, new Pojo(1, 2));

        mapper.writeValue(new File("tmp"), complex);
        Complex read = mapper.readValue(new File("tmp"), Complex.class);
        Assertions.assertEquals(complex, read);

        System.out.println("Complex ok");
    }
}

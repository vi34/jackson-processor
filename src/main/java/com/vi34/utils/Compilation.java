package com.vi34.utils;

import com.vi34.JacksonProcessor;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by vi34 on 18/02/2017.
 */
public class Compilation {
    public static void compile(File... files) throws IOException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector< JavaFileObject > diagnostics = new DiagnosticCollector<>();
        final StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null );
        final Iterable< ? extends JavaFileObject> sources = manager.getJavaFileObjectsFromFiles(Arrays.asList(files));
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sources);
        System.out.println("Compilation succesful: " + task.call());
        for( final Diagnostic< ? extends JavaFileObject > diagnostic: diagnostics.getDiagnostics() ) {
            System.out.format("%s, line %d in %s",
                    diagnostic.getMessage( null ),
                    diagnostic.getLineNumber(),
                    diagnostic.getSource().getName() );
        }

    }

    public static void main(String[] args) {
        try {
            compile(Paths.get("./src/test/java/Pojo2.java").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

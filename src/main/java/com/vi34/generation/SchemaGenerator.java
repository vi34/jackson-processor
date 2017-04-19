package com.vi34.generation;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vi34.beans.BeanDescription;
import com.vi34.beans.Property;
import com.vi34.schema.JsonSchema;
import com.vi34.schema.JsonType;
import com.vi34.utils.Utils;

import javax.annotation.processing.Filer;
import javax.tools.DocumentationTool;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;

/**
 * Created by vi34 on 19/04/2017.
 */
public class SchemaGenerator {

    private static MappingJsonFactory factory = new MappingJsonFactory();
    private static ObjectMapper mapper = new ObjectMapper(factory);
    Filer filer;

    public SchemaGenerator(Filer filer) {
        this.filer = filer;
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
    }


    public JsonSchema generateSchema(BeanDescription description) throws IOException {
        JsonSchema schema = new JsonSchema();
        schema.setType(JsonType.OBJECT);
        schema.setTitle(description.getTypeName());
        for (Property property : description.getProps()) {
            schema.addProp(property.getName(), createPropSchema(property));
        }
        writeSchema(schema, schemaFile(description.getTypeName()), description.getPackageName());
        return schema;
    }

    private JsonSchema createPropSchema(Property property) {
        JsonSchema schema = new JsonSchema();
        schema.set$schema(null);
        if (property.isSimple()) {
            schema.setType(property.getJsonType());
        } else {
            schema.set$ref(schemaFile(property.getTypeName()));
        }
        return schema;
    }


    private void writeSchema(JsonSchema schema, String fileName, String pkg) throws IOException {
        Files.createDirectories(Paths.get("./json-schema"));
        Path path = Paths.get("./json-schema/" + fileName);
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            Files.createFile(path);
        }
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
        //FileObject schemaFile = filer.createResource(StandardLocation.SOURCE_OUTPUT, pkg, fileName, null);
        mapper.writeValue(bufferedWriter, schema);
    }

    private String schemaFile(String typeName) {
        return Utils.qualifiedToSimple(typeName) + "-schema.json";
    }
}

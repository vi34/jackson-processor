package com.vi34.generation;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vi34.beans.*;
import com.vi34.schema.ArraySchema;
import com.vi34.schema.EnumSchema;
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
    public final String SCHEMA_DIR;

    public SchemaGenerator(String schemaDir) {
        SCHEMA_DIR = schemaDir;
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
        JsonSchema schema;
        if (property.isSimple()) {
            schema = new JsonSchema();
            schema.setType(property.getJsonType());
        } else if (property instanceof ContainerProp) {
            schema = new ArraySchema();
            schema.setType(JsonType.ARRAY);
            JsonSchema refSchema = createPropSchema(((ContainerProp) property).getElem());
            ((ArraySchema)schema).setItems(refSchema);
        } else if (property instanceof EnumProp) {
            schema = new EnumSchema();
        } else if (property instanceof MapProp) {
            schema = new JsonSchema();
            schema.setType(JsonType.OBJECT);
            schema.setAdditionalProperties(createPropSchema(((MapProp) property).getValue()));
        } else {
            schema = new JsonSchema();
            schema.set$ref(schemaFile(property.getTypeName()));
        }
        schema.set$schema(null);
        return schema;
    }


    private void writeSchema(JsonSchema schema, String fileName, String pkg) throws IOException {
        Files.createDirectories(Paths.get(SCHEMA_DIR));
        Path path = Paths.get(SCHEMA_DIR, fileName);
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
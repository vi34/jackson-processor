package com.vshatrov.processor.generation;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vshatrov.processor.type.*;
import com.vshatrov.processor.type.properties.ContainerProperty;
import com.vshatrov.processor.type.properties.EnumProperty;
import com.vshatrov.processor.type.properties.MapProperty;
import com.vshatrov.processor.type.properties.Property;
import com.vshatrov.processor.schema.ArraySchema;
import com.vshatrov.processor.schema.EnumSchema;
import com.vshatrov.processor.schema.JsonSchema;
import com.vshatrov.processor.schema.JsonType;
import com.vshatrov.processor.utils.Utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;

/**
 * @author Viktor Shatrov.
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
        } else if (property instanceof ContainerProperty) {
            schema = new ArraySchema();
            schema.setType(JsonType.ARRAY);
            JsonSchema refSchema = createPropSchema(((ContainerProperty) property).getElement());
            ((ArraySchema)schema).setItems(refSchema);
        } else if (property instanceof EnumProperty) {
            schema = new EnumSchema();
        } else if (property instanceof MapProperty) {
            schema = new JsonSchema();
            schema.setType(JsonType.OBJECT);
            schema.setAdditionalProperties(createPropSchema(((MapProperty) property).getValue()));
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

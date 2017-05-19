package com.vshatrov.schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Viktor Shatrov.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonSchema {

    public JsonSchema() {
        properties = new HashMap<>();
        required = new ArrayList<>();
    }

    String $schema = "http://json-schema.org/draft-04/schema#";

    String $ref;

    String title;

    String id;

    JsonType type;

    Map<String, JsonSchema> properties;

    List<String> required;

    JsonSchema additionalProperties;

    public void addProp(String name, JsonSchema schema) {
        properties.put(name, schema);
        //required.add(name);
    }


    public String get$schema() {
        return this.$schema;
    }

    public String get$ref() {
        return this.$ref;
    }

    public String getId() {
        return this.id;
    }

    public JsonType getType() {
        return this.type;
    }

    public Map<String, JsonSchema> getProperties() {
        return this.properties;
    }

    public List<String> getRequired() {
        return this.required;
    }

    public JsonSchema getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void set$schema(String $schema) {
        this.$schema = $schema;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(JsonType type) {
        this.type = type;
    }

    public void setAdditionalProperties(JsonSchema additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}

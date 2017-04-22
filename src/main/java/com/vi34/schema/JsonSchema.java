package com.vi34.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vi34 on 19/04/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JsonSchema {

    public JsonSchema() {
        properties = new HashMap<>();
        required = new ArrayList<>();
    }

    @Getter
    @Setter
    String $schema = "http://json-schema.org/draft-04/schema#";

    @Getter
    @Setter
    String $ref;

    @Setter
    String title;

    @Getter
    @Setter
    String id;

    @Setter
    @Getter
    JsonType type;

    @Getter
    Map<String, JsonSchema> properties;

    @Getter
    List<String> required;

    @Getter
    @Setter
    JsonSchema additionalProperties;

    public void addProp(String name, JsonSchema schema) {
        properties.put(name, schema);
        required.add(name);
    }


}

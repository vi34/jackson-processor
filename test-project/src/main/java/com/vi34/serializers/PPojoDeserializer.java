package com.vi34.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vi34.entities.PrivatePojo;

import java.io.IOException;

/**
 * Created by vi34 on 07/04/2017.
 */
public class PPojoDeserializer extends JsonDeserializer<PrivatePojo> {
    @Override
    public PrivatePojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode i1 = node.get("i1");

        return new PrivatePojo(i1.asInt(), null, null, false, null, 0, 'f');
    }
}

package com.vshatrov.prototypes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vshatrov.raw.PrimitiveArray;

import java.io.IOException;

/**
 * @author Viktor Shatrov.
 */
public class PrimitiveArrayDeser extends StdDeserializer<PrimitiveArray> implements ResolvableDeserializer {


    private JsonDeserializer<int[]> deserializer;

    protected PrimitiveArrayDeser(Class<?> vc) {
        super(vc);
    }

    public PrimitiveArrayDeser() {
        this(null);
    }

    @Override
    public PrimitiveArray deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        p.nextToken();
        p.nextToken();
        int[] arr = deserializer.deserialize(p, ctxt);

        return new PrimitiveArray(arr);
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        deserializer = (JsonDeserializer<int[]>) PrimitiveArrayDeserializers.forType(int.class);
    }
}

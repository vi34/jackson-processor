package com.vi34.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.vi34.serializers.PPojoDeserializer;

import java.io.IOException;

/**
 * Created by vi34 on 07/04/2017.
 */
@JsonDeserialize(using = PPojoDeserializer.class)
public class Prox implements JsonSerializable {
    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("i1", 12);
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("i1", 12);
        gen.writeEndObject();
    }
}

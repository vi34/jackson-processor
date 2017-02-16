package com.vi34;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vi34.entities.Pojo;

import java.io.IOException;

/**
 * Created by vi34 on 04/02/2017.
 */
public class PojoSerializer extends StdSerializer<Pojo> {

    public PojoSerializer() {
        this(null);
    }

    protected PojoSerializer(Class<Pojo> t) {
        super(t);
    }

    @Override
    public void serialize(Pojo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("a", value.getA());
        gen.writeStringField("s", value.getS());
        gen.writeEndObject();
    }
}

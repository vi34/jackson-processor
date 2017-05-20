package com.vshatrov.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vshatrov.model.Pojo;

import java.io.IOException;

/**
 * @author Viktor Shatrov.
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
        gen.writeNumberField("i1", value.getI1());
        gen.writeStringField("str", value.getStr());
        gen.writeArrayFieldStart("list");
        for (int v : value.getList()) {
            gen.writeNumber(v);
        }
        gen.writeEndArray();
        gen.writeBooleanField("bool", value.isBool());
        gen.writeNumberField("prInt", value.getPrInt());
        gen.writeStringField("aChar", value.getAChar() + "");
        gen.writeNumberField("aDouble", value.getADouble());
        gen.writeEndObject();
    }
}

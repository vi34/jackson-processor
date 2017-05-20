package com.vshatrov.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vshatrov.model.Complex;
import com.vshatrov.model.Pojo;

import java.io.IOException;

/**
 * @author Viktor Shatrov.
 */
public class ComplexSerializer extends StdSerializer<Complex> {

    public ComplexSerializer() {
        this(null);
    }

    protected ComplexSerializer(Class<Complex> t) {
        super(t);
    }

    @Override
    public void serialize(Complex value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("num", value.getNum());
        gen.writeStringField("name", value.getName());

        gen.writeArrayFieldStart("pojos");
        //JsonSerializer<Object> pojoSer = provider.findValueSerializer(Pojo.class);
        for (Pojo v : value.getPojos()) {
            //pojoSer.serialize(v, gen, provider);
            gen.writeStartObject();
            gen.writeNumberField("i1", v.getI1());
            gen.writeStringField("str", v.getStr());
            gen.writeArrayFieldStart("list");
            for (int val : v.getList()) {
                gen.writeNumber(val);
            }
            gen.writeEndArray();
            gen.writeBooleanField("bool", v.isBool());
            gen.writeNumberField("prInt", v.getPrInt());
            gen.writeStringField("aChar", ""+v.getAChar());
            gen.writeNumberField("aDouble", v.getADouble());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

}

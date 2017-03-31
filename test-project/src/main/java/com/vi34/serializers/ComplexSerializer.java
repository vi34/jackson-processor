package com.vi34.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vi34.entities.Complex;
import com.vi34.entities.Pojo;

import java.io.IOException;

/**
 * Created by vi34 on 04/02/2017.
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

        gen.writeArrayFieldStart("list");
//        JsonSerializer<Object> pojoSer =  provider.findValueSerializer(Pojo.class);
        for (Pojo v : value.getList()) {
//            pojoSer.serialize(v, gen, provider);
            gen.writeStartObject();
            gen.writeNumberField("i1", v.getI1());
            gen.writeStringField("s", v.getStr());
            gen.writeArrayFieldStart("Flist");
            for (int val : v.getList()) {
                gen.writeNumber(val);
            }
            gen.writeEndArray();
            gen.writeBooleanField("bool", v.isBool());
            gen.writeNumberField("prInt", v.getPrInt());
            gen.writeStringField("aChar", v.getAChar().toString());
            gen.writeNumberField("aDouble", v.getADouble());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}

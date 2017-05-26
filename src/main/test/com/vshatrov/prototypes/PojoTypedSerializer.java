package com.vshatrov.prototypes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vshatrov.raw.Pojo;

import java.io.IOException;

public final class PojoTypedSerializer extends StdSerializer<Pojo> implements ResolvableSerializer {
    public static final SerializedString FIELD_A2 = new SerializedString("a2");
    public static final SerializedString FIELD_I1 = new SerializedString("i1");

    protected PojoTypedSerializer(Class<Pojo> t) {
        super(t);
    }

    public PojoTypedSerializer() {
        this(Pojo.class);
    }

    @Override
    public void serialize(Pojo value, JsonGenerator gen, SerializerProvider provider) throws
            IOException {
        gen.writeStartObject();
        write_pojo(value, gen, provider);
        gen.writeEndObject();
    }

    public void write_pojo(Pojo value, JsonGenerator gen, SerializerProvider provider) throws
            IOException {
        gen.writeFieldName(FIELD_I1);
        gen.writeNumber(value.i1);
        gen.writeFieldName(FIELD_A2);
        gen.writeNumber(value.a2);
    }

    @Override
    public void serializeWithType(Pojo value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefixForObject(value, gen);
        write_pojo(value, gen, serializers);
        typeSer.writeTypeSuffixForObject(value, gen);
    }

    @Override
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        JavaType javaType;
        TypeFactory typeFactory = TypeFactory.defaultInstance();
    }
}

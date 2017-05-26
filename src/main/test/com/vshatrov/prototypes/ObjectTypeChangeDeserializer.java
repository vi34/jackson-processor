package com.vshatrov.prototypes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.vshatrov.raw.Complex;
import com.vshatrov.raw.Pojo;
import com.vshatrov.raw.annotations.ObjectTypeChange;

import java.io.IOException;
import java.util.HashMap;

public final class ObjectTypeChangeDeserializer extends StdDeserializer<ObjectTypeChange> implements ResolvableDeserializer {
    public static final HashMap<String, Integer> fullFieldToIndex = new HashMap<String, Integer>();

    public static final String FULL_FIELD_NAME = "name";

    public static final int IX_FULL_FIELD_NAME = 1;

    public static final SerializedString FIELD_NAME = new SerializedString(FULL_FIELD_NAME);

    public static final String FULL_FIELD_OBJECT = "object";

    public static final int IX_FULL_FIELD_OBJECT = 2;

    public static final SerializedString FIELD_OBJECT = new SerializedString(FULL_FIELD_OBJECT);

    static {
        fullFieldToIndex.put(FULL_FIELD_NAME, IX_FULL_FIELD_NAME);
        fullFieldToIndex.put(FULL_FIELD_OBJECT, IX_FULL_FIELD_OBJECT);
    }

    private JsonDeserializer<Object> pojo_deserializer;

    private JsonDeserializer<Object> complex_deserializer;

    protected ObjectTypeChangeDeserializer(Class<ObjectTypeChange> t) {
        super(t);
    }

    public ObjectTypeChangeDeserializer() {
        this(ObjectTypeChange.class);
    }

    @Override
    public ObjectTypeChange deserialize(JsonParser parser, DeserializationContext ctxt) throws
            IOException {
        return read_objecttypechange(parser, ctxt);
    }

    public ObjectTypeChange read_objecttypechange(JsonParser parser, DeserializationContext ctxt)
            throws IOException {
        ObjectTypeChange _objecttypechange = new ObjectTypeChange();
        if (parser.nextFieldName(FIELD_NAME)) {
            parser.nextToken();
            String name_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : parser.getText();
            _objecttypechange.name = name_read;
            if (parser.nextFieldName(FIELD_OBJECT)) {
                parser.nextToken();
                Complex object_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : read_object(parser, ctxt);
                _objecttypechange.object = object_read;
                parser.nextToken();
                verifyCurrent(parser, JsonToken.END_OBJECT);
                return _objecttypechange;
            }
        }
        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case IX_FULL_FIELD_NAME:
                        parser.nextToken();
                        String name_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : parser.getText();
                        _objecttypechange.name = name_read;
                        continue;
                    case IX_FULL_FIELD_OBJECT:
                        parser.nextToken();
                        Complex object_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : (Complex) complex_deserializer.deserialize(parser, ctxt);
                        _objecttypechange.object = object_read;
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
        verifyCurrent(parser, JsonToken.END_OBJECT);
        if (_objecttypechange.name == null) throw new IllegalStateException("Missing field: " + FIELD_NAME);
        if (_objecttypechange.object == null) throw new IllegalStateException("Missing field: " + FIELD_OBJECT);
        return _objecttypechange;
    }

    private void verifyCurrent(JsonParser parser, JsonToken expToken) throws IOException {
          if (parser.getCurrentToken() != expToken) {
                    reportIllegal(parser, expToken);
                }
    }

    private void reportIllegal(JsonParser parser, JsonToken expToken) throws IOException {
         JsonToken curr = parser.getCurrentToken();
                String msg = "Expected token "+expToken+"; got "+curr;
                if (curr == JsonToken.FIELD_NAME) {
                    msg += " (current field name '"+parser.getCurrentName()+"')";
                }
                msg += ", location: "+parser.getTokenLocation();
                throw new IllegalStateException(msg);
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        JavaType javaType;
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        javaType = typeFactory.constructType(new TypeReference<Pojo>(){});
        pojo_deserializer = ctxt.findRootValueDeserializer(javaType);

        DeserializerFactory factory = ctxt.getFactory();
        TypeDeserializer objectTypeDeser = factory.findTypeDeserializer(ctxt.getConfig(), ctxt.constructType(Object.class));
        javaType = typeFactory.constructType(new TypeReference<Complex>(){});

        complex_deserializer = new TypeWrappedDeserializer(objectTypeDeser,
                        new ComplexDeserializer(ctxt.findNonContextualValueDeserializer(javaType)));
    }

    private Complex read_object(JsonParser parser, DeserializationContext ctxt) throws IOException {
        Complex object_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : (Complex) complex_deserializer.deserialize(parser, ctxt);
        return object_read;
    }

    static class ComplexDeserializer extends StdDeserializer<Complex> {

        JsonDeserializer<Object> delegate;

        public ComplexDeserializer(JsonDeserializer<Object> delegate) throws JsonMappingException {
           super(Complex.class);
           this.delegate = delegate;
        }

        @Override
        public Complex deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return (Complex) delegate.deserialize(p, ctxt);
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            Object o = typeDeserializer.deserializeTypedFromObject(p, ctxt);
            if (o instanceof Pojo) {
                Complex complex = new Complex();
                complex.pojo = (Pojo) o;
                return complex;
            }
            return o;
        }
    }
}

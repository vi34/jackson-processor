package com.vshatrov.prototypes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vshatrov.raw.Pojo;
import com.vshatrov.raw.annotations.TypeChange;

import java.io.IOException;
import java.util.HashMap;

public final class TypeChangeDeserializer extends StdDeserializer<TypeChange> implements ResolvableDeserializer {
    public static final HashMap<String, Integer> fullFieldToIndex = new HashMap<String, Integer>();

    public static final String FULL_FIELD_NAME = "name";

    public static final int IX_FULL_FIELD_NAME = 1;

    public static final SerializedString FIELD_NAME = new SerializedString(FULL_FIELD_NAME);

    public static final String FULL_FIELD_POJO = "pojo";

    public static final int IX_FULL_FIELD_POJO = 2;

    public static final SerializedString FIELD_POJO = new SerializedString(FULL_FIELD_POJO);

    static {
        fullFieldToIndex.put(FULL_FIELD_NAME, IX_FULL_FIELD_NAME);
        fullFieldToIndex.put(FULL_FIELD_POJO, IX_FULL_FIELD_POJO);
    }

    private JsonDeserializer<Object> pojo_deserializer;

    protected TypeChangeDeserializer(Class<TypeChange> t) {
        super(t);
    }

    public TypeChangeDeserializer() {
        this(null);
    }

    @Override
    public TypeChange deserialize(JsonParser parser, DeserializationContext ctxt) throws
            IOException {
        return read_typechange(parser, ctxt);
    }

    public TypeChange read_typechange(JsonParser parser, DeserializationContext ctxt) throws
            IOException {
        TypeChange _typechange = new TypeChange();
        if (parser.nextFieldName(FIELD_NAME)) {
            parser.nextToken();
            String name_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : parser.getText();
            _typechange.name = name_read;
            if (parser.nextFieldName(FIELD_POJO)) {
                parser.nextToken();
                Pojo pojo_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : read_field_pojo(parser, ctxt);
                _typechange.pojo = pojo_read;
                parser.nextToken();
                verifyCurrent(parser, JsonToken.END_OBJECT);
                return _typechange;
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
                        _typechange.name = name_read;
                        continue;
                    case IX_FULL_FIELD_POJO:
                        parser.nextToken();
                        Pojo pojo_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : (Pojo) pojo_deserializer.deserialize(parser, ctxt);
                        _typechange.pojo = pojo_read;
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
        verifyCurrent(parser, JsonToken.END_OBJECT);
        if (_typechange.name == null) throw new IllegalStateException("Missing field: " + FIELD_NAME);
        if (_typechange.pojo == null) throw new IllegalStateException("Missing field: " + FIELD_POJO);
        return _typechange;
    }

    private Pojo read_field_pojo(JsonParser parser, DeserializationContext ctxt) throws IOException {
        if (parser.currentToken() != JsonToken.START_OBJECT) {
            Pojo pojo = new Pojo();
            pojo.i1 = parser.getIntValue();
            return pojo;
        } else {
            return (Pojo) pojo_deserializer.deserialize(parser, ctxt);
        }
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
        pojo_deserializer = ctxt.findNonContextualValueDeserializer(javaType);
        ArrayType arrayType;
    }
}

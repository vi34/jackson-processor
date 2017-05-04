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
import com.vshatrov.raw.annotations.Alternatives;

import java.io.IOException;
import java.util.HashMap;

public final class AlternativesDeserializer extends StdDeserializer<Alternatives> implements ResolvableDeserializer {
    public static final HashMap<String, Integer> fullFieldToIndex = new HashMap<String, Integer>();

    public static final String FULL_FIELD_NAME = "name";
    public static final String FULL_ALT_1_FIELD_NAME = "fullname";
    public static final String FULL_ALT_2_FIELD_NAME = "Name";
    public static final String FULL_ALT_3_FIELD_NAME = "nAme";
    public static final int IX_FULL_FIELD_NAME = 1;
    public static final String FULL_FIELD_AGE = "age";
    public static final String FULL_ALT_1_FIELD_AGE = "old";
    public static final int IX_FULL_FIELD_AGE = 2;
    public static final String FULL_FIELD_POJO = "pojo";
    public static final String FULL_ALT_1_FIELD_POJO = "Pojjo";
    public static final int IX_FULL_FIELD_POJO = 3;

    public static final SerializedString FIELD_AGE = new SerializedString(FULL_FIELD_AGE);
    public static final SerializedString FIELD_NAME = new SerializedString(FULL_FIELD_NAME);
    public static final SerializedString FIELD_POJO = new SerializedString(FULL_FIELD_POJO);

    static {
        fullFieldToIndex.put(FULL_FIELD_NAME, IX_FULL_FIELD_NAME);
        fullFieldToIndex.put(FULL_ALT_1_FIELD_NAME, IX_FULL_FIELD_NAME);
        fullFieldToIndex.put(FULL_ALT_2_FIELD_NAME, IX_FULL_FIELD_NAME);
        fullFieldToIndex.put(FULL_ALT_3_FIELD_NAME, IX_FULL_FIELD_NAME);
        fullFieldToIndex.put(FULL_FIELD_AGE, IX_FULL_FIELD_AGE);
        fullFieldToIndex.put(FULL_ALT_1_FIELD_AGE, IX_FULL_FIELD_AGE);
        fullFieldToIndex.put(FULL_FIELD_POJO, IX_FULL_FIELD_POJO);
        fullFieldToIndex.put(FULL_ALT_1_FIELD_POJO, IX_FULL_FIELD_POJO);
    }

    private JsonDeserializer<Object> pojo_deserializer;

    protected AlternativesDeserializer(Class<Alternatives> t) {
        super(t);
    }

    public AlternativesDeserializer() {
        this(null);
    }

    @Override
    public Alternatives deserialize(JsonParser parser, DeserializationContext ctxt) throws
            IOException {
        return read_alternatives(parser, ctxt);
    }

    public Alternatives read_alternatives(JsonParser parser, DeserializationContext ctxt) throws
            IOException {
        Alternatives _alternatives = new Alternatives();
        boolean have_age = false;

        if (parser.nextFieldName(FIELD_NAME)) {
            parser.nextToken();
            String name_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : parser.getText();
            _alternatives.name = name_read;
            if (parser.nextFieldName(FIELD_AGE)) {
                parser.nextToken();
                int age_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : parser.getIntValue();
                _alternatives.age = age_read;
                have_age = true;
                if (parser.nextFieldName(FIELD_POJO)) {
                    parser.nextToken();
                    Pojo pojo_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : (Pojo) pojo_deserializer.deserialize(parser, ctxt);
                    _alternatives.pojo = pojo_read;
                    parser.nextToken();
                    verifyCurrent(parser, JsonToken.END_OBJECT);
                    return _alternatives;
                }
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
                        _alternatives.name = name_read;
                        continue;
                    case IX_FULL_FIELD_AGE:
                        parser.nextToken();
                        int age_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : parser.getIntValue();
                        _alternatives.age = age_read;
                        have_age = true;
                        continue;
                    case IX_FULL_FIELD_POJO:
                        parser.nextToken();
                        Pojo pojo_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : (Pojo) pojo_deserializer.deserialize(parser, ctxt);
                        _alternatives.pojo = pojo_read;
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
        verifyCurrent(parser, JsonToken.END_OBJECT);
        if (_alternatives.name == null) throw new IllegalStateException("Missing field: " + FIELD_NAME);
        if (!have_age) throw new IllegalStateException("Missing field: " + FIELD_AGE);
        if (_alternatives.pojo == null) throw new IllegalStateException("Missing field: " + FIELD_POJO);
        return _alternatives;
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

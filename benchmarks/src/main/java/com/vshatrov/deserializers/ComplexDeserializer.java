package com.vshatrov.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vshatrov.entities.Complex;
import com.vshatrov.entities.Pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Viktor Shatrov.
 */
public class ComplexDeserializer extends StdDeserializer<Complex> implements ResolvableDeserializer {

    public static final String FULL_FIELD_NUM = "num";
    public static final int IX_FULL_FIELD_NUM = 1;
    public static final String FULL_FIELD_NAME = "name";
    public static final int IX_FULL_FIELD_NAME = 2;
    public static final String FULL_FIELD_POJOS = "pojos";
    public static final int IX_FULL_FIELD_POJOS = 3;
    private static final SerializedString FIELD_NUM = new SerializedString(FULL_FIELD_NUM);
    private static final SerializedString FIELD_NAME = new SerializedString(FULL_FIELD_NAME);
    private static final SerializedString FIELD_POJOS = new SerializedString(FULL_FIELD_POJOS);

    public static final HashMap<String,Integer> fullFieldToIndex = new HashMap<String,Integer>();
    static {
        fullFieldToIndex.put(FULL_FIELD_NUM, IX_FULL_FIELD_NUM);
        fullFieldToIndex.put(FULL_FIELD_NAME, IX_FULL_FIELD_NAME);
        fullFieldToIndex.put(FULL_FIELD_POJOS, IX_FULL_FIELD_POJOS);
    }

    private JsonDeserializer<Object> pojoDeserializer;

    public ComplexDeserializer() {
        this(null);
    }

    protected ComplexDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Complex deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Complex complex = new Complex();

        boolean haveNum = false;
        if (parser.nextFieldName(FIELD_NAME)) {
            complex.setName(parser.nextTextValue());
            if (parser.nextFieldName(FIELD_NUM)) {
                complex.setNum(parser.nextIntValue(-1));
                haveNum = true;
                if (parser.nextFieldName(FIELD_POJOS)) {
                    complex.setList(readPojos(parser, ctxt));
                    parser.nextToken();
                    verifyCurrent(parser, JsonToken.END_OBJECT);
                    return complex;
                }
            }
        }

        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            // read value token (or START_ARRAY)
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case IX_FULL_FIELD_NUM:
                        complex.setNum(parser.nextIntValue(-1));
                        haveNum = true;
                        continue;
                    case IX_FULL_FIELD_NAME:
                        complex.setName(parser.nextTextValue());
                        continue;
                    case IX_FULL_FIELD_POJOS:
                        complex.setList(readPojos(parser, ctxt));
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }

        verifyCurrent(parser, JsonToken.END_OBJECT);

        if (!haveNum) throw new IllegalStateException("Missing field: " + FIELD_NUM);
        if (complex.getName() == null) throw new IllegalStateException("Missing field: " + FIELD_NAME);
        if (complex.getList() == null) throw new IllegalStateException("Missing field: " + FIELD_POJOS);

        return complex;
    }

    private List<Pojo> readPojos(JsonParser parser, DeserializationContext ctxt) throws IOException {
        if (parser.nextToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        List<Pojo> lints = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            lints.add((Pojo) pojoDeserializer.deserialize(parser, ctxt));
        }
        verifyCurrent(parser, JsonToken.END_ARRAY);
        return lints;
    }

    private void verifyCurrent(JsonParser parser, JsonToken expToken) throws IOException
    {
        if (parser.getCurrentToken() != expToken) {
            reportIllegal(parser, expToken);
        }
    }

    private void reportIllegal(JsonParser parser, JsonToken expToken) throws IOException
    {
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
        JavaType javaType = TypeFactory.defaultInstance().constructType(Pojo.class);
        pojoDeserializer = ctxt.findNonContextualValueDeserializer(javaType);
    }
}

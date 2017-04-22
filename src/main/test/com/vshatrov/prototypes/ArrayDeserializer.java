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
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vshatrov.raw.Array;
import com.vshatrov.raw.Enums;
import com.vshatrov.raw.Pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ArrayDeserializer extends StdDeserializer<Array> implements ResolvableDeserializer {
    public static final HashMap<String, Integer> fullFieldToIndex = new HashMap<String, Integer>();

    public static final String FULL_FIELD_INTS = "ints";

    public static final int IX_FULL_FIELD_INTS = 1;

    public static final SerializedString FIELD_INTS = new SerializedString(FULL_FIELD_INTS);

    public static final String FULL_FIELD_POJOS = "pojos";

    public static final int IX_FULL_FIELD_POJOS = 2;

    public static final SerializedString FIELD_POJOS = new SerializedString(FULL_FIELD_POJOS);

    public static final String FULL_FIELD_LINTS = "lInts";

    public static final int IX_FULL_FIELD_LINTS = 3;

    public static final SerializedString FIELD_LINTS = new SerializedString(FULL_FIELD_LINTS);

    public static final String FULL_FIELD_LPOJOS = "lPojos";

    public static final int IX_FULL_FIELD_LPOJOS = 4;

    public static final SerializedString FIELD_LPOJOS = new SerializedString(FULL_FIELD_LPOJOS);

    public static final String FULL_FIELD_ENS = "ens";

    public static final int IX_FULL_FIELD_ENS = 5;

    public static final SerializedString FIELD_ENS = new SerializedString(FULL_FIELD_ENS);

    static {
        fullFieldToIndex.put(FULL_FIELD_INTS, IX_FULL_FIELD_INTS);
        fullFieldToIndex.put(FULL_FIELD_POJOS, IX_FULL_FIELD_POJOS);
        fullFieldToIndex.put(FULL_FIELD_LINTS, IX_FULL_FIELD_LINTS);
        fullFieldToIndex.put(FULL_FIELD_LPOJOS, IX_FULL_FIELD_LPOJOS);
        fullFieldToIndex.put(FULL_FIELD_ENS, IX_FULL_FIELD_ENS);
    }

    private JsonDeserializer<Object> pojo_deserializer;

    private ObjectArrayDeserializer pojo_arrayDeserializer;

    private JsonDeserializer<int[]> int_arrayDeserializer;

    private ObjectArrayDeserializer en_arrayDeserializer;

    protected ArrayDeserializer(Class<Array> t) {
        super(t);
    }

    public ArrayDeserializer() {
        this(null);
    }

    @Override
    public Array deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        return read_array(parser, ctxt);
    }

    public Array read_array(JsonParser parser, DeserializationContext ctxt) throws IOException {
        Array _array = new Array();
        if (parser.nextFieldName(FIELD_INTS)) {
            parser.nextToken();
            int[] ints_read = read_ints(parser, ctxt);
            _array.ints = ints_read;
            if (parser.nextFieldName(FIELD_POJOS)) {
                parser.nextToken();
                Pojo[] pojos_read = read_pojos(parser, ctxt);
                _array.pojos = pojos_read;
                if (parser.nextFieldName(FIELD_LINTS)) {
                    parser.nextToken();
                    List<Integer> lInts_read = read_lInts(parser, ctxt);
                    _array.lInts = lInts_read;
                    if (parser.nextFieldName(FIELD_LPOJOS)) {
                        parser.nextToken();
                        List<Pojo> lPojos_read = read_lPojos(parser, ctxt);
                        _array.lPojos = lPojos_read;
                        if (parser.nextFieldName(FIELD_ENS)) {
                            parser.nextToken();
                            Enums.En[] ens_read = read_ens(parser, ctxt);
                            _array.ens = ens_read;
                            parser.nextToken();
                            verifyCurrent(parser, JsonToken.END_OBJECT);
                            return _array;
                        }
                    }
                }
            }
        }
        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case IX_FULL_FIELD_INTS:
                        parser.nextToken();
                        int[] ints_read = read_ints(parser, ctxt);
                        _array.ints = ints_read;
                        continue;
                    case IX_FULL_FIELD_POJOS:
                        parser.nextToken();
                        Pojo[] pojos_read = read_pojos(parser, ctxt);
                        _array.pojos = pojos_read;
                        continue;
                    case IX_FULL_FIELD_LINTS:
                        parser.nextToken();
                        List<Integer> lInts_read = read_lInts(parser, ctxt);
                        _array.lInts = lInts_read;
                        continue;
                    case IX_FULL_FIELD_LPOJOS:
                        parser.nextToken();
                        List<Pojo> lPojos_read = read_lPojos(parser, ctxt);
                        _array.lPojos = lPojos_read;
                        continue;
                    case IX_FULL_FIELD_ENS:
                        parser.nextToken();
                        Enums.En[] ens_read = read_ens(parser, ctxt);
                        _array.ens = ens_read;
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
        verifyCurrent(parser, JsonToken.END_OBJECT);
        if (_array.ints == null) throw new IllegalStateException("Missing field: " + FIELD_INTS);
        if (_array.pojos == null) throw new IllegalStateException("Missing field: " + FIELD_POJOS);
        if (_array.lInts == null) throw new IllegalStateException("Missing field: " + FIELD_LINTS);
        if (_array.lPojos == null) throw new IllegalStateException("Missing field: " + FIELD_LPOJOS);
        if (_array.ens == null) throw new IllegalStateException("Missing field: " + FIELD_ENS);
        return _array;
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
        arrayType = typeFactory.constructArrayType(com.vshatrov.raw.Pojo.class);
        pojo_arrayDeserializer = new ObjectArrayDeserializer(arrayType, pojo_deserializer, null);
        int_arrayDeserializer = (JsonDeserializer<int[]>) PrimitiveArrayDeserializers.forType(int.class);
        arrayType = typeFactory.constructArrayType(com.vshatrov.raw.Enums.En.class);
        en_arrayDeserializer =  new ObjectArrayDeserializer(arrayType, new JsonDeserializer<Object>() {
                                    @Override
                                    public Enums.En deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                        return Enums.En.valueOf(p.getText());
                                    }
                                }, null);
    }

    private int[] read_ints(JsonParser parser, DeserializationContext ctxt) throws IOException {
        if (parser.currentToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        int[] arr = (int[]) int_arrayDeserializer.deserialize(parser, ctxt);
        return arr;
    }

    private List<Integer> read_lInts(JsonParser parser, DeserializationContext ctxt) throws
            IOException {
        if (parser.currentToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        List<Integer> arr = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            Integer Integer_read = parser.getIntValue();
            arr.add(Integer_read);
        }
        verifyCurrent(parser, JsonToken.END_ARRAY);
        return arr;
    }

    private Pojo[] read_pojos(JsonParser parser, DeserializationContext ctxt) throws IOException {
        if (parser.currentToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        Pojo[] arr = (Pojo[]) pojo_arrayDeserializer.deserialize(parser, ctxt);
        return arr;
    }

    private List<Pojo> read_lPojos(JsonParser parser, DeserializationContext ctxt) throws
            IOException {
        if (parser.currentToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        List<Pojo> arr = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            Pojo Pojo_read = parser.currentToken() == JsonToken.VALUE_NULL ? null: (Pojo) pojo_deserializer.deserialize(parser, ctxt);
            arr.add(Pojo_read);
        }
        verifyCurrent(parser, JsonToken.END_ARRAY);
        return arr;
    }

    private Enums.En[] read_ens(JsonParser parser, DeserializationContext ctxt) throws IOException {
        if (parser.currentToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        Enums.En[] arr = (Enums.En[]) en_arrayDeserializer.deserialize(parser, ctxt);
        return arr;
    }
}

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
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vshatrov.raw.Maps;
import com.vshatrov.raw.Pojo;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public final class MapsDeserializer extends StdDeserializer<Maps> implements ResolvableDeserializer {
    public static final HashMap<String, Integer> fullFieldToIndex = new HashMap<String, Integer>();

    public static final String FULL_FIELD_PROPS = "props";

    public static final int IX_FULL_FIELD_PROPS = 1;

    public static final SerializedString FIELD_PROPS = new SerializedString(FULL_FIELD_PROPS);

    public static final String FULL_FIELD_HASHMAP = "hashMap";

    public static final int IX_FULL_FIELD_HASHMAP = 2;

    public static final SerializedString FIELD_HASHMAP = new SerializedString(FULL_FIELD_HASHMAP);

    public static final String FULL_FIELD_TREEMAP = "treeMap";

    public static final int IX_FULL_FIELD_TREEMAP = 3;

    public static final SerializedString FIELD_TREEMAP = new SerializedString(FULL_FIELD_TREEMAP);

    static {
        fullFieldToIndex.put(FULL_FIELD_PROPS, IX_FULL_FIELD_PROPS);
        fullFieldToIndex.put(FULL_FIELD_HASHMAP, IX_FULL_FIELD_HASHMAP);
        fullFieldToIndex.put(FULL_FIELD_TREEMAP, IX_FULL_FIELD_TREEMAP);
    }

    private JsonDeserializer<Object> pojo_deserializer;

    protected MapsDeserializer(Class<Maps> t) {
        super(t);
    }

    public MapsDeserializer() {
        this(null);
    }

    @Override
    public Maps deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        return read_maps(parser, ctxt);
    }

    public Maps read_maps(JsonParser parser, DeserializationContext ctxt) throws IOException {
        Maps _maps = new Maps();
        if (parser.nextFieldName(FIELD_PROPS)) {
            parser.nextToken();
            Map props_read = parser.currentToken() == JsonToken.VALUE_NULL ? null:  read_props(ctxt, parser);
            _maps.props = props_read;
            if (parser.nextFieldName(FIELD_HASHMAP)) {
                parser.nextToken();
                HashMap hashMap_read = parser.currentToken() == JsonToken.VALUE_NULL ? null: read_hashMap(ctxt, parser);
                _maps.hashMap = hashMap_read;
                if (parser.nextFieldName(FIELD_TREEMAP)) {
                    parser.nextToken();
                    TreeMap treeMap_read = parser.currentToken() == JsonToken.VALUE_NULL ? null: read_treeMap(ctxt, parser);
                    _maps.treeMap = treeMap_read;
                    parser.nextToken();
                    verifyCurrent(parser, JsonToken.END_OBJECT);
                    return _maps;
                }
            }
        }
        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case IX_FULL_FIELD_PROPS:
                        parser.nextToken();
                        Map<String, String> props_read = parser.currentToken() == JsonToken.VALUE_NULL ? null : read_props(ctxt, parser);
                        _maps.props = props_read;
                        continue;
                    case IX_FULL_FIELD_HASHMAP:
                        parser.nextToken();
                        HashMap hashMap_read = parser.currentToken() == JsonToken.VALUE_NULL ? null: read_hashMap(ctxt, parser);
                        _maps.hashMap = hashMap_read;
                        continue;
                    case IX_FULL_FIELD_TREEMAP:
                        parser.nextToken();
                        TreeMap treeMap_read = parser.currentToken() == JsonToken.VALUE_NULL ? null: read_treeMap(ctxt, parser);
                        _maps.treeMap = treeMap_read;
                        continue;
                }
            }
            System.out.println(parser.getCurrentLocation());
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
        verifyCurrent(parser, JsonToken.END_OBJECT);
        if (_maps.props == null) throw new IllegalStateException("Missing field: " + FIELD_PROPS);
        if (_maps.hashMap == null) throw new IllegalStateException("Missing field: " + FIELD_HASHMAP);
        if (_maps.treeMap == null) throw new IllegalStateException("Missing field: " + FIELD_TREEMAP);
        return _maps;
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
        javaType = typeFactory.constructType(new TypeReference<com.vshatrov.raw.Pojo>(){});
        pojo_deserializer = ctxt.findNonContextualValueDeserializer(javaType);
        ArrayType arrayType;
    }

    private Map<String, String> read_props(DeserializationContext ctxt, JsonParser parser) throws IOException {
        verifyCurrent(parser, JsonToken.START_OBJECT);
        Map<String, String> res = new HashMap<>();

        String keyStr = parser.nextFieldName();

        for (; keyStr != null; keyStr = parser.nextFieldName()) {
            String key = keyStr;
            JsonToken t = parser.nextToken();
            String value;
            if (t == JsonToken.VALUE_NULL) {
                value = null;
            } else {
                value = parser.getText();
            }
            res.put(key, value);
        }
        return res;
    }

    private HashMap<Integer, String> read_hashMap(DeserializationContext ctxt, JsonParser parser) throws IOException {
        verifyCurrent(parser, JsonToken.START_OBJECT);
        HashMap<Integer, String> res = new HashMap<>();

        String keyStr = parser.nextFieldName();

        for (; keyStr != null; keyStr = parser.nextFieldName()) {
            Integer key = Integer.valueOf(keyStr);
            JsonToken t = parser.nextToken();
            String value;
            if (t == JsonToken.VALUE_NULL) {
                value = null;
            } else {
                value = parser.getText();
            }
            res.put(key, value);
        }
        return res;
    }

    private TreeMap<String, Pojo> read_treeMap(DeserializationContext ctxt, JsonParser parser) throws IOException {
        verifyCurrent(parser, JsonToken.START_OBJECT);
        TreeMap<String, Pojo> res = new TreeMap<>();

        String keyStr = parser.nextFieldName();

        for (; keyStr != null; keyStr = parser.nextFieldName()) {
            String key = keyStr;
            JsonToken t = parser.nextToken();
            Pojo value;
            if (t == JsonToken.VALUE_NULL) {
                value = null;
            } else {
                value = (Pojo) pojo_deserializer.deserialize(parser, ctxt);
            }
            res.put(key, value);
        }
        return res;
    }

    private MapDeserializer findMapDeserializer(DeserializationContext ctxt, JavaType mapType, JavaType instType, Class<?> keyClass, JsonDeserializer<?> valueDeser) throws JsonMappingException {
        MapDeserializer des = new MapDeserializer(mapType, new StdValueInstantiator(ctxt.getConfig(), instType)
        ,StdKeyDeserializer.forType(keyClass), (JsonDeserializer<Object>) valueDeser, null);
        des.resolve(ctxt);
        return des;
    }
}

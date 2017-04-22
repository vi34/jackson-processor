package com.vi34.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vi34.entities.Pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vi34 on 09/04/2017.
 */
public class PojoDeserializer extends StdDeserializer<Pojo> {

    public static final String FULL_FIELD_I1 = "i1";
    public static final int IX_FULL_FIELD_I1 = 1;
    public static final String FULL_FIELD_STR = "Str";
    public static final int IX_FULL_FIELD_STR = 2;
    public static final String FULL_FIELD_ILIST = "Ilist";
    public static final int IX_FULL_FIELD_ILIST = 3;
    public static final String FULL_FIELD_BOOL = "bool";
    public static final int IX_FULL_FIELD_BOOL = 4;
    public static final String FULL_FIELD_ADOUBLE = "aDouble";
    public static final int IX_FULL_FIELD_ADOUBLE = 5;
    public static final String FULL_FIELD_PRINT = "prInt";
    public static final int IX_FULL_FIELD_PRINT = 6;
    public static final String FULL_FIELD_ACHAR = "aChar";
    public static final int IX_FULL_FIELD_ACHAR = 7;
    private static final SerializedString FIELD_I1 = new SerializedString(FULL_FIELD_I1);
    private static final SerializedString FIELD_STR = new SerializedString(FULL_FIELD_STR);
    private static final SerializedString FIELD_ILIST = new SerializedString(FULL_FIELD_ILIST);
    private static final SerializedString FIELD_BOOL = new SerializedString(FULL_FIELD_BOOL);
    private static final SerializedString FIELD_ADOUBLE = new SerializedString(FULL_FIELD_ADOUBLE);
    private static final SerializedString FIELD_PRINT = new SerializedString(FULL_FIELD_PRINT);
    private static final SerializedString FIELD_ACHAR = new SerializedString(FULL_FIELD_ACHAR);

    public static final HashMap<String,Integer> fullFieldToIndex = new HashMap<String,Integer>();
    static {
        fullFieldToIndex.put(FULL_FIELD_I1, IX_FULL_FIELD_I1);
        fullFieldToIndex.put(FULL_FIELD_STR, IX_FULL_FIELD_STR);
        fullFieldToIndex.put(FULL_FIELD_ILIST, IX_FULL_FIELD_ILIST);
        fullFieldToIndex.put(FULL_FIELD_BOOL, IX_FULL_FIELD_BOOL);
        fullFieldToIndex.put(FULL_FIELD_ADOUBLE, IX_FULL_FIELD_ADOUBLE);
        fullFieldToIndex.put(FULL_FIELD_PRINT, IX_FULL_FIELD_PRINT);
        fullFieldToIndex.put(FULL_FIELD_ACHAR, IX_FULL_FIELD_ACHAR);
    }

    public PojoDeserializer() {
        this(null);
    }

    protected PojoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Pojo deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Pojo pojo = new Pojo();

        boolean haveI1 = false;
        boolean haveBool = false;
        boolean havePrint = false;
        boolean haveAchar = false;
        if (parser.nextFieldName(FIELD_I1)) {
            pojo.setI1(parser.nextIntValue(-1));
            haveI1 = true;
            if (parser.nextFieldName(FIELD_STR)) {
                pojo.setStr(parser.nextTextValue());
                if (parser.nextFieldName(FIELD_ILIST)) {
                    pojo.setList(readInts(parser, ctxt));
                    if (parser.nextFieldName(FIELD_BOOL)) {
                        pojo.setBool(parser.nextBooleanValue());
                        haveBool = true;
                        if (parser.nextFieldName(FIELD_ADOUBLE)) {
                            parser.nextToken();
                            pojo.setADouble(_parseDouble(parser, ctxt));
                            if (parser.nextFieldName(FIELD_PRINT)) {
                                pojo.setPrInt(parser.nextIntValue(-1));
                                havePrint = true;
                                if (parser.nextFieldName(FIELD_ACHAR)) {
                                    pojo.setAChar(parser.nextTextValue().charAt(0));
                                    haveAchar = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            // read value token (or START_ARRAY)
            parser.nextToken();
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case IX_FULL_FIELD_I1:
                        pojo.setI1(parser.getIntValue());
                        haveI1 = true;
                        continue;
                    case IX_FULL_FIELD_STR:
                        pojo.setStr(parser.getText());
                        continue;
                    case IX_FULL_FIELD_ILIST:
                        pojo.setList(readInts(parser, ctxt));
                        continue;
                    case IX_FULL_FIELD_BOOL:
                        pojo.setBool(parser.getBooleanValue());
                        haveBool = true;
                        continue;
                    case IX_FULL_FIELD_ADOUBLE:
                        pojo.setADouble(_parseDouble(parser, ctxt));
                        continue;
                    case IX_FULL_FIELD_PRINT:
                        pojo.setPrInt(parser.getIntValue());
                        havePrint = true;
                        continue;
                    case IX_FULL_FIELD_ACHAR:
                        pojo.setAChar(parser.getText().charAt(0));
                        haveAchar = true;
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }

        /*
        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            // read value token (or START_ARRAY)
            parser.nextToken();
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case FIELD_IX_URI:
                        image.uri = parser.getText();
                        continue;
                    case FIELD_IX_TITLE:
                        image.title = parser.getText();
                        continue;
                    case FIELD_IX_WIDTH:
                        image.width = parser.getIntValue();
                        haveWidth = true;
                        continue;
                    case FIELD_IX_HEIGHT:
                        image.height = parser.getIntValue();
                        haveHeight = true;
                        continue;
                    case FIELD_IX_SIZE:
                        image.size = Size.valueOf(parser.getText());
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
         */


         /*
    @JsonProperty("i1") int i1;
    @JsonProperty("Str") String str;
    @JsonProperty("Ilist") List<Integer> list;
    boolean bool;
    Double aDouble;
    private int prInt;
    char aChar;
     */

        verifyCurrent(parser, JsonToken.END_OBJECT);

        if (!haveI1) throw new IllegalStateException("Missing field: " + FIELD_I1);
        if (!haveBool) throw new IllegalStateException("Missing field: " + FIELD_BOOL);
        if (!havePrint) throw new IllegalStateException("Missing field: " + FIELD_PRINT);
        if (!haveAchar) throw new IllegalStateException("Missing field: " + FIELD_ACHAR);
        if (pojo.getStr() == null) throw new IllegalStateException("Missing field: " + FIELD_STR);
        if (pojo.getList() == null) throw new IllegalStateException("Missing field: " + FIELD_ILIST);
        if (pojo.getADouble() == null) throw new IllegalStateException("Missing field: " + FIELD_ADOUBLE);

        return pojo;
    }

    private List<Integer> readInts(JsonParser parser, DeserializationContext ctxt) throws IOException {
        if (parser.nextToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        List<Integer> lints = new ArrayList<>();
        while (parser.nextToken() == JsonToken.VALUE_NUMBER_INT) {
            lints.add(parser.getIntValue());
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

}

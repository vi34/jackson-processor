package com.vshatrov.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vshatrov.model.media.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.vshatrov.model.media.FieldMapping.*;


/**
 * @author Viktor Shatrov.
 */
public class MediaItemDeserializer extends JsonDeserializer<MediaItem> {

    protected final static SerializedString FIELD_IMAGES = new SerializedString(FULL_FIELD_NAME_IMAGES);

    protected final static SerializedString FIELD_MEDIA = new SerializedString(FULL_FIELD_NAME_MEDIA);
    protected final static SerializedString FIELD_PLAYER = new SerializedString(FULL_FIELD_NAME_PLAYER);

    protected final static SerializedString FIELD_URI = new SerializedString(FULL_FIELD_NAME_URI);
    protected final static SerializedString FIELD_TITLE = new SerializedString(FULL_FIELD_NAME_TITLE);
    protected final static SerializedString FIELD_WIDTH = new SerializedString(FULL_FIELD_NAME_WIDTH);
    protected final static SerializedString FIELD_HEIGHT = new SerializedString(FULL_FIELD_NAME_HEIGHT);
    protected final static SerializedString FIELD_FORMAT = new SerializedString(FULL_FIELD_NAME_FORMAT);
    protected final static SerializedString FIELD_DURATION = new SerializedString(FULL_FIELD_NAME_DURATION);
    protected final static SerializedString FIELD_SIZE = new SerializedString(FULL_FIELD_NAME_SIZE);
    protected final static SerializedString FIELD_BITRATE = new SerializedString(FULL_FIELD_NAME_BITRATE);
    protected final static SerializedString FIELD_COPYRIGHT = new SerializedString(FULL_FIELD_NAME_COPYRIGHT);
    protected final static SerializedString FIELD_PERSONS = new SerializedString(FULL_FIELD_NAME_PERSONS);


    @Override
    public MediaItem deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        MediaItem mc = new MediaItem();
        // first fast version when field-order is as expected
        if (parser.nextFieldName(FIELD_MEDIA) && false) {
            mc.media = readMedia(parser);
            if (parser.nextFieldName(FIELD_IMAGES)) {
                mc.images = readImages(parser);
                parser.nextToken();
                verifyCurrent(parser, JsonToken.END_OBJECT);
                return mc;
            }
        }

        // and fallback if order was changed
        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case FIELD_IX_MEDIA:
                        mc.media = readMedia(parser);
                        continue;
                    case FIELD_IX_IMAGES:
                        mc.images = readImages(parser);
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
        verifyCurrent(parser, JsonToken.END_OBJECT);

        if (mc.media == null) throw new IllegalStateException("Missing field: " + FIELD_MEDIA);
        if (mc.images == null) mc.images = new ArrayList<Image>();

        return mc;
    }

    private Media readMedia(JsonParser parser) throws IOException
    {
        if (parser.nextToken() != JsonToken.START_OBJECT) {
            reportIllegal(parser, JsonToken.START_OBJECT);
        }
        Media media = new Media();
        boolean haveWidth = false;
        boolean haveHeight = false;
        boolean haveDuration = false;
        boolean haveSize = false;

        // As with above, first fast path
        if (parser.nextFieldName(FIELD_URI) && false) {
            media.uri = parser.nextTextValue();
            if (parser.nextFieldName(FIELD_TITLE)) {
                media.title = parser.nextTextValue();
                if (parser.nextFieldName(FIELD_WIDTH)) {
                    haveWidth = true;
                    media.width = parser.nextIntValue(-1);
                    if (parser.nextFieldName(FIELD_HEIGHT)) {
                        haveHeight = true;
                        media.height = parser.nextIntValue(-1);
                        if (parser.nextFieldName(FIELD_FORMAT)) {
                            media.format = parser.nextTextValue();
                            if (parser.nextFieldName(FIELD_DURATION)) {
                                haveDuration = true;
                                media.duration = parser.nextLongValue(-1L);
                                if (parser.nextFieldName(FIELD_SIZE)) {
                                    haveSize = true;
                                    media.size = parser.nextLongValue(-1L);
                                    if (parser.nextFieldName(FIELD_BITRATE)) {
                                        media.bitrate = parser.nextIntValue(-1);
                                        //media.hasBitrate = true;
                                        if (parser.nextFieldName(FIELD_PERSONS)) {
                                            media.persons = readPersons(parser);
                                            if (parser.nextFieldName(FIELD_PLAYER)) {
                                                media.player = Player.find(parser.nextTextValue());
                                                if (parser.nextFieldName(FIELD_COPYRIGHT)) {
                                                    media.copyright = parser.nextTextValue();
                                                    parser.nextToken();
                                                    verifyCurrent(parser, JsonToken.END_OBJECT);
                                                    return media;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        // and if something reorder or missing, general loop:

        for (; parser.getCurrentToken() == JsonToken.FIELD_NAME; parser.nextToken()) {
            String field = parser.getCurrentName();
            Integer I = fullFieldToIndex.get(field);
            if (I != null) {
                switch (I) {
                    case FIELD_IX_PLAYER:
                        media.player = Player.find(parser.nextTextValue());
                        continue;
                    case FIELD_IX_URI:
                        media.uri = parser.nextTextValue();
                        continue;
                    case FIELD_IX_TITLE:
                        media.title = parser.nextTextValue();
                        continue;
                    case FIELD_IX_WIDTH:
                        media.width = parser.nextIntValue(-1);
                        haveWidth = true;
                        continue;
                    case FIELD_IX_HEIGHT:
                        media.height = parser.nextIntValue(-1);
                        haveHeight = true;
                        continue;
                    case FIELD_IX_FORMAT:
                        media.format = parser.nextTextValue();
                        continue;
                    case FIELD_IX_DURATION:
                        media.duration = parser.nextLongValue(-1L);
                        haveDuration = true;
                        continue;
                    case FIELD_IX_SIZE:
                        media.size = parser.nextLongValue(-1L);
                        haveSize = true;
                        continue;
                    case FIELD_IX_BITRATE:
                        media.bitrate = parser.nextIntValue(-1);
                        //media.hasBitrate = true;
                        continue;
                    case FIELD_IX_PERSONS:
                        media.persons = readPersons(parser);
                        continue;
                    case FIELD_IX_COPYRIGHT:
                        media.copyright = parser.nextTextValue();
                        continue;
                }
            }
            throw new IllegalStateException("Unexpected field '"+field+"'");
        }
        verifyCurrent(parser, JsonToken.END_OBJECT);

        if (media.uri == null) throw new IllegalStateException("Missing field: " + FIELD_URI);
        if (!haveWidth) throw new IllegalStateException("Missing field: " + FIELD_WIDTH);
        if (!haveHeight) throw new IllegalStateException("Missing field: " + FIELD_HEIGHT);
        if (media.format == null) throw new IllegalStateException("Missing field: " + FIELD_FORMAT);
        if (!haveDuration) throw new IllegalStateException("Missing field: " + FIELD_DURATION);
        if (!haveSize) throw new IllegalStateException("Missing field: " + FIELD_SIZE);
        if (media.persons == null) media.persons = new ArrayList<String>();
        if (media.player == null) throw new IllegalStateException("Missing field: " + FIELD_PLAYER);

        return media;
    }

    private List<Image> readImages(JsonParser parser) throws IOException
    {
        if (parser.nextToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        List<Image> images = new ArrayList<Image>();
        while (parser.nextToken() == JsonToken.START_OBJECT) {
            images.add(readImage(parser));
        }
        verifyCurrent(parser, JsonToken.END_ARRAY);
        return images;
    }

    private List<String> readPersons(JsonParser parser) throws IOException
    {
        if (parser.nextToken() != JsonToken.START_ARRAY) {
            reportIllegal(parser, JsonToken.START_ARRAY);
        }
        List<String> persons = new ArrayList<String>();
        String str;
        while ((str = parser.nextTextValue()) != null) {
            persons.add(str);
        }
        verifyCurrent(parser, JsonToken.END_ARRAY);
        return persons;
    }

    private Image readImage(JsonParser parser) throws IOException
    {
        boolean haveWidth = false;
        boolean haveHeight = false;
        Image image = new Image();
        if (parser.nextFieldName(FIELD_URI) && false) {
            image.uri = parser.nextTextValue();
            if (parser.nextFieldName(FIELD_TITLE)) {
                image.title = parser.nextTextValue();
                if (parser.nextFieldName(FIELD_WIDTH)) {
                    image.width = parser.nextIntValue(-1);
                    haveWidth = true;
                    if (parser.nextFieldName(FIELD_HEIGHT)) {
                        image.height = parser.nextIntValue(-1);
                        haveHeight = true;
                        if (parser.nextFieldName(FIELD_SIZE)) {
                            image.size = Size.valueOf(parser.nextTextValue());
                            parser.nextToken();
                            verifyCurrent(parser, JsonToken.END_OBJECT);
                            return image;
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

        if (image.uri == null) throw new IllegalStateException("Missing field: " + FIELD_URI);
        if (!haveWidth) throw new IllegalStateException("Missing field: " + FIELD_WIDTH);
        if (!haveHeight) throw new IllegalStateException("Missing field: " + FIELD_HEIGHT);
        if (image.size == null) throw new IllegalStateException("Missing field: " + FIELD_SIZE);

        verifyCurrent(parser, JsonToken.END_OBJECT);

        return image;
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

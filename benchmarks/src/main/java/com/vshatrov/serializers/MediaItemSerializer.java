package com.vshatrov.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vshatrov.entities.media.Media;
import com.vshatrov.entities.media.MediaItem;
import com.vshatrov.entities.media.Image;

import java.io.IOException;

import static com.vshatrov.entities.media.FieldMapping.*;

/**
 * @author Viktor Shatrov.
 */
public class MediaItemSerializer extends StdSerializer<MediaItem> {


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

    public MediaItemSerializer() {
        this(null);
    }

    protected MediaItemSerializer(Class<MediaItem> t) {
        super(t);
    }

    @Override
    public void serialize(MediaItem value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName(FIELD_MEDIA);
        writeMedia(generator, value.media);
        generator.writeFieldName(FIELD_IMAGES);
        generator.writeStartArray();
        for (Image i : value.images) {
            writeImage(generator, i);
        }
        generator.writeEndArray();
        generator.writeEndObject();

    }

    private void writeMedia(JsonGenerator generator, Media media) throws IOException
    {
        generator.writeStartObject();
        generator.writeFieldName(FIELD_PLAYER);
        generator.writeString(media.player.name());
        generator.writeFieldName(FIELD_URI);
        generator.writeString(media.uri);
        if (media.title != null) {
            generator.writeFieldName(FIELD_TITLE);
            generator.writeString(media.title);
        }
        generator.writeFieldName(FIELD_WIDTH);
        generator.writeNumber(media.width);
        generator.writeFieldName(FIELD_HEIGHT);
        generator.writeNumber(media.height);
        generator.writeFieldName(FIELD_FORMAT);
        generator.writeString(media.format);
        generator.writeFieldName(FIELD_DURATION);
        generator.writeNumber(media.duration);
        generator.writeFieldName(FIELD_SIZE);
        generator.writeNumber(media.size);
        //if (media.hasBitrate) {
            generator.writeFieldName(FIELD_BITRATE);
            generator.writeNumber(media.bitrate);
        //}
        if (media.copyright != null) {
            generator.writeFieldName(FIELD_COPYRIGHT);
            generator.writeString(media.copyright);
        }
        generator.writeFieldName(FIELD_PERSONS);
        generator.writeStartArray();
        for (String person : media.persons) {
            generator.writeString(person);
        }
        generator.writeEndArray();
        generator.writeEndObject();
    }

    private void writeImage(JsonGenerator generator, Image image) throws IOException
    {
        generator.writeStartObject();
        generator.writeFieldName(FIELD_URI);
        generator.writeString(image.uri);
        if (image.title != null) {
            generator.writeFieldName(FIELD_TITLE);
            generator.writeString(image.title);
        }
        generator.writeFieldName(FIELD_WIDTH);
        generator.writeNumber(image.width);
        generator.writeFieldName(FIELD_HEIGHT);
        generator.writeNumber(image.height);
        generator.writeFieldName(FIELD_SIZE);
        generator.writeString(image.size.name());
        generator.writeEndObject();
    }
}

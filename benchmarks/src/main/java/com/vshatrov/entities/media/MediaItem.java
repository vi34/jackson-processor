package com.vshatrov.entities.media;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
@EqualsAndHashCode
@ToString
public class MediaItem
{
    public Media media;
    public List<Image> images;

    public MediaItem() { }

    public MediaItem addPhoto(Image i) {
        if (images == null) {
            images = new ArrayList<Image>();
        }
        images.add(i);
        return this;
    }

    public static MediaItem buildItem()
    {
        Media content = new Media();
        content.player = Player.JAVA;
        content.uri = "http://javaone.com/keynote.mpg";
        content.title = "Javaone Keynote";
        content.width = 640;
        content.height = 480;
        content.format = "video/mpeg4";
        content.duration = 180000000000000000L;
        content.size = 58982400L;
        content.bitrate = 262144;
        content.copyright = "None";
        content.addPerson("Bill Gates");
        content.addPerson("Steve Jobs");

        MediaItem item = new MediaItem();
        item.media = content;

        item.addPhoto(new Image("http://javaone.com/keynote_large.jpg", "Javaone Keynote", 1024, 768, Size.LARGE));
        item.addPhoto(new Image("http://javaone.com/keynote_small.jpg", "Javaone Keynote", 320, 240, Size.SMALL));

        return item;
    }
}

;


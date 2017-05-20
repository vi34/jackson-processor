package com.vshatrov.model.media;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MediaItem)) return false;
        final MediaItem other = (MediaItem) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$media = this.media;
        final Object other$media = other.media;
        if (this$media == null ? other$media != null : !this$media.equals(other$media)) return false;
        final Object this$images = this.images;
        final Object other$images = other.images;
        if (this$images == null ? other$images != null : !this$images.equals(other$images)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $media = this.media;
        result = result * PRIME + ($media == null ? 43 : $media.hashCode());
        final Object $images = this.images;
        result = result * PRIME + ($images == null ? 43 : $images.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MediaItem;
    }

    public String toString() {
        return "com.vshatrov.entities.media.MediaItem(media=" + this.media + ", images=" + this.images + ")";
    }
}

;


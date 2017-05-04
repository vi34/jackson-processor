package com.vshatrov.entities.media;


import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
@EqualsAndHashCode
public class Image
{
    public Image() { }
    public Image(String uri, String title, int w, int h, Size s) {
        this.uri = uri;
        this.title = title;
        width = w;
        height = h;
        size = s;
    }

    public String uri;
    public String title;
    public int width, height;
    public Size size;
}

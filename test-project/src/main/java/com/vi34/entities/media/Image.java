package com.vi34.entities.media;


import com.vi34.annotations.Json;
import lombok.EqualsAndHashCode;

/**
 * Created by vi34 on 31/03/2017.
 */
@Json
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

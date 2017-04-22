package com.vshatrov.entities.media;


import com.vshatrov.annotations.GenerateClasses;
import lombok.EqualsAndHashCode;

/**
 * @author Viktor Shatrov.
 */
@GenerateClasses
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

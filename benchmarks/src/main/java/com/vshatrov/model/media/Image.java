package com.vshatrov.model.media;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Image)) return false;
        final Image other = (Image) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$uri = this.uri;
        final Object other$uri = other.uri;
        if (this$uri == null ? other$uri != null : !this$uri.equals(other$uri)) return false;
        final Object this$title = this.title;
        final Object other$title = other.title;
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        if (this.width != other.width) return false;
        if (this.height != other.height) return false;
        final Object this$size = this.size;
        final Object other$size = other.size;
        if (this$size == null ? other$size != null : !this$size.equals(other$size)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uri = this.uri;
        result = result * PRIME + ($uri == null ? 43 : $uri.hashCode());
        final Object $title = this.title;
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        result = result * PRIME + this.width;
        result = result * PRIME + this.height;
        final Object $size = this.size;
        result = result * PRIME + ($size == null ? 43 : $size.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Image;
    }
}

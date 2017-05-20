package com.vshatrov.model.media;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize
@JsonDeserialize
public class Media {

    public String uri;
    public String title;        // Can be unset.
    public int width;
    public int height;
    public String format;
    public long duration;
    public long size;
    public int bitrate;         // Can be unset.
    //public boolean hasBitrate;

    public List<String> persons;

    public Player player;

    public String copyright;    // Can be unset.

    public Media addPerson(String p) {
        if (persons == null) {
            persons = new ArrayList<String>();
        }
        persons.add(p);
        return this;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Media)) return false;
        final Media other = (Media) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$uri = this.uri;
        final Object other$uri = other.uri;
        if (this$uri == null ? other$uri != null : !this$uri.equals(other$uri)) return false;
        final Object this$title = this.title;
        final Object other$title = other.title;
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        if (this.width != other.width) return false;
        if (this.height != other.height) return false;
        final Object this$format = this.format;
        final Object other$format = other.format;
        if (this$format == null ? other$format != null : !this$format.equals(other$format)) return false;
        if (this.duration != other.duration) return false;
        if (this.size != other.size) return false;
        if (this.bitrate != other.bitrate) return false;
        final Object this$persons = this.persons;
        final Object other$persons = other.persons;
        if (this$persons == null ? other$persons != null : !this$persons.equals(other$persons)) return false;
        final Object this$player = this.player;
        final Object other$player = other.player;
        if (this$player == null ? other$player != null : !this$player.equals(other$player)) return false;
        final Object this$copyright = this.copyright;
        final Object other$copyright = other.copyright;
        if (this$copyright == null ? other$copyright != null : !this$copyright.equals(other$copyright)) return false;
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
        final Object $format = this.format;
        result = result * PRIME + ($format == null ? 43 : $format.hashCode());
        final long $duration = this.duration;
        result = result * PRIME + (int) ($duration >>> 32 ^ $duration);
        final long $size = this.size;
        result = result * PRIME + (int) ($size >>> 32 ^ $size);
        result = result * PRIME + this.bitrate;
        final Object $persons = this.persons;
        result = result * PRIME + ($persons == null ? 43 : $persons.hashCode());
        final Object $player = this.player;
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $copyright = this.copyright;
        result = result * PRIME + ($copyright == null ? 43 : $copyright.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Media;
    }
}

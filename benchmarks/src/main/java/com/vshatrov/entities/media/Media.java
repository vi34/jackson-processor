package com.vshatrov.entities.media;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktor Shatrov.
 */
@JsonSerialize(using = JsonSerializer.None.class)
@JsonDeserialize(using = JsonDeserializer.None.class)
@EqualsAndHashCode
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
}

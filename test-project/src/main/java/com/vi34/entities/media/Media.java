package com.vi34.entities.media;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vi34 on 31/03/2017.
 */
public class Media {

    public String uri;
    public String title;        // Can be unset.
    public int width;
    public int height;
    public String format;
    public long duration;
    public long size;
    public int bitrate;         // Can be unset.
    public boolean hasBitrate;

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

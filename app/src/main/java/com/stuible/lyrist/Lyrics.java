package com.stuible.lyrist;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Lyrics {

    public String title;
    public String body;
    public String created;
    public String type;
    public int UID;

    public Lyrics(String type, int UID, String title, String body, String created){
        this.title = title;
        this.body = body;
        this.created = created;
        this.type = type;
        this.UID = UID;
    }

}


package com.stuible.lyrist;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

//Is used to sort Lyrics
public class LyricCompare implements Comparator<Lyrics> {

    public boolean newestFirst = true;

    public LyricCompare(boolean newestFirst){
        this.newestFirst = newestFirst;
    }

    @Override
    public int compare(Lyrics l1, Lyrics l2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        Date date1;
        Date date2;

        try {
            date1 =  df.parse(l1.created);
            try {
                date2 =  df.parse(l2.created);
                if(newestFirst) return date2.compareTo(date1);
                else return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }




    }
}

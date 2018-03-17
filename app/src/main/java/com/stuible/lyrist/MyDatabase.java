package com.stuible.lyrist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.stuible.lyrist.Constants.DATABASE_NAME;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase (Context c){
        context = c;
        helper = new MyHelper(context);

    }

    public long insertLyrics (String title, String lyrics)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, title);
        contentValues.put(Constants.LYRICS, lyrics);
        long id = db.insert(Constants.LYRIC_TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getLyrics()
    {
//        context.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.TITLE, Constants.LYRICS};
        Cursor cursor = db.query(Constants.LYRIC_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


//    public String getSelectedData(String type)
//    {
//        //select plants from database of type 'herb'
//        SQLiteDatabase db = helper.getWritableDatabase();
//        String[] columns = {Constants.NAME, Constants.TYPE, Constants.LATIN, Constants.LOCATION};
//
//        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
//        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);
//
//        StringBuffer buffer = new StringBuffer();
//        while (cursor.moveToNext()) {
//
//            int index1 = cursor.getColumnIndex(Constants.NAME);
//            int index2 = cursor.getColumnIndex(Constants.TYPE);
//            int index3 = cursor.getColumnIndex(Constants.LATIN);
//            int index4 = cursor.getColumnIndex(Constants.LOCATION);
//
//            String plantName = cursor.getString(index1);
//            String plantType = cursor.getString(index2);
//            String plantLatin = cursor.getString(index3);
//            String plantLocation = cursor.getString(index4);
//            buffer.append(plantName + " " + plantType + " " + plantLatin + " " + plantLocation + "\n");
//        }
//        return buffer.toString();
//    }

}

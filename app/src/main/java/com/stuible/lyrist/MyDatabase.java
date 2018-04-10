package com.stuible.lyrist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

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
        long id = db.insert(Constants.LYRICS_TABLE_NAME, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Constants.UID, id);
        contentValues2.put(Constants.TEXT_LYRICS, lyrics);
        db.insert(Constants.TEXT_LYRIC_TABLE_NAME, null, contentValues2);
        return id;
    }

    public long insertAudioLyrics (String title, String soundFile)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, title);
        long id = db.insert(Constants.LYRICS_TABLE_NAME, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Constants.UID, id);
        contentValues2.put(Constants.AUDIO_LYRICS, soundFile);
        db.insert(Constants.AUDIO_LYRIC_TABLE_NAME, null, contentValues2);
        return id;
    }

    public boolean deleteLyrics(long id){
        db = helper.getWritableDatabase();
        return db.delete(Constants.LYRICS_TABLE_NAME, Constants.UID + "=" + id, null) > 0;
    }

    public long insertPhotoLyrics (String title, byte[] photo)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, title);
        long id = db.insert(Constants.LYRICS_TABLE_NAME, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Constants.UID, id);
        contentValues2.put(Constants.PHOTO_LYRICS, photo);
        db.insert(Constants.PHOTO_LYRIC_TABLE_NAME, null, contentValues2);
        return id;
    }

    public Cursor getLyrics()
    {
//        context.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {"lyrics." + Constants.UID, Constants.TITLE, Constants.DATE, Constants.TEXT_LYRICS};



        String query = "SELECT " + TextUtils.join(", ", columns) + " FROM "
                + Constants.LYRICS_TABLE_NAME + " lyrics INNER JOIN "
                + Constants.TEXT_LYRIC_TABLE_NAME + " text WHERE lyrics."
                + Constants.UID + " = text."
                + Constants.UID;


        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;


    }

    public Cursor getPhotoLyrics()
    {
//        context.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {"lyrics." + Constants.UID, Constants.TITLE, Constants.DATE, Constants.PHOTO_LYRICS};

        String query = "SELECT " + TextUtils.join(", ", columns) + " FROM "
                + Constants.LYRICS_TABLE_NAME + " lyrics INNER JOIN "
                + Constants.PHOTO_LYRIC_TABLE_NAME + " photo WHERE lyrics."
                + Constants.UID + " = photo."
                + Constants.UID;

        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;


    }

    public Cursor getAudioLyrics()
    {
//        context.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {"lyrics." + Constants.UID, Constants.TITLE, Constants.DATE, Constants.AUDIO_LYRICS};

        String query = "SELECT " + TextUtils.join(", ", columns) + " FROM "
                + Constants.LYRICS_TABLE_NAME + " lyrics INNER JOIN "
                + Constants.AUDIO_LYRIC_TABLE_NAME + " audio WHERE lyrics."
                + Constants.UID + " = audio."
                + Constants.UID;

        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, null);
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

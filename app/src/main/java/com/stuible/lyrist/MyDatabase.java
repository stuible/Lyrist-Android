package com.stuible.lyrist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
//        contentValues.put(Constants.TEXT_LYRICS, lyrics);
        long id = db.insert(Constants.LYRICS_TABLE_NAME, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Constants.UID, id);
        contentValues2.put(Constants.TEXT_LYRICS, lyrics);
        db.insert(Constants.TEXT_LYRIC_TABLE_NAME, null, contentValues2);
        return id;
    }

    public Cursor getLyrics()
    {
//        context.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {"lyrics." + Constants.UID, Constants.TITLE, Constants.DATE, Constants.TEXT_LYRICS};

//        Cursor cursor = db.query(Constants.LYRICS_TABLE_NAME, columns, null, null, null, null, null);
//        return cursor;

        String query = "SELECT " + String.join(", ", columns) + " FROM "
                + Constants.LYRICS_TABLE_NAME + " lyrics INNER JOIN "
                + Constants.TEXT_LYRIC_TABLE_NAME + " text WHERE lyrics."
                + Constants.UID + " = text."
                + Constants.UID;

        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

//        // Building query using INNER JOIN keyword
//        String query = "SELECT " + EMPLOYEE_ID_WITH_PREFIX + ","
//        + EMPLOYEE_NAME_WITH_PREFIX + "," + DataBaseHelper.EMPLOYEE_DOB
//        + "," + DataBaseHelper.EMPLOYEE_SALARY + ","
//        + DataBaseHelper.EMPLOYEE_DEPARTMENT_ID + ","
//        + DEPT_NAME_WITH_PREFIX + " FROM "
//        + DataBaseHelper.EMPLOYEE_TABLE + " emp INNER JOIN "
//        + DataBaseHelper.DEPARTMENT_TABLE + " dept ON emp."
//        + DataBaseHelper.EMPLOYEE_DEPARTMENT_ID + " = dept."
//        + DataBaseHelper.ID_COLUMN;

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

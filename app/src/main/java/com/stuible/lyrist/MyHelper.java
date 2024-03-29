package com.stuible.lyrist;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String CREATE_LYRIC_TABLE =
            "CREATE TABLE "+
                    Constants.LYRICS_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.TITLE + " TEXT NOT NULL, " +
                    Constants.DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private static final String CREATE_TEXT_TABLE =
            "CREATE TABLE "+
                    Constants.TEXT_LYRIC_TABLE_NAME + " (" +
                    Constants.TEXT_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.UID + " INTEGER NOT NULL, " +
                    Constants.TEXT_LYRICS + " TEXT NOT NULL, " +
                        "FOREIGN KEY ("+Constants.UID+") REFERENCES "+Constants.LYRICS_TABLE_NAME+"("+Constants.UID+")ON DELETE CASCADE);";

    private static final String CREATE_PHOTO_TABLE =
            "CREATE TABLE "+
                    Constants.PHOTO_LYRIC_TABLE_NAME + " (" +
                    Constants.PHOTO_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.UID + " INTEGER NOT NULL, " +
                    Constants.PHOTO_LYRICS + " TEXT NOT NULL, " +
                    "FOREIGN KEY ("+Constants.UID+") REFERENCES "+Constants.LYRICS_TABLE_NAME+"("+Constants.UID+")ON DELETE CASCADE);";

    private static final String CREATE_AUDIO_TABLE =
            "CREATE TABLE "+
                    Constants.AUDIO_LYRIC_TABLE_NAME + " (" +
                    Constants.AUDIO_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.UID + " INTEGER NOT NULL, " +
                    Constants.AUDIO_LYRICS + " TEXT NOT NULL, " +
                    "FOREIGN KEY ("+Constants.UID+") REFERENCES "+Constants.LYRICS_TABLE_NAME+"("+Constants.UID+")ON DELETE CASCADE);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.LYRICS_TABLE_NAME +
            "; DROP TABLE IF EXISTS " + Constants.TEXT_LYRIC_TABLE_NAME +
            "; DROP TABLE IF EXISTS " + Constants.PHOTO_LYRIC_TABLE_NAME +
            "; DROP TABLE IF EXISTS " + Constants.AUDIO_LYRIC_TABLE_NAME;

    private static final String DROP_LYRICS = "DROP TABLE IF EXISTS " + Constants.LYRICS_TABLE_NAME;
    private static final String DROP_TEXT = "DROP TABLE IF EXISTS " + Constants.TEXT_LYRIC_TABLE_NAME;
    private static final String DROP_PHOTO = "DROP TABLE IF EXISTS " + Constants.PHOTO_LYRIC_TABLE_NAME;
    private static final String DROP_AUDIO = "DROP TABLE IF EXISTS " + Constants.AUDIO_LYRIC_TABLE_NAME;

    public MyHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_LYRIC_TABLE);
            db.execSQL(CREATE_TEXT_TABLE);
            db.execSQL(CREATE_PHOTO_TABLE);
            Log.d("DB MESSAGE", "About to create audio Table");
            db.execSQL(CREATE_AUDIO_TABLE);
            Log.d("DB MESSAGE", "onCreate() called");
//            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Log.d("DB MESSAGE", "DB Creation Error: " + e.toString());
//            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_LYRICS);
            db.execSQL(DROP_TEXT);
            db.execSQL(DROP_AUDIO);
            db.execSQL(DROP_PHOTO);
            onCreate(db);
            Log.d("DB MESSAGE", "onUpgrade() called");
//            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Log.d("DB MESSAGE", "onUpgrade() Error");
//            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}

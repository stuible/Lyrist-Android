package com.stuible.lyrist;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    Constants.LYRIC_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.TITLE + " TEXT, " +
                    Constants.LYRICS + " TEXT);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.LYRIC_TABLE_NAME;

    public MyHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Log.d("DB MESSAGE", "onCreate() called");
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Log.d("DB MESSAGE", "DB Creation Error");
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            Log.d("DB MESSAGE", "onUpgrade() called");
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Log.d("DB MESSAGE", "onUpgrade() Error");
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}

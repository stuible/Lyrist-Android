package com.stuible.lyrist;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.URL;
import java.util.ArrayList;

public class TextEditor extends AppCompatActivity {

    public EditText titleEdit;
    public EditText bodyEdit;
    public MyDatabase db;
    public String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
//        getActionBar().setTitle("Edit Lyrics");

        Intent intent = getIntent();
        this.id = intent.getStringExtra("ID");

        Log.d("ID", this.id);

        titleEdit = findViewById(R.id.titleTextEdit);
        bodyEdit = findViewById(R.id.bodyTextEdit);

        this.db = new MyDatabase(this);
        new loadLyrics(this.id, this.db).execute();


    }

    public void saveButtonClick(View v){
        db.updateTitle(titleEdit.getText().toString(), id);

        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }

    //Async take that loads lyrics
    private class loadLyrics extends AsyncTask<URL, Integer, ArrayList<Lyrics>> {

        String idString;
        MyDatabase thisDB;

        public loadLyrics(String idString, MyDatabase db) {
            super();
            this.idString = idString;
            this.thisDB  = db;
        }

        protected ArrayList<Lyrics> doInBackground(URL... urls) {
            ArrayList<Lyrics> mArrayList = new ArrayList<>();

                Cursor cursor = this.thisDB.getLyricsByID(idString);


                int index1 = cursor.getColumnIndex(Constants.TITLE);
                int index2 = cursor.getColumnIndex(Constants.TEXT_LYRICS);
                int index3 = cursor.getColumnIndex(Constants.UID);
                int index4 = cursor.getColumnIndex(Constants.DATE);


                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String lyricTitle = cursor.getString(index1);
                    String lyrics = cursor.getString(index2);
                    int uid = cursor.getInt(index3);
                    String date = cursor.getString(index4);
                    mArrayList.add(new Lyrics("TEXT", uid, lyricTitle,lyrics, date));
                    Log.d("Found Item In Database", lyricTitle + " " + lyrics);
                    cursor.moveToNext();
                }


            return mArrayList;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ArrayList<Lyrics> result) {

            Lyrics lyric = result.get(0);

            titleEdit.setText(lyric.title);
            bodyEdit.setText(lyric.body);

        }
    }
}

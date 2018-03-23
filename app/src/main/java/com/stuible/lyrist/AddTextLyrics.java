package com.stuible.lyrist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTextLyrics extends AppCompatActivity {

    EditText TitleEditText, lyricsEditText;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_lyrics);

        TitleEditText = (EditText)findViewById(R.id.TitleEditText);
        lyricsEditText = (EditText)findViewById(R.id.lyricsEditText);

        db = new MyDatabase(this);
    }

    public void addLyricEntry (View view)
    {
        String title = TitleEditText.getText().toString();
        String lyrics = lyricsEditText.getText().toString();
        String date = "01-01-2007";
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

        long id = db.insertLyrics(title, lyrics);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
        TitleEditText.setText("");
        lyricsEditText.setText("");

        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);

    }
}

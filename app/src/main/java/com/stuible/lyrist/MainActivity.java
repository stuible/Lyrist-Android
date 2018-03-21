package com.stuible.lyrist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    public RecyclerView mRecyclerView;
    public MyDatabase db;
    public MyAdapter myAdapter;
    public MyHelper helper;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), AddTextLyrics.class);
                startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        MenuItem sortBy = menu.findItem(R.id.sortByMenuButton);

        SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
        String order = sharedPref.getString(getString(R.string.pref_sort_order), "Sort By: Newest");

        sortBy.setTitle(order);


        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
        db = new MyDatabase(this);
        helper = new MyHelper(this);

        new loadLyrics().execute();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        MenuItem sortBy = menu.findItem(R.id.sortByMenuButton);
        int id = item.getItemId();

        SharedPreferences sharedPref =this.getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_SortNewest) {
            sortBy.setTitle("Sort By: Newest");
            editor.putString(getString(R.string.pref_sort_order), "Sort By: Newest");
            editor.apply();
            return true;
        }
        if (id == R.id.action_SortOldest) {
            sortBy.setTitle("Sort By: Oldest");
            editor.putString(getString(R.string.pref_sort_order), "Sort By: Oldest");
            editor.apply();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout clickedRow = (LinearLayout) view;
        TextView titleTextView = (TextView) view.findViewById(R.id.lyricTitle);
        TextView summeryTextView = (TextView) view.findViewById(R.id.lyricSummery);
        Toast.makeText(this, "row " + (1+position) + ":  " + titleTextView.getText() + " " + summeryTextView.getText(), Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_lyrics) {
            // Handle the camera action
        } else if (id == R.id.nav_text_lyric) {
            Intent myIntent = new Intent(getBaseContext(), AddTextLyrics.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_photo_lyric) {
            Toast.makeText(this,
                    "You have clicked " + "Create Photo Lyric",
                    Toast.LENGTH_SHORT).show();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 1);
            }

        } else if (id == R.id.nav_audio_lyric) {

        } else if (id == R.id.nav_audio_lyrics) {

        } else if (id == R.id.nav_photo_lyrics) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class loadLyrics extends AsyncTask<URL, Integer, ArrayList<String>> {
        protected ArrayList<String> doInBackground(URL... urls) {
            Cursor cursor = db.getLyrics();

            int index1 = cursor.getColumnIndex(Constants.TITLE);
            int index2 = cursor.getColumnIndex(Constants.TEXT_LYRICS);

            ArrayList<String> mArrayList = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String lyricTitle = cursor.getString(index1);
                String lyrics = cursor.getString(index2);
                String s = lyricTitle +"," + lyrics;
                mArrayList.add(s);
                Log.d("Found Item In Database", s);
                cursor.moveToNext();
            }

            return mArrayList;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ArrayList<String> result) {
            myAdapter = new MyAdapter(result);
            mRecyclerView = findViewById(R.id.LyricsRecyclerView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(myAdapter);
        }
    }
}

package com.stuible.lyrist;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    //Variables
    public RecyclerView mRecyclerView;
    public MyDatabase db;
    public MyAdapter myAdapter;
    public MyHelper helper;
    private Menu menu;
    private boolean ascending = true;

    public boolean viewTextLyrics = true;
    public boolean viewPhotoLyrics = true;
    public boolean viewAudioLyrics = true;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public String uniqueFilename;
    public File imageFolder;
    public String photoName;



    //When app starts
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

        //Attach elements by ID
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //For Photo Saving
        generateNewPhotoID();

        //Set folder where we store images
        this.imageFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Lyrist" + File.separator + "Images");

        boolean success = true;

        //Create folder if it doesn't yet exist
        if (!this.imageFolder.exists()) {
            success = this.imageFolder.mkdirs();
        }
        if (success) {
        } else {
        }


    }

    //Close drawer when back button is pressed
    @Override
    public void onBackPressed() {
        //Close sidebar when back button is pressed
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    //Create action options menu for the long press items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add Items of menu
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

        //Reload lyrics
        new loadLyrics().execute();


    }

    //When items are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        MenuItem sortBy = menu.findItem(R.id.sortByMenuButton);
        int id = item.getItemId();

        SharedPreferences sharedPref =this.getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        //Check which button was pressed
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_SortNewest) {
            sortBy.setTitle("Sort By: Newest");
            editor.putString(getString(R.string.pref_sort_order), "Sort By: Newest");
            editor.apply();
            ascending = true;
            new loadLyrics().execute();
            return true;
        }
        if (id == R.id.action_SortOldest) {
            sortBy.setTitle("Sort By: Oldest");
            editor.putString(getString(R.string.pref_sort_order), "Sort By: Oldest");
            editor.apply();
            ascending = false;
            new loadLyrics().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Triggered on item click
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout clickedRow = (LinearLayout) view;
        TextView titleTextView = (TextView) view.findViewById(R.id.lyricTitle);
        TextView summeryTextView = (TextView) view.findViewById(R.id.lyricSummery);
        Toast.makeText(this, "row " + (1+position) + ":  " + titleTextView.getText() + " " + summeryTextView.getText(), Toast.LENGTH_LONG).show();
    }

    //Code for sidebar
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_lyrics) {
            viewTextLyrics = true;
            viewAudioLyrics = true;
            viewPhotoLyrics = true;
            Log.d("Clicked Sidebar", "All Lyrics");
            new loadLyrics().execute();
            // Handle the camera action
        } else if (id == R.id.nav_text_lyric) {
            Intent myIntent = new Intent(getBaseContext(), AddTextLyrics.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_photo_lyric) {
            Toast.makeText(this,
                    "You have clicked " + "Create Photo Lyric",
                    Toast.LENGTH_SHORT).show();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //create file
            File f = null;
            try {
                f = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //create URO for storing photo

            Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                    "com.stuible.lyrist.provider",
                    f);

            photoName = photoURI.getLastPathSegment();
            Log.d("Here is the file", photoName);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        } else if (id == R.id.nav_audio_lyric) {
            Intent myIntent = new Intent(getBaseContext(), AddAudioLyrics.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_audio_lyrics) {
            viewTextLyrics = false;
            viewAudioLyrics = true;
            viewPhotoLyrics = false;
            Log.d("Clicked Sidebar", "Audio Lyrics");
            new loadLyrics().execute();

        } else if (id == R.id.nav_photo_lyrics) {
            viewTextLyrics = false;
            viewAudioLyrics = false;
            viewPhotoLyrics = true;
            Log.d("Clicked Sidebar", "Photo Lyrics");
            new loadLyrics().execute();

        }else if (id == R.id.nav_text_lyrics) {
            viewTextLyrics = true;
            viewAudioLyrics = false;
            viewPhotoLyrics = false;
            Log.d("Clicked Sidebar", "Text Lyrics");
            new loadLyrics().execute();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Create new random name for image
    public void generateNewPhotoID(){
        UUID uuid = UUID.randomUUID();
        this.uniqueFilename = uuid.toString();

        Log.d("Generated UUID", this.uniqueFilename);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Once Image is taken, insert location into database
        Log.d("Image Taken", "onActivityResultCalled");

        generateNewPhotoID();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("Image Taken", "About to get Picture");

            Log.d("Adding to DB", photoName);

            long id = db.insertPhotoLyrics("Temp Title", photoName);
            if (id < 0)
            {
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            }


        }
    }

    //Create Image file where the camera will output the photo taken by user
    private File createImageFile() throws IOException {
        // Create an image file name


        String imageFileName = uniqueFilename;
        Log.d("Unique File", imageFileName);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                this.imageFolder
        );
        return image;
    }




    //Async take that loads lyrics
    private class loadLyrics extends AsyncTask<URL, Integer, ArrayList<Lyrics>> {
        protected ArrayList<Lyrics> doInBackground(URL... urls) {
            ArrayList<Lyrics> mArrayList = new ArrayList<>();

            if(viewTextLyrics){
                Cursor cursor = db.getLyrics();


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
                    Log.d("Found Item In Database", lyricTitle + " " + date);
                    cursor.moveToNext();
                }
            }


            if(viewPhotoLyrics) {
                Cursor cursor = db.getPhotoLyrics();

                int index1 = cursor.getColumnIndex(Constants.TITLE);
                int index2 = cursor.getColumnIndex(Constants.PHOTO_LYRICS);
                int index3 = cursor.getColumnIndex(Constants.UID);
                int index4 = cursor.getColumnIndex(Constants.DATE);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String lyricTitle = cursor.getString(index1);
                    int uid = cursor.getInt(index3);
                    String date = cursor.getString(index4);
                    String filename = cursor.getString(index2);
                    mArrayList.add(new Lyrics("IMAGE", uid, lyricTitle, filename, date));
                    Log.d("Found Item In Database", lyricTitle);
                    cursor.moveToNext();
                }
            }

            if(viewAudioLyrics) {
                Log.d("Database", "About to load Audio Lyrics");
                Cursor cursor = db.getAudioLyrics();

                int index1 = cursor.getColumnIndex(Constants.TITLE);
                int index2 = cursor.getColumnIndex(Constants.AUDIO_LYRICS);
                int index3 = cursor.getColumnIndex(Constants.UID);
                int index4 = cursor.getColumnIndex(Constants.DATE);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String lyricTitle = cursor.getString(index1);
                    int uid = cursor.getInt(index3);
                    String date = cursor.getString(index4);
                    String filename = cursor.getString(index2);
                    mArrayList.add(new Lyrics("AUDIO", uid, lyricTitle, filename, date));
                    Log.d("Found Item In Database", lyricTitle);
                    cursor.moveToNext();
                }
            }

            return mArrayList;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ArrayList<Lyrics> result) {

            myAdapter = new MyAdapter(getBaseContext(), result, db, ascending);
            mRecyclerView = findViewById(R.id.LyricsRecyclerView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(myAdapter);
        }
    }


}

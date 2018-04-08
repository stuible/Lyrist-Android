package com.stuible.lyrist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    public RecyclerView mRecyclerView;
    public MyDatabase db;
    public MyAdapter myAdapter;
    public MyHelper helper;
    private Menu menu;
    private boolean ascending = true;

    static final int REQUEST_IMAGE_CAPTURE = 1;



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

//    private void sortData(boolean asc)
//    {
//        //SORT ARRAY ASCENDING AND DESCENDING
//        if (asc)
//        {
//            Collections.sort(spacecrafts);
//        }
//        else
//        {
//            Collections.reverse(spacecrafts);
//        }
//
//        //ADAPTER
//        myAdapter = new MyAdapter(this, spacecrafts);
//        mRecyclerView.setAdapter(myAdapter);
//
//    }

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
//                if (hasImageCaptureBug()) {
//                    takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));
//                } else {
//                    takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                }
//                takePictureIntent.putExtra("return-data", true);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        } else if (id == R.id.nav_audio_lyric) {
            Intent myIntent = new Intent(getBaseContext(), AddAudioLyrics.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_audio_lyrics) {

        } else if (id == R.id.nav_photo_lyrics) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Image Taken", "onActivityResultCalled");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d("Image Taken", "About to get Picture");
            if (data.getData() != null) {
                Log.d("Image Taken", "Data exists");
                ParcelFileDescriptor parcelFileDescriptor = null;
                try {
                    parcelFileDescriptor = this.getContentResolver().openFileDescriptor(data.getData(), "r");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                try {
                    parcelFileDescriptor.close();

                } catch (IOException e) {
                    Log.d("Image Taken", e.toString());
                }
            } else {
                Log.d("Image Taken", "Data didn't exist");
                Bitmap imageRetrieved = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageRetrieved.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Log.d("Image Taken", byteArray.toString());

                long id = db.insertPhotoLyrics("Temp Title", byteArray);
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
    }


    public boolean hasImageCaptureBug() {

        // list of known devices that have the bug
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("android-devphone1/dream_devphone/dream");
        devices.add("generic/sdk/generic");
        devices.add("vodafone/vfpioneer/sapphire");
        devices.add("tmobile/kila/dream");
        devices.add("verizon/voles/sholes");
        devices.add("google_ion/google_ion/sapphire");

        return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
                + android.os.Build.DEVICE);

    }

    private class loadLyrics extends AsyncTask<URL, Integer, ArrayList<String>> {
        protected ArrayList<String> doInBackground(URL... urls) {
            Cursor cursor = db.getLyrics();

            int index1 = cursor.getColumnIndex(Constants.TITLE);
            int index2 = cursor.getColumnIndex(Constants.TEXT_LYRICS);
            int index3 = cursor.getColumnIndex(Constants.UID);

            ArrayList<String> mArrayList = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String lyricTitle = cursor.getString(index1);
                String lyrics = cursor.getString(index2);
                int uid = cursor.getInt(index3);
                String s = uid+","+lyricTitle +"," + lyrics;
                mArrayList.add(s);
                Log.d("Found Item In Database", s);
                cursor.moveToNext();
            }



            cursor = db.getPhotoLyrics();

            index1 = cursor.getColumnIndex(Constants.TITLE);
            index2 = cursor.getColumnIndex(Constants.PHOTO_LYRICS);
            index3 = cursor.getColumnIndex(Constants.UID);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String lyricTitle = cursor.getString(index1);
                int uid = cursor.getInt(index3);
                byte[] lyrics = cursor.getBlob(index2);
                String encoded = Base64.encodeToString(lyrics, 0);
                String s = uid+","+"IMAGE," + lyricTitle +"," + encoded;
                mArrayList.add(s);
                Log.d("Found Item In Database", s);
                cursor.moveToNext();
            }

            return mArrayList;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ArrayList<String> result) {

            myAdapter = new MyAdapter(getBaseContext(), result, db, ascending);
            mRecyclerView = findViewById(R.id.LyricsRecyclerView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(myAdapter);
        }
    }

//    private boolean multiSelect = false;
//    private ArrayList<Integer> selectedItems = new ArrayList<>();
//    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            multiSelect = true;
//            menu.add(0, 0, 0, "Share");
//            menu.add(0, 1, 0, "Delete");
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//
//            if(item.getItemId() == 0){
//
//                Log.d("Clicked", "Share");
//                for (Integer intItem : selectedItems) {
//                    String[]  results = (myAdapter.list.get(intItem).toString()).split(",");
//                    Log.d("Item to Share", results[0]);
//                    String shareBody = results[2];
//                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    sharingIntent.setType("text/plain");
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, results[1]);
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                    startActivity(Intent.createChooser(sharingIntent, "Share Using"));
//                }
//
//
//
//            }
//            else if(item.getItemId() == 1){
//                Log.d("Clicked", "Delete");
//                for (Integer intItem : selectedItems) {
//                    String[]  results = (myAdapter.list.get(intItem).toString()).split(",");
//                    Log.d("Item to delete", results[0]);
//                    boolean attemptDelete = db.deleteLyrics(Long.parseLong(results[0]));
//                    if (attemptDelete == false)
//                    {
//                        Log.d("Delete", "Failed to delete");
//                    }
//                    else
//                    {
//                        Log.d("Delete", "Successfully deleted");
////                        removeItem(intItem);
//
//                    }
////                    Log.d("Item to delete", intItem.toString());
//
//                }
//            }
//
//
//            mode.finish();
//            return true;
//        }
//
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            multiSelect = false;
//            selectedItems.clear();
//            myAdapter.notifyDataSetChanged();
//        }
//    };
}

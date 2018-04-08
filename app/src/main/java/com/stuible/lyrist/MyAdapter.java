package com.stuible.lyrist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static com.stuible.lyrist.R.layout.row;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

//    private ArrayList<Integer> items = new ArrayList<>();
    private boolean multiSelect = false;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private Context context;
    public ArrayList<String> list;
    private boolean ascending = true;

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
//                    String[]  results = (list.get(intItem).toString()).split(",");
//                    Log.d("Item to Share", results[0]);
//                    String shareBody = results[2];
//                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    sharingIntent.setType("text/plain");
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, results[1]);
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                    context.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
//                }
//
//
//
//            }
//            else if(item.getItemId() == 1){
//                Log.d("Clicked", "Delete");
//                for (Integer intItem : selectedItems) {
//                    String[]  results = (list.get(intItem).toString()).split(",");
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
//            notifyDataSetChanged();
//        }
//    };


//    private int selectedPos = RecyclerView.NO_POSITION;

    public MyAdapter(Context context, ArrayList<String> list, boolean ascending) {
        this.list = list;
        if(ascending){
            Collections.sort(this.list);
        }
        else {
            Collections.reverse(this.list);
        }
        this.context = context.getApplicationContext();
    }

    public void removeItem(int position) {
        Log.d("Delete", "called removeItem");
        this.list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(row, parent, false);
        Log.d("View Holder", "Created view holder");
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

        Log.d("onBindViewHolder Pos", (String.valueOf(position)));
        String[]  results = (list.get(position).toString()).split(",");
        if(!results[1].equals("IMAGE")){
            Log.d("Item to List:", results[1] + ": " + results[2]);
            holder.lyricTitleTextView.setText(results[1]);
            holder.summeryTextView.setText(results[2]);
        }
        else if (results[1].equals("IMAGE")){
            Log.d("Item to List:", results[1] + ": " + results[2]);
            holder.lyricTitleTextView.setText(results[2]);
//            holder.summeryTextView.setText(results[2]);
            holder.summeryTextView.setVisibility(View.INVISIBLE);
            holder.myImage.setVisibility(View.VISIBLE);
            byte[] bytes = Base64.decode(results[3], 0);

            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            holder.myImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 250,
                    250, false));

        }

        holder.update(position);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView lyricTitleTextView;
        public TextView summeryTextView;
        public LinearLayout myLayout;
        public ImageView myImage;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            lyricTitleTextView = (TextView) itemView.findViewById(R.id.lyricTitle);
            summeryTextView = (TextView) itemView.findViewById(R.id.lyricSummery);
            myImage = itemView.findViewById(R.id.imageView);



//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
            context = itemView.getContext();

        }

        void selectItem(Integer item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
//                    frameLayout.setBackgroundColor(Color.WHITE);
                    myLayout.setBackgroundResource(R.drawable.lyric_card);
                } else {
                    selectedItems.add(item);
//                    frameLayout.setBackgroundColor(Color.LTGRAY);
                    myLayout.setBackgroundResource(R.drawable.lyric_card_selected);
                }
            }
        }

        void update(final Integer value) {
//            textView.setText(value + "");
            if (selectedItems.contains(value)) {
//                frameLayout.setBackgroundColor(Color.LTGRAY);
                myLayout.setBackgroundResource(R.drawable.lyric_card_selected);
            } else {
//                frameLayout.setBackgroundColor(Color.WHITE);
                myLayout.setBackgroundResource(R.drawable.lyric_card);
            }
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    ((AppCompatActivity)view.getContext()).startSupportActionMode(context.actionModeCallbacks);
//                    selectItem(value);
//                    return true;
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(value);
                    Toast.makeText(context,
                    "You have clicked " + ((TextView)view.findViewById(R.id.lyricTitle)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
                }
            });

        }


//        @Override
//        public void onClick(View view) {
//            Toast.makeText(context,
//                    "You have clicked " + ((TextView)view.findViewById(R.id.lyricTitle)).getText().toString(),
//                    Toast.LENGTH_SHORT).show();
//        }

//        @Override
//        public boolean onLongClick(View view) {
//            Toast.makeText(context,
//                    "You have long clicked " + ((TextView)view.findViewById(R.id.lyricTitle)).getText().toString(),
//                    Toast.LENGTH_SHORT).show();
//
////            notifyItemChanged(selectedPos);
////            View.selectedPos = getLayoutPosition();
////            notifyItemChanged(selectedPos);
//
//            return true;
//        }
    }
}
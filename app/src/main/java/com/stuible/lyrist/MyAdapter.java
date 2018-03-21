package com.stuible.lyrist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.stuible.lyrist.R.layout.row;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

//    private ArrayList<Integer> items = new ArrayList<>();
    private boolean multiSelect = false;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Share");
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (Integer intItem : selectedItems) {
                list.remove(intItem);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    public ArrayList<String> list;
//    private int selectedPos = RecyclerView.NO_POSITION;
    Context context;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
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
        Log.d("Item to List:", results[0] + ": " + results[1]);
        holder.lyricTitleTextView.setText(results[0]);
        holder.summeryTextView.setText(results[1]);
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

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            lyricTitleTextView = (TextView) itemView.findViewById(R.id.lyricTitle);
            summeryTextView = (TextView) itemView.findViewById(R.id.lyricSummery);



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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(value);
                    return true;
                }
            });
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
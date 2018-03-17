package com.stuible.lyrist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.stuible.lyrist.R.layout.row;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(row,parent,false);
////        View v = LayoutInflater.from(parent.getContext()).inflate(row,parent,false);
//        MyViewHolder viewHolder = new MyViewHolder(v);
//        Log.d("View Holder", "Created view holder");
//        return viewHolder;

//        View v = LayoutInflater.from(parent.getContext()).inflate(row,parent,false);
//        MyViewHolder viewHolder = new MyViewHolder(v);
//        Log.d("View Holder", "Created view holder");
//        return viewHolder;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(row, parent, false);
        Log.d("View Holder", "Created view holder");
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

        Log.d("onBindViewHolder Position", (String.valueOf(position)));
        String[]  results = (list.get(position).toString()).split(",");
        Log.d("Item to List:", results[0] + ": " + results[1]);
        holder.lyricTitleTextView.setText(results[0]);
        holder.summeryTextView.setText(results[1]);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView lyricTitleTextView;
        public TextView summeryTextView;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            lyricTitleTextView = (TextView) itemView.findViewById(R.id.lyricTitle);
            summeryTextView = (TextView) itemView.findViewById(R.id.lyricSummery);

            itemView.setOnClickListener(this);
            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    "You have clicked " + ((TextView)view.findViewById(R.id.lyricTitle)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
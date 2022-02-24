package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class holder extends RecyclerView.ViewHolder {
    public TextView header;
    public TextView desc;
    public TextView date;

    public holder(View v){
        super(v);
        header = (TextView) v.findViewById(R.id.list_title);
        desc = (TextView)  v.findViewById(R.id.noteDesc);
        date = (TextView) v.findViewById(R.id.noteDate);
    }

}

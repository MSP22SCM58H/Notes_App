package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<holder> {
    private final List<Notes> listNote;
    private final MainActivity ma;

    public NoteAdapter(List<Notes> nl, MainActivity ma){
        this.listNote = nl;
        this.ma = ma;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(final ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notes, parent, false);
        v.setOnClickListener(ma);
        v.setOnLongClickListener( ma);
        return new holder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(holder m,int p){
        Notes n = listNote.get(p);
        String head = n.getTitle();
        if (head.length() > 80){
            head = head.substring(0,80);
            head = head + "...";
        }
        m.header.setText(head);
        String description = n.getDesc();
        if (head.length() > 80){
            description = description.substring(0,80);
            description = description + "...";
        }
        m.desc.setText(description);
        SimpleDateFormat f = new SimpleDateFormat("EEE MM d, hh:mm a");
        String date_1 = f.format(n.getUpdateDate());
        m.date.setText(date_1);
    }
    @Override
    public int getItemCount(){
        return listNote.size();
    }
}

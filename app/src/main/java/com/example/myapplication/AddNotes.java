package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNotes extends AppCompatActivity {
    private static final String TAG = "AddNotesTAG";
    private EditText title;
    private EditText describe1;
    private String text = "";
    private String text2 = "";
    private long oldDate_Time;
    private Notes originalNote;
    private boolean areNew = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        title = (EditText) findViewById(R.id.notes_title);
        describe1 = (EditText) findViewById(R.id.describe);

        Intent intent = getIntent();
        if (intent.hasExtra("EditNote")) {
            originalNote = (Notes) intent.getSerializableExtra("EditNote");
            if (originalNote != null) {
                text = originalNote.getTitle();
                text2 = originalNote.getDesc();
                oldDate_Time = intent.getLongExtra("Time", 0);
                title.setText(text);
                describe1.setText(text2);
            }
            areNew = false;
        } else {
            areNew = true;
        }
    }


    public void saveNote() {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E MMM dd',' YYYY hh:mm aaa");
        String title_1 = title.getText().toString();
        String desc = describe1.getText().toString();
        if(areNew){
            if (title_1.trim().isEmpty()) {
                showTitleDialogBox();
                return;
            }
            Notes newNote = new Notes(title_1, desc);
            Intent intent = new Intent();
            intent.putExtra("A", newNote);
            setResult(2, intent);
            finish();
        }else if (areNew==false){
            if (title_1.trim().isEmpty()) {
                showTitleDialogBox();
                return;
            }
            originalNote.setTitle(title_1);
            originalNote.setTitle(desc);
            originalNote.setUpdateDate(oldDate_Time);
            Intent intent = new Intent();
            intent.putExtra("EditNote", originalNote);
            setResult(1, intent);
            finish();
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addnote_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        String noteTitle = title.getText().toString();
        String noteText = describe1.getText().toString();

        if (areNew && (!noteTitle.trim().isEmpty())) {
            showSaveDialogBox();
        } else if (areNew && (!noteText.trim().isEmpty())) {
            showSaveDialogBox();
        } else if (!(originalNote == null) && !(noteTitle.equals(originalNote.getTitle()))) {
            showSaveDialogBox();
        } else if (!(originalNote == null) && !(noteText.equals(originalNote.getDesc()))) {
            showSaveDialogBox();
        } else {
            AddNotes.super.onBackPressed();
            return;
        }
    }

    public void showTitleDialogBox() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert) //set icon
                .setTitle("No Title")
                .setMessage("Note cannot be saved without title! Do you want to exit without saving?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddNotes.super.onBackPressed();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .show();
    }

    public void showSaveDialogBox() {
        String noteTitle = title.getText().toString();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)//set icon
                .setTitle("Save Note")
                .setMessage("Your note is not saved! Save Note '" + noteTitle + "' ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveNote();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddNotes.super.onBackPressed();
                        finish();
                    }
                })
                .show();
    }

}
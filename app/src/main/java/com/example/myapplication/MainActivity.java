package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {
        private static final String TAG = "MainActivity";
        private final List<Notes> ln = new ArrayList<>();
        private ActivityResultLauncher<Intent> activityResultLauncher;
        private RecyclerView recyclerView;
        private NoteAdapter nadap;
        private Notes note;
        private int lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;
        super.onCreate(savedInstanceState);
        readJSONFile();
        String app_name = getResources().getString(R.string.app_name);
        setTitle(app_name + " (" + ln.size() + ")");
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_1);
        nadap = new NoteAdapter(ln, this);
        recyclerView.setAdapter(nadap);
        LinearLayoutManager l = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(l);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleMainResult);

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.info) {
            Intent myintent = new Intent(this, About.class);
            startActivity(myintent);
            return true;
        }
        if (id==R.id.add_list){
            Intent myintent = new Intent(this,AddNotes.class);
            activityResultLauncher.launch(myintent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        lp = recyclerView.getChildAdapterPosition(view);
        note = ln.get(lp);
        Intent intent = new Intent(this, AddNotes.class);
        intent.putExtra("EditNote", note);
        activityResultLauncher.launch(intent);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void handleMainResult(ActivityResult activityResult) {
        Log.d(TAG, "handleMainResult: -------------- in Handle Method");
        if (activityResult.getResultCode() == 1) {
            Intent resultData = activityResult.getData();
            if (resultData == null) {

            } else {
                note = (Notes) resultData.getSerializableExtra("EditNote");
                if(note == null){
                    Toast.makeText(this, "Error",Toast.LENGTH_SHORT ).show();
                } else{
                    ln.get(lp).setTitle(note.getTitle());
                    ln.get(lp).setDesc(note.getDesc());
                    ln.get(lp).setUpdateDate(System.currentTimeMillis());
                    Collections.sort(ln);
                    nadap.notifyDataSetChanged();
                }
            }
        }
        if (activityResult.getResultCode() == 2) {
            Intent resultData = activityResult.getData();
            if (resultData != null) {
                note = (Notes) resultData.getSerializableExtra("A");
                if (note != null) {
                    ln.add(note);
                    Collections.sort(ln);
                    nadap.notifyDataSetChanged();
                    String app_name = getResources().getString(R.string.app_name);
                    setTitle(app_name + " (" + ln.size() + ")");
                }
            }
        }
    }
    private void readJSONFile() {
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String noteTitle = jsonObject.getString("title");
                String noteText = jsonObject.getString("text");
                long dateMS = jsonObject.getLong("date");
                Notes note = new Notes(noteTitle, noteText);
                note.setUpdateDate(dateMS);
                ln.add(note);
            }
        } catch (Exception e) {
            Log.d(TAG, "readJSONData: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeDataToJSON()  {

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getResources().getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();
            for(Notes note : ln) {
                jsonWriter.beginObject();
                jsonWriter.name("title").value(note.getTitle());
                jsonWriter.name("text").value(note.getDesc());
                jsonWriter.name("date").value(note.getUpdateDate().getTime());
                jsonWriter.endObject();
            }
            Log.d(TAG, "writeDataToJSON: "+ "Writing into Json"+jsonWriter.toString() );
            jsonWriter.endArray();
            jsonWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        writeDataToJSON();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onLongClick(View view) {
        lp = recyclerView.getChildAdapterPosition(view);
        note = ln.get(lp);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert) //set icon
                .setMessage("Delete Note '" + note.getTitle() + "' ")//set message
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() { // Positive Button Action
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ln.remove(lp);

                        Collections.sort(ln);
                        // Notifying Adapter data has changed
                        nadap.notifyDataSetChanged();
                        // Changing application name and Title with updated count of list
                        String app_name = getResources().getString(R.string.app_name);
                        setTitle(app_name + " (" + ln.size() + ")");
                        return;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() { // Negative Button Action
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .show();
        return true;
    }
}
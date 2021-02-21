package com.example.multinotepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    private final ArrayList<Notes> notesArrayList = new ArrayList<>();
    
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    
    private static final int save_note = 1;
    private static final int edit_note = 2;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(notesArrayList,this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadJsonFile();
        updateTitleCount();

    }

    public void loadJsonFile()
    {
        try
        {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader jReader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            jReader.beginArray();
            while (jReader.hasNext())
            {
                Notes note = new Notes();
                jReader.beginObject();
                while (jReader.hasNext())
                {
                    String name = jReader.nextName();
                    switch (name)
                    {
                        case "title":
                            note.setTitle(jReader.nextString());
                            break;
                        case "notetext":
                            note.setNote_text(jReader.nextString());
                            break;
                        case "date":
                            note.setDate(jReader.nextString());
                            break;
                        default:
                            jReader.skipValue();
                            break;
                    }
                }
                jReader.endObject();
                notesArrayList.add(note);
            }
            jReader.endArray();
        }
        catch (FileNotFoundException ex)
        {
            Toast.makeText(this, "No Data Available.",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void writeJsonFile()
    {
        try
        {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jWriter = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            jWriter.setIndent(" ");
            jWriter.beginArray();
            for (Notes n : notesArrayList)
            {
                jWriter.beginObject();
                jWriter.name("title").value(n.getTitle());
                jWriter.name("notetext").value(n.getNote_text());
                jWriter.name("date").value(n.getDate());
                jWriter.endObject();
            }
            jWriter.endArray();
            jWriter.close();
        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }

    public void updateTitleCount()
    {
        int totalNotes = notesArrayList.size();
        if (totalNotes != 0)
            setTitle(getString(R.string.app_name)+ " (" + totalNotes + ")");
        else
            setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutActivity:
                Intent i1 = new Intent(this,AboutActivity.class);
                startActivity(i1);
                break;
            case R.id.addNote:
                Intent i2 = new Intent(this,EditNoteActivity.class);
                startActivityForResult(i2,save_note);;
                break;
            default:
                Toast.makeText(this,"Option not valid", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        int note_position = recyclerView.getChildLayoutPosition(view);
        Notes note = notesArrayList.get(note_position);
        Intent i3 = new Intent(this,EditNoteActivity.class);
        i3.putExtra("newNote",note);
        i3.putExtra("note_position",note_position);
        startActivityForResult(i3,edit_note);
        
    }

    @Override
    public boolean onLongClick(View view) {

        deleteAlert(view);
        return false;
    }

    private void deleteAlert(final View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int note_position = recyclerView.getChildLayoutPosition(v);
                notesArrayList.remove(note_position);
                notesAdapter.notifyDataSetChanged();
                updateTitleCount();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle("Delete the following note?");
        builder.setMessage("Are you sure you want to delete the note?");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            switch (requestCode) {
                case save_note:
                    if (resultCode == RESULT_OK) {
                        Notes note = new Notes();
                        assert data != null;
                        note.setTitle(data.getStringExtra("title"));
                        note.setNote_text(data.getStringExtra("notetext"));
                        note.setDate(data.getStringExtra("date"));
                        notesArrayList.add(0, note);
                        notesAdapter.notifyDataSetChanged();
                    }
                    break;
                case edit_note:
                    if (resultCode == RESULT_OK) {
                        Notes note = new Notes();
                        assert data != null;
                        note.setTitle(data.getStringExtra("title"));
                        note.setNote_text(data.getStringExtra("notetext"));
                        note.setDate(data.getStringExtra("date"));
                        notesArrayList.remove(data.getIntExtra("note_position", -1));
                        notesArrayList.add(0, note);
                        notesAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    Log.d(TAG, "onActivityResult: Request Code" + requestCode);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Null Pointer encountered.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        updateTitleCount();
        super.onResume();
    }

    @Override
    protected void onPause() {
        writeJsonFile();
        super.onPause();
    }
}
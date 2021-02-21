package com.example.multinotepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    public EditText title,note_text;
    private static final String TAG = "EditNoteActivity";
    private Notes note;
    private String bT = "",bD = "", aT="",aD="";
    private int note_position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        title = findViewById(R.id.noteTitle);
        note_text = findViewById(R.id.noteDesc);

        note_text.setMovementMethod(new ScrollingMovementMethod());
        note_text.setTextIsSelectable(true);
        title.setTextIsSelectable(true);

        Intent data = getIntent();
        if (data.hasExtra("newNote"))
        {
            note = (Notes) data.getSerializableExtra("newNote");
            if (note != null)
            {
                title.setText(note.getTitle());
                note_text.setText(note.getNote_text());
                bT = note.getTitle();
                bD = note.getNote_text();
            }
        }

        if (data.hasExtra("note_position"))
        {
            note_position = data.getIntExtra("note_position",-1);
        }

        note_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addnote_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.saveNote:
                if (title.getText().toString().equals("") && note_text.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noNotes),Toast.LENGTH_LONG).show();
                    finish();

                }
                else if (title.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noTitle),Toast.LENGTH_LONG).show();
                    finish();
                }
                else if (note_text.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noDesc),Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    if(detectChange())
                        saveClicked();
                    else
                        finish();
                }
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean detectChange()
    {
        aT = title.getText().toString();
        aD = note_text.getText().toString();

        if(bT.equals("") && bD.equals("") && aT.equals("") && aD.equals(""))
            return false;
        else if(!bT.equals(aT) || !bD.equals(aD))
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed()
    {
        if (!title.getText().toString().equals("") && !note_text.getText().toString().equals("") && detectChange())
        {
            createAlert();
        }
        else
            super.onBackPressed();

    }

    private void createAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(detectChange())
                    saveClicked();
                else
                    finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });

        builder.setTitle(getString(R.string.sv_dialog_title));
        builder.setMessage("Save Note '"+ title.getText()+"' ?");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void saveClicked()
    {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E MMM dd',' YYYY hh:mm a ");

        Intent data = new Intent();
        data.putExtra("title", title.getText().toString());
        data.putExtra("notetext",note_text.getText().toString());
        data.putExtra("date",ft.format(d));
        if(note_position!=-1)
            data.putExtra("note_position",note_position);
        setResult(RESULT_OK,data);
        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        finish();
    }

}
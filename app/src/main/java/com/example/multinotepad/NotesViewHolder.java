package com.example.multinotepad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    public TextView note_title;
    TextView note_timestamp;
    TextView note_text;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        note_title = itemView.findViewById(R.id.noteRowTitle);
        note_timestamp = itemView.findViewById(R.id.dateText);
        note_text = itemView.findViewById(R.id.NoteRowText);
    }
}

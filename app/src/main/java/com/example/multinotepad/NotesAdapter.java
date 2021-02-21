package com.example.multinotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private static final String TAG = "NotesAdapter";
    private List<Notes> noteslist;
    private MainActivity mainAct;

    public NotesAdapter(List<Notes> noteslist, MainActivity mainAct) {
        this.noteslist = noteslist;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NotesViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        Notes note = noteslist.get(position);

        if (note.getTitle().length() > 80){
            holder.note_title.setText(String.format("%s...", note.getTitle().substring(0, 80)));
        }
        else {
            holder.note_title.setText(note.getTitle());
        }
        holder.note_timestamp.setText(note.getDate());

        if (note.getNote_text().length() > 80){
            holder.note_text.setText(String.format("%s...", note.getNote_text().substring(0, 80)));
        }
        else {
            holder.note_text.setText(note.getNote_text());
        }
    }

    @Override
    public int getItemCount() {
        return noteslist.size();
    }
}

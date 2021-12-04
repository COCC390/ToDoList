package com.bc.secretnoteandtodo.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bc.secretnoteandtodo.CreateNewNote;
import com.bc.secretnoteandtodo.R;
import com.bc.secretnoteandtodo.database.DatabaseHelper;
import com.bc.secretnoteandtodo.database.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder>
{

    private List<Note> noteList;
    private AllNotes allNotesActivity;
    private DatabaseHelper db;

    public NotesAdapter(DatabaseHelper db, AllNotes allNotes)
    {
        this.db = db;
        this.allNotesActivity = allNotes;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesAdapter.MyViewHolder holder, int position)
    {
        db.openDatabase();

        final Note note = noteList.get(position);
        holder.note.setText(note.getContent());
    }

    @Override
    public int getItemCount()
    {
        return noteList.size();
    }

    public Context getContext()
    {
        return allNotesActivity;
    }

    public void setNotes(List<Note> noteList)
    {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position)
    {
        Note note = noteList.get(position);
        db.deleteNote(note.getId());
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position)
    {
        Note note = noteList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", note.getId());
        bundle.putString("content", note.getContent());
        CreateNewNote fragment = new CreateNewNote();
        fragment.setArguments(bundle);
        fragment.show(allNotesActivity.getSupportFragmentManager(), CreateNewNote.TAG);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView note;

        MyViewHolder(View view)
        {
            super(view);
            note = view.findViewById(R.id.noteTextView);
        }
    }
}

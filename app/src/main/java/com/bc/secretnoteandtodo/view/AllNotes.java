package com.bc.secretnoteandtodo.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bc.secretnoteandtodo.CreateNewNote;
import com.bc.secretnoteandtodo.CreateNewTask;
import com.bc.secretnoteandtodo.R;
import com.bc.secretnoteandtodo.UserSetting;
import com.bc.secretnoteandtodo.database.DatabaseHelper;
import com.bc.secretnoteandtodo.database.DatabaseHelperForToDoTask;
import com.bc.secretnoteandtodo.database.model.Note;
import com.bc.secretnoteandtodo.utils.DialogCloseListener;
import com.bc.secretnoteandtodo.utils.MyDividerItemDecoration;
import com.bc.secretnoteandtodo.utils.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllNotes extends AppCompatActivity implements View.OnClickListener, DialogCloseListener {
    private Button btnAccount, btnToDo;
    private FloatingActionButton fab;
    private NotesAdapter notesAdapter;
    private List<Note> notesList = new ArrayList<>();
    private RecyclerView rvNotes;
    private TextView noNotesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        getSupportActionBar().hide();

        db = new DatabaseHelper(this);
        try
        {
            db.createDataBase();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        db.openDatabase();

        LinkToView();

        notesList = new ArrayList<>();

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(db, this);
        rvNotes.setAdapter(notesAdapter);

//        toggleEmptyNotes();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerTouchListener(notesAdapter));
        itemTouchHelper.attachToRecyclerView(rvNotes);

        notesList = db.getAllNotes();
        Collections.reverse(notesList);
        notesAdapter.setNotes(notesList);
//        LoadNote();

        btnToDo.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void LoadNote()
    {
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(db, this);
        rvNotes.setAdapter(notesAdapter);

        notesList = db.getAllNotes();
        Collections.reverse(notesList);
        notesAdapter.setNotes(notesList);
    }

    private void LinkToView()
    {
        rvNotes = (RecyclerView) findViewById(R.id.rvNote);
        fab = findViewById(R.id.fab);
        btnToDo = (Button) findViewById(R.id.btn_toDoList);
        noNotesView = (TextView) findViewById(R.id.empty_notes_view);
        btnAccount = (Button) findViewById(R.id.btn_account);
    }

    @Override
    public void onClick(View view)
    {
       if(view.getId() == R.id.btn_toDoList)
       {
            Intent intent = new Intent(AllNotes.this, AllToDo.class);
            startActivity(intent);
       }
       if(view.getId() == R.id.fab)
       {
            CreateNewNote.newInstance().show(getSupportFragmentManager(), CreateNewNote.TAG);
            LoadNote();
       }
       if(view.getId() == R.id.btn_account)
       {
//              use this to switch to account class!!!
           Intent intent = new Intent(AllNotes.this, UserSetting.class);
           startActivity(intent);
       }
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (notesAdapter.getItemCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface)
    {
        notesList = db.getAllNotes();
        Collections.reverse(notesList);
        notesAdapter.setNotes(notesList);
        notesAdapter.notifyDataSetChanged();
    }
}

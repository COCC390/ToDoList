package com.bc.secretnoteandtodo.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bc.secretnoteandtodo.CreateNewTask;
import com.bc.secretnoteandtodo.R;
import com.bc.secretnoteandtodo.database.DatabaseHelperForToDoTask;
import com.bc.secretnoteandtodo.database.model.ToDo;
import com.bc.secretnoteandtodo.utils.DialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllToDo extends AppCompatActivity implements View.OnClickListener, DialogCloseListener {
    private Button btnNote, btnAccount;
    private RecyclerView rvTasks;
    private ToDoTasksAdapter toDoTasksAdapter;
    private DatabaseHelperForToDoTask db;
    private FloatingActionButton floatingActionButton;

    private List<ToDo> toDoTasksList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_to_do);
        getSupportActionBar().hide();

        db = new DatabaseHelperForToDoTask(this);
        db.openDatabase();
        try {
            db.createDataBase();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        LinkToView();

        toDoTasksList = new ArrayList<>();

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        toDoTasksAdapter = new ToDoTasksAdapter(db, this);
        rvTasks.setAdapter(toDoTasksAdapter);

        toDoTasksList = db.getAllTasks();
        Collections.reverse(toDoTasksList);
        toDoTasksAdapter.setTasks(toDoTasksList);
        LoadTask();

        btnNote.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    private void LinkToView()
    {
        rvTasks = (RecyclerView) findViewById(R.id.rvTasks);
        btnNote = (Button) findViewById(R.id.btn_note);
        btnAccount = (Button) findViewById(R.id.btn_account);
        floatingActionButton = findViewById(R.id.fabToDo);
    }

    private void LoadTask()
    {
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        toDoTasksAdapter = new ToDoTasksAdapter(db, this);
        rvTasks.setAdapter(toDoTasksAdapter);

        toDoTasksList = db.getAllTasks();
        Collections.reverse(toDoTasksList);
        toDoTasksAdapter.setTasks(toDoTasksList);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.btn_note)
        {
            Intent intent = new Intent(AllToDo.this, AllNotes.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.fabToDo)
        {
            CreateNewTask.newInstance().show(getSupportFragmentManager(), CreateNewTask.TAG);
//            LoadTask();
//            if(toDoTasksList.size() > 0)
//            {
//                Log.d("myTag", toDoTasksList.get(0).getTitle());
//            }
        }
        if(v.getId() == R.id.btn_account)
        {
            // use this to switch to account class!!!
//           Intent intent = new Intent(AllNotes.this, Account.class);
//           startActivity(intent);
        }
    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface)
    {
        toDoTasksList = db.getAllTasks();
        Collections.reverse(toDoTasksList);
        toDoTasksAdapter.setTasks(toDoTasksList);
        toDoTasksAdapter.notifyDataSetChanged();
    }
}
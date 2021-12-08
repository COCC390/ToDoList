package com.bc.secretnoteandtodo.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bc.secretnoteandtodo.CreateNewTask;
import com.bc.secretnoteandtodo.R;
import com.bc.secretnoteandtodo.UserSetting;
import com.bc.secretnoteandtodo.database.DBHelper;

import com.bc.secretnoteandtodo.database.DatabaseHelperForToDoTask;
import com.bc.secretnoteandtodo.database.model.ToDo;
import com.bc.secretnoteandtodo.utils.DialogCloseListener;
import com.bc.secretnoteandtodo.utils.ToDoRecyclerTouchListener;
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

    private List<ToDo> toDoTasksFilterList;
    private List<ToDo> toDoTasksList;

    private DBHelper userDb;

    private int currentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_to_do);
        getSupportActionBar().hide();

        db = new DatabaseHelperForToDoTask(this);
        userDb = new DBHelper(this);
        currentId = userDb.getCurrentID();
        CreateNewTask.currentId = currentId;


        try {
            db.createDataBase();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        db.openDatabase();

        LinkToView();

//        toDoTasksList = new ArrayList<>();

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        toDoTasksAdapter = new ToDoTasksAdapter(db, this);
        rvTasks.setAdapter(toDoTasksAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ToDoRecyclerTouchListener(toDoTasksAdapter));
        itemTouchHelper.attachToRecyclerView(rvTasks);

        toDoTasksList = db.getAllTasks();

        toDoTasksFilterList = NoteListFilter(toDoTasksList);

        Collections.reverse(toDoTasksFilterList);
        toDoTasksAdapter.setTasks(toDoTasksFilterList);


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

    private List<ToDo> NoteListFilter(List<ToDo> toDoListUnFilter)
    {
        List<ToDo> toDoList = new ArrayList<>();

        for (ToDo item: toDoListUnFilter)
        {
            if(item.getUserId() == currentId)
            {
                toDoList.add(item);
            }
        }
        return toDoList;
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
        }
        if(v.getId() == R.id.btn_account)
        {

           Intent intent = new Intent(AllToDo.this, UserSetting.class);
           startActivity(intent);
        }
    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface)
    {
        toDoTasksList = db.getAllTasks();
        toDoTasksFilterList = NoteListFilter(toDoTasksList);
        Collections.reverse(toDoTasksFilterList);
        toDoTasksAdapter.setTasks(toDoTasksFilterList);
        toDoTasksAdapter.notifyDataSetChanged();
    }
}
package com.bc.secretnoteandtodo.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bc.secretnoteandtodo.CreateNewTask;
import com.bc.secretnoteandtodo.R;
import com.bc.secretnoteandtodo.database.DatabaseHelperForToDoTask;
import com.bc.secretnoteandtodo.database.model.Note;
import com.bc.secretnoteandtodo.database.model.ToDo;

import java.util.List;

public class ToDoTasksAdapter extends RecyclerView.Adapter<ToDoTasksAdapter.ViewHolder>
{
    private List<ToDo> toDoList;
    private AllToDo allToDoActivity;
    private DatabaseHelperForToDoTask db;

    public ToDoTasksAdapter(DatabaseHelperForToDoTask db, AllToDo allToDo)
    {
        this.db = db;
        this.allToDoActivity = allToDo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        db.openDatabase();

        final ToDo item = toDoList.get(position);
        holder.task.setText(item.getTitle());
        holder.task.setChecked(updateCheckedStatus(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    db.updateStatus(item.getId(), 1);
                }
                else
                {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return toDoList.size();
    }

    private boolean updateCheckedStatus(int checkStatus)
    {
        return checkStatus != 0;
    }

    public Context getContext()
    {
        return allToDoActivity;
    }

    public void setTasks(List<ToDo> toDoList)
    {
        this.toDoList = toDoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position)
    {
        ToDo toDo = toDoList.get(position);
        db.deleteTask(toDo.getId());
        toDoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position)
    {
        ToDo item = toDoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTitle());
        Log.d("title" , item.getTitle().toString());
        CreateNewTask fragment = new CreateNewTask();
        fragment.setArguments(bundle);
        fragment.show(allToDoActivity.getSupportFragmentManager(), CreateNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox task;

        ViewHolder(View view)
        {
            super(view);
            task = view.findViewById(R.id.todoCheck);
        }
    }
}

package com.bc.secretnoteandtodo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.bc.secretnoteandtodo.database.DatabaseHelperForToDoTask;
import com.bc.secretnoteandtodo.database.model.ToDo;
import com.bc.secretnoteandtodo.utils.DialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateNewTask extends BottomSheetDialogFragment
{
    public static final String TAG = "ActionBottomDialog";

    private EditText etNewTask;
    private Button btnNewTask;
    private DatabaseHelperForToDoTask db;

    public static CreateNewTask newInstance()
    {
        return new CreateNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.TaskStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.create_task, viewGroup, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        etNewTask = getView().findViewById(R.id.etNewTask);
        btnNewTask = getView().findViewById(R.id.btnCreateNewTask);

        db = new DatabaseHelperForToDoTask(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null)
        {
            isUpdate = true;
            String task = bundle.getString("task");
            etNewTask.setText(task);
            if(task.length() > 0)
            {
                btnNewTask.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }
        }

        etNewTask.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().equals(""))
                {
                    btnNewTask.setEnabled(false);
                    btnNewTask.setTextColor(Color.BLACK);
                }
                else
                {
                    btnNewTask.setEnabled(true);
                    btnNewTask.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            { }
        });

        boolean finalIsUpdate = isUpdate;
        btnNewTask.setOnClickListener(new View.OnClickListener()
        {
//
            @Override
            public void onClick(View v)
            {
                String text = etNewTask.getText().toString();
                if(finalIsUpdate)
                {
                    db.updateTask(bundle.getInt("ID"), text);
                }
                else
                {
//                    Log.d("myTag", "This is my message");
                    ToDo todoTask = new ToDo();
                    todoTask.setTitle(text);
                    todoTask.setStatus(0);
                    db.insertTask(todoTask);
//                    Log.d("myTag", todoTask.getTitle().toString());
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface)
    {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
        {
            ((DialogCloseListener)activity).handleDialogClose(dialogInterface);
        }
    }
}

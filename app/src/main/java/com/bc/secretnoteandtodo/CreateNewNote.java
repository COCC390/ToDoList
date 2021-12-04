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
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bc.secretnoteandtodo.database.DatabaseHelper;
import com.bc.secretnoteandtodo.database.model.Note;
import com.bc.secretnoteandtodo.utils.DialogCloseListener;
import com.bc.secretnoteandtodo.view.AllNotes;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateNewNote extends BottomSheetDialogFragment
{
    public static final String TAG = "NoteActionBottomDialog";

    private EditText etNewNote;
    private Button btnNewNote;
    private DatabaseHelper db;

    public static CreateNewNote newInstance()
    {
        return new CreateNewNote();
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
        View view = inflater.inflate(R.layout.note_dialog, viewGroup, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        etNewNote = getView().findViewById(R.id.etNewNote);
        btnNewNote = getView().findViewById(R.id.btnCreateNewNote);

        db = new DatabaseHelper(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null)
        {
            isUpdate = true;
            String note = bundle.getString("note");
            etNewNote.setText(note);
            if(note.length() > 0)
            {
                etNewNote.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }
        }

        etNewNote.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().equals(""))
                {
                    etNewNote.setEnabled(false);
                    etNewNote.setTextColor(Color.BLACK);
                }
                else
                {
                    etNewNote.setEnabled(true);
                    etNewNote.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            { }
        });

        boolean finalIsUpdate = isUpdate;
        btnNewNote.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String text = etNewNote.getText().toString();
                if(finalIsUpdate)
                {
                    db.updateNote(bundle.getInt("ID"), text);
                }
                else
                {
//                    Log.d("myTag", "This is my message");
                    Note note = new Note();
                    note.setContent(text);
                    db.insertTask(note);
                    Log.d("myTag", note.getContent().toString());
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

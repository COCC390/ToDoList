package com.bc.secretnoteandtodo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bc.secretnoteandtodo.R;
import com.bc.secretnoteandtodo.view.NotesAdapter;
import com.bc.secretnoteandtodo.view.ToDoTasksAdapter;

public class RecyclerTouchListener extends ItemTouchHelper.SimpleCallback
{
    private NotesAdapter notesAdapter;
    private ToDoTasksAdapter toDoTasksAdapter;

    public RecyclerTouchListener(NotesAdapter notesAdapter)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.notesAdapter = notesAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
    {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
    {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(notesAdapter.getContext());
            builder.setTitle("Delete Note");
            builder.setMessage("Are you sure you want to delete this note");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            notesAdapter.deleteItem(position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notesAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            notesAdapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if(dX > 0)
        {
            icon = ContextCompat.getDrawable(notesAdapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(notesAdapter.getContext(), R.color.colorPrimaryDark));
        }
        else
        {
            icon = ContextCompat.getDrawable(notesAdapter.getContext(), R.drawable.ic_baseline_delete);
            background = new ColorDrawable(ContextCompat.getColor(notesAdapter.getContext(), R.color.colorPrimaryDark));
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop  + icon.getIntrinsicHeight();

        if (dX > 0)
        {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        }
        else if (dX < 0)
        {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
        else
        {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}

package com.bc.secretnoteandtodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.bc.secretnoteandtodo.database.model.ToDo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperForToDoTask extends SQLiteOpenHelper
{
    private static final int VERSION = 2;
    private static String DB_PATH = "";
    private static final String NAME = "Mobile";
    private static final String TODO_TASK_TABLE = "Todo";
    private static final String ID = "ID";
    private static final String TITLE = "Title";
    private static final String STATUS = "Status";

    private final Context context;

    private SQLiteDatabase db;

    public DatabaseHelperForToDoTask(Context context)
    {
        super(context, NAME, null, VERSION);

        if(Build.VERSION.SDK_INT >= 17)
        {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        this.context = context;
    }

    @Override
    public synchronized void close()
    {
        if(db != null)
        {
            db.close();
        }
        super.close();
    }

    private boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;
        try
        {
            String myPath = DB_PATH + NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLiteException e)
        {
        }
        if (checkDB != null)
        {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException
    {
        InputStream myInput = context.getAssets().open(NAME);
        String outFileName = DB_PATH + NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

//    public void openDatabase() throws SQLException
//    {
//        String myPath = DB_PATH + NAME;
//        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//    }

    public void createDataBase() throws IOException
    {
        boolean dbExist = checkDataBase();
        if (dbExist)
        {
        } else
            {
            this.getReadableDatabase();
            try
            {
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openDatabase()
    {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDo task)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, task.getTitle());
        contentValues.put(STATUS, 0);
        db.insert(TODO_TASK_TABLE, null, contentValues);
    }

    public List<ToDo> getAllTasks()
    {
        List<ToDo> tasksList = new ArrayList<>();
        SQLiteDatabase readDb = this.getReadableDatabase();

        Cursor cursor = null;
        readDb.beginTransaction();
        try
        {
            cursor = db.query(TODO_TASK_TABLE, null, null, null, null, null, null);
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do {
                        ToDo task = new ToDo();
                        task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                        task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(STATUS)));
                        tasksList.add(task);
                    }
                    while (cursor.moveToNext());
                }
            }
        }
        finally
        {
            readDb.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return tasksList;
    }

    public void updateStatus(int id, int status)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TODO_TASK_TABLE, contentValues, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, task);
        db.update(TODO_TASK_TABLE, contentValues, ID + "=?",  new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id)
    {
        db.delete(TODO_TASK_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}

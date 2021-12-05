package com.bc.secretnoteandtodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.bc.secretnoteandtodo.database.model.Note;
import com.bc.secretnoteandtodo.database.model.ToDo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static String DB_PATH = "";
    private static final String NAME = "Mobile";
    private static final String NOTE_TABLE = "Note";
    private static final String ID = "ID";
    private static final String CONTENT = "Content";
//    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TASK_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT," + STATUS + " INTEGER)";

    private final Context context;

    private SQLiteDatabase db;

    public DatabaseHelper(Context context)
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

//    public void openDataBase() throws SQLException
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
//        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
//        db.execSQL("DROP TABLE IF EXISTS " + TODO_TASK_TABLE);
//        onCreate(db);
        if (newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }

    public void openDatabase()
    {
        db = this.getWritableDatabase();
    }

    public void insertNote(Note note)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTENT, note.getContent());
        db.insert(NOTE_TABLE, null, contentValues);
    }

    public List<Note> getAllNotes()
    {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase readDb = this.getReadableDatabase();

        Cursor cursor = null;
        readDb.beginTransaction();
        try
        {
            cursor = db.query(NOTE_TABLE, null, null, null, null, null, null);
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do {
                        Note note = new Note();
                        note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                        note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)));
                        noteList.add(note);
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
        return noteList;
    }

    public void updateNote(int id, String note)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTENT, note);
        db.update(NOTE_TABLE, contentValues, ID + "=?",  new String[] {String.valueOf(id)});
    }

    public void deleteNote(int id)
    {
        db.delete(NOTE_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}

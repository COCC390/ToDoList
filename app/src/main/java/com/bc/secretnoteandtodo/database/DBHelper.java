package com.bc.secretnoteandtodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.bc.secretnoteandtodo.database.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static String DB_PATH = "";
    private static final String NAME = "Mobile.db";
    private static final String TABLE_NAME = "User";
    private static final String ID = "ID";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    private final Context context;
    private static int id;
    private static String userName;
    private static String password;


    private SQLiteDatabase db;

    public DBHelper(Context context)
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
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
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
        if (newVersion > oldVersion)
        {
            try
            {
                copyDataBase();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void openDatabase()
    {
        db = this.getWritableDatabase();
    }

    /*===========================Function for signup // register ===========================*/
    /*========================================Signup========================================*/
    public List<User> getAllUsers()
    {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase readDb = this.getReadableDatabase();

        Cursor cursor = null;
        readDb.beginTransaction();
        try
        {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do {
                        User user = new User();
                        user.setID(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)));
                        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD)));
                        userList.add(user);
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
        return  userList;
    }

    public boolean checkSignUp(String userName, String password, List<User> userList)
    {
        boolean signUp = false;
        for (User item : userList)
        {
            String userNameInList = item.getUsername().trim();

            if(userNameInList.equals(userName))
            {
                if(item.getPassword().equals(password))
                {
                    signUp = true;

                    setCurrentID(item.getID());
                    setCurrentUserName(item.getUsername());
                    setCurrentPassword(item.getPassword());

                }
                break;
            }
        }
        return signUp;
    }

    public void setCurrentID(int id)
    {
        this.id = id;
    }

    public int getCurrentID()
    {
        return id;
    }

    public void setCurrentUserName(String userName)
    {
        this.userName = userName;
    }

    public String getCurrentUserName()
    {
        return userName;
    }

    public void setCurrentPassword(String password)
    {
        this.password = password;
    }

    public String getCurrentPassword()
    {
        return password;
    }

    public void addNewUser(User user)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, user.getUsername());
        contentValues.put(PASSWORD, user.getPassword());
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void updateUser(int id, String newPassword)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PASSWORD, newPassword);
        db.update(TABLE_NAME, contentValues, ID + "=?",  new String[] {String.valueOf(id)});
    }

}

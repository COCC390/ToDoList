package com.bc.secretnoteandtodo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bc.secretnoteandtodo.R;
import com.bc.secretnoteandtodo.Register;
import com.bc.secretnoteandtodo.database.DBHelper;
import com.bc.secretnoteandtodo.database.model.User;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static int currentUserID;
    EditText etUserName, etPassword;
    Button btnSignIn, btnSignUp;

    DBHelper db;

    private String userName, password;
    private boolean signUpSuccessful;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        LinkToView();

        db = new DBHelper(this);

        try
        {
            db.createDataBase();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        db.openDatabase();

        userList = db.getAllUsers();

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void LinkToView()
    {
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignup);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.btnSignIn)
        {
            userName = etUserName.getText().toString();
            password = etPassword.getText().toString();

            signUpSuccessful = db.checkSignUp(userName.trim(), password.trim(), userList);

            if(signUpSuccessful)
            {
                Toast.makeText(MainActivity.this, "Sign up succeed!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AllNotes.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(MainActivity.this, "Wrong user name or password!", Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId() == R.id.btnSignup)
        {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        }
    }


}
package com.bc.secretnoteandtodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.bc.secretnoteandtodo.database.DBHelper;
import com.bc.secretnoteandtodo.database.model.User;
import com.bc.secretnoteandtodo.view.AllNotes;
import com.bc.secretnoteandtodo.view.MainActivity;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText etUserName, etPassword, etRePassword;
    private Button btnCreateAccount, btnReturnLogin;

    String userName, password;

    DBHelper db;

    boolean isEqualPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        LinkToView();

        db = new DBHelper(this);
        db.openDatabase();

        btnCreateAccount.setOnClickListener(this);
        btnReturnLogin.setOnClickListener(this);
    }

    void LinkToView()
    {
        etUserName = (EditText) findViewById(R.id.etUserName1);
        etPassword = (EditText) findViewById(R.id.etPassword1);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        btnCreateAccount = (Button) findViewById(R.id.btnSignUp1);
        btnReturnLogin = (Button) findViewById(R.id.btnSignIn1);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.btnSignUp1)
        {
            isEqualPassword = CheckPassAndRePass();

            if(isEqualPassword)
            {
                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();

                User user = new User();
                user.setUsername(userName);
                user.setPassword(password);
                db.addNewUser(user);


                Toast.makeText(Register.this, "Register succeed!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        }
        if(v.getId() == R.id.btnSignIn1)
        {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
        }
    }

    boolean CheckPassAndRePass()
    {
        boolean equal = false;
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        if(password.equals(rePassword))
        {
            equal = true;
        }
        return equal;
    }
}
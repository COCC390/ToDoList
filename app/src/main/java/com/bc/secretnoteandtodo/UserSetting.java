package com.bc.secretnoteandtodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.secretnoteandtodo.database.DBHelper;

import com.bc.secretnoteandtodo.view.AllNotes;
import com.bc.secretnoteandtodo.view.MainActivity;

public class UserSetting extends AppCompatActivity implements View.OnClickListener
{
    EditText etCurrentPassword, etNewPassword, etRePassword;
    Button btnChangePassword, btnLogout, btnBack;
    TextView tvHiUser;

    String refPassword, currentPassword, newPassword, rePassword;
    int currentID;

    boolean isCurrentEqualRef, isNewEqualRePass;


    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        getSupportActionBar().hide();

        LinkToView();

        db = new DBHelper(this);
        db.openDatabase();

        tvHiUser.setText("Hi " + db.getCurrentUserName() + "!");
        refPassword = db.getCurrentPassword();

        btnChangePassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    private void LinkToView()
    {
        btnBack = (Button) findViewById(R.id.btnBack);

        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        etCurrentPassword = (EditText) findViewById(R.id.etCurrentPassword);
        etNewPassword = (EditText) findViewById(R.id.etChangePassword);
        etRePassword = (EditText) findViewById(R.id.etChangeRePassword);
        tvHiUser = (TextView) findViewById(R.id.tvHelloUserName);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.btnChangePassword)
        {
            currentPassword = etCurrentPassword.getText().toString();
            newPassword = etNewPassword.getText().toString();
            rePassword = etRePassword.getText().toString();

            currentID = db.getCurrentID();

            isCurrentEqualRef = CheckOldPassword(currentPassword, refPassword);
            if(isCurrentEqualRef)
            {
                isNewEqualRePass = CheckPassAndRePass();
                if(isNewEqualRePass)
                {
                    Toast.makeText(UserSetting.this, "Change password succeed!", Toast.LENGTH_SHORT).show();
                    db.updateUser(currentID, newPassword);
                }
            }
            else
            {
                Toast.makeText(UserSetting.this, "Your current password not true!", Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId() == R.id.btnLogout)
        {
            Intent intent = new Intent(UserSetting.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId() == R.id.btnBack)
        {
            Intent intent = new Intent(UserSetting.this, AllNotes.class);
            startActivity(intent);
        }
    }

    boolean CheckOldPassword(String currentPassword, String refPassword)
    {
        boolean isEqual = false;
        if(currentPassword.equals(refPassword))
        {
            isEqual = true;
        }
        return isEqual;

    }

    boolean CheckPassAndRePass()
    {
        boolean equal = false;
        String password = etNewPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        if(password.equals(rePassword))
        {
            equal = true;
        }
        return equal;
    }
}
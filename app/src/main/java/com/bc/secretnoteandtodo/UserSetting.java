package com.bc.secretnoteandtodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.secretnoteandtodo.database.DBHelper;
import com.bc.secretnoteandtodo.view.MainActivity;

public class UserSetting extends AppCompatActivity implements View.OnClickListener {
    EditText etCurrentPassword, etNewPassword, etRePassword;
    Button btnChangePassword, btnLogout;
    TextView tvHiUser;

    String currentPassword, newPassword, rePassword;
    int currentID;

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

        btnChangePassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void LinkToView()
    {
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
            CheckPassAndRePass();
            if(CheckPassAndRePass())
            {
                Toast.makeText(UserSetting.this, "Change password succeed!", Toast.LENGTH_SHORT).show();
                db.updateUser(currentID, newPassword);
            }
        }
        if(v.getId() == R.id.btnLogout)
        {
            Intent intent = new Intent(UserSetting.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
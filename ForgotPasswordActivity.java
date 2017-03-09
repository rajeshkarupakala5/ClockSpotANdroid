package com.example.karup.clockspot;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by karup on 12/27/2016.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    EditText mobile;
    String pin;
    EditText username;
    String uname;
    DatabaseHelperActivity helper = new DatabaseHelperActivity(this);

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Forgot Password");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        username = (EditText) findViewById(R.id.forgotUserName);


        mobile = (EditText) findViewById(R.id.mobileNumber);


        Button forgotPassBtn = (Button) findViewById(R.id.resetPasswordBtn);
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                pin = mobile.getText().toString();
                if (username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    username.setBackgroundColor(Color.RED);
                } else if (mobile.getText().toString().equals("")) {
                    username.setBackgroundColor(Color.TRANSPARENT);
                    mobile.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Security pin cannot be empty", Toast.LENGTH_SHORT).show();

                } else if (mobile.getText().toString().length() != 4) {

                    mobile.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Please enter a valid pin", Toast.LENGTH_SHORT).show();
                } else {
                    mobile.setBackgroundColor(Color.TRANSPARENT);
                    String getPassword = "";
                    try {
                        getPassword = helper.forgotPassword(uname, pin);

                        if (getPassword.equals("")) {
                            Toast.makeText(getApplicationContext(), "Username and pin you've provided \ndo not match with any account" +
                                    " \nPlease try again", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Your password is :" + getPassword + "",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
                            finish();
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        Log.d("ForgotPassword", "Caught exception while trying to get password" + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Username and pin you've provided \ndo not match with any account" +
                                " \nPlease try again", Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        ForgotPasswordActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}

package com.example.karup.clockspot;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.ProgressDialog.STYLE_SPINNER;

/**
 * Created by karup on 12/23/2016.
 */

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelperActivity helper = new DatabaseHelperActivity(this);
    private ProgressDialog pDialog;
    Matcher matcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        // Sets the Toolbar to act as the ActionBar for this Activity window.
//        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button registerButton = (Button) findViewById(R.id.registerButton);
//        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        final Button resetButton = (Button) findViewById(R.id.resetButton);


        final EditText username = (EditText) RegisterActivity.this.findViewById(R.id.userName);
        final EditText password = (EditText) RegisterActivity.this.findViewById(R.id.password);
        final EditText confirmPassword = (EditText) RegisterActivity.this.findViewById(R.id.password1);
        final Spinner occupationSpinner = (Spinner) RegisterActivity.this.findViewById(R.id.occupationSpinner);
        final EditText mobile = (EditText) RegisterActivity.this.findViewById(R.id.mobileNumber);


        Pattern pattern = Pattern.compile("^'\'[1-9]{1}[0-9]{9}$");
        matcher = pattern.matcher(mobile.getText().toString());
        final String a = username.getText().toString();
        Log.v("Value of username is " + a + "Something", "");
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Register send data online and go to login page

                Log.v("Value of username is " + a + "Something", "");
                if (username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    username.setBackgroundColor(Color.RED);
                } else if (username.getText().toString().length() < 4 || username.getText().toString().length() > 15) {
                    Toast.makeText(getApplicationContext(), "Username must be between 4 to 15 characters", Toast.LENGTH_SHORT).show();
                    username.setBackgroundColor(Color.RED);
                } else if (password.getText().toString().equals("")) {
                    username.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getApplicationContext(), "Passwords cannot be empty", Toast.LENGTH_SHORT).show();
                    password.setBackgroundColor(Color.RED);
                } else if (confirmPassword.getText().toString().equals("")) {
                    password.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getApplicationContext(), "Passwords cannot be empty", Toast.LENGTH_SHORT).show();
                    confirmPassword.setBackgroundColor(Color.RED);
                } else if (((password.getText().toString().length() < 4) || (password.getText().toString().length() > 15))) {
                    password.setBackgroundColor(Color.RED);
                    confirmPassword.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Passwords length must be between 4 to 15 characters", Toast.LENGTH_SHORT).show();


                } else if ((!(password.getText().toString().equals(confirmPassword.getText().toString())))) {
                    password.setBackgroundColor(Color.RED);
                    confirmPassword.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();


                } else if (mobile.getText().toString().equals("")) {
                    password.setBackgroundColor(Color.TRANSPARENT);
                    confirmPassword.setBackgroundColor(Color.TRANSPARENT);
                    mobile.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Security cannot be empty", Toast.LENGTH_SHORT).show();

                } else if (mobile.getText().toString().length() != 4) {
                    mobile.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Please enter a valid pin", Toast.LENGTH_SHORT).show();
                } else {
                    mobile.setBackgroundColor(Color.TRANSPARENT);

                    //Toast.makeText(getApplicationContext(), "Passwords match" , Toast.LENGTH_SHORT).show();
                    ContactActivity c = new ContactActivity();
                    c.setUname(username.getText().toString());
                    String uname = username.getText().toString();
                    c.setPass(password.getText().toString());
                    c.setOccupation(occupationSpinner.getSelectedItem().toString());
                    c.setMobile(mobile.getText().toString());
                    //Assigned with certain activities in the DB
                    Boolean isUserNameAvailable;

                    isUserNameAvailable = helper.userNameAvailable(uname);

                    if (isUserNameAvailable) {
                        showVerticalProgressDialog();
                        helper.insertContact(c);
                        Toast.makeText(getApplicationContext(), "Registered Successfully !!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                        i.putExtra("uname",uname);
                        dismissProgressDialog();
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Username already taken by someone " +
                                "please try with another username", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Cancel and go back to previous page
//
//            }
//        });

//        cancelButton.setOnClickListener(this);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Reset method called" + username.getText().toString() + password.getText().toString()
                        + confirmPassword.getText().toString()
                        + occupationSpinner.getSelectedItem().toString(), "Data shown and reset");
                username.setText("");
                password.setText("");
                confirmPassword.setText("");
                occupationSpinner.setSelection(0);
            }
        });

        resetButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
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

    public void cancelClick(View view) {

        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        RegisterActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    public void showVerticalProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering data. Please wait...");
        pDialog.setProgressStyle(STYLE_SPINNER);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.cancelButton:
//                Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
//                startActivity(i);
//                break;
//
//
//
//        }

    public void dismissProgressDialog() {
        if ((pDialog != null) && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}

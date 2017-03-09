package com.example.karup.clockspot;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.ProgressDialog.STYLE_SPINNER;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    DatabaseHelperActivity helper = new DatabaseHelperActivity(this);
    boolean is_logged_in = true;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    private static final String USERNAME = "USERNAME";
    String uname;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Clock Spot");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.hide();

        try
        {
            Intent intent = getIntent();
            uname = intent.getExtras().getString("uname");
        }
        catch (Exception e)
        {
            Log.v("HomeActivity","Exception Caught"+e.getMessage());
        }

        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button forgotPasswordButton = (Button) findViewById(R.id.forgotButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        username = (EditText) findViewById(R.id.userName);
        username.setText(uname);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Validate the username and password first
                //Go to login page with login values and show the screen

                String aStr = username.getText().toString();

                EditText b = (EditText) findViewById(R.id.password);
                String bStr = b.getText().toString();
                String pass = helper.searchPass(aStr);

                sharedPreferences = getApplicationContext().getSharedPreferences(USER_LOGGED_IN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (aStr.equals("")) {
                    username.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (username.getText().toString().length() < 4 || username.getText().toString().length() > 15) {
                    Toast.makeText(getApplicationContext(), "Username must be between 4 to 15 characters", Toast.LENGTH_SHORT).show();
                    username.setBackgroundColor(Color.RED);
                } else if (bStr.equals("")) {
                    username.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    b.setBackgroundColor(Color.RED);
                } else if (((b.getText().toString().length() < 4) || (b.getText().toString().length() > 15))) {
                    b.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Passwords length must be between 4 to 15 characters", Toast.LENGTH_SHORT).show();


                } else if (pass.equals(bStr)) {
                    b.setBackgroundColor(Color.TRANSPARENT);
                    showVerticalProgressDialog();
                    editor.putBoolean(USER_LOGGED_IN, is_logged_in);
                    editor.putString(USERNAME, aStr);
                    editor.commit();

                    //Intent i = new Intent(HomeActivity.this, ActivitiesHomeActivity.class);
                    Intent i = new Intent(HomeActivity.this, RecyclerViewActivityHome.class);
                    i.putExtra("uname", aStr);
                    dismissProgressDialog();
                    startActivity(i);

                } else {
                    b.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getBaseContext(), "The username or password that you've \n " +
                            "entered doesn't match any account.\n Sign up for an " +
                            "account.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to forgot screen and allow the user to change the password
                Intent i = new Intent(HomeActivity.this, ForgotPasswordActivity.class);
                startActivity(i);

            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, RegisterActivity.class);
                startActivity(i);
                //GO to register page and register the user using the input entered by them
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create().show();
    }

    public void showVerticalProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Logging in.. Please wait...");
        pDialog.setProgressStyle(STYLE_SPINNER);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void dismissProgressDialog() {
        if ((pDialog != null) && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}

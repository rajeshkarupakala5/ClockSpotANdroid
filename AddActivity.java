package com.example.karup.clockspot;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by karup on 12/23/2016.
 */

public class AddActivity extends AppCompatActivity {
    Dialog d;
    EditText nameOfTheActivity;
    String activityName = null;
    int pos;
    private GridviewAdapter mAdapter;
    private ArrayList<String> activity;
    private ArrayList<Integer> imageId;
    private GridView gridView;
    DatabaseHelperActivity helper = new DatabaseHelperActivity(this);
    String uname;
    boolean is_logged_in = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String MESSAGE = "MESSAGE";
    private static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    private static final String USERNAME = "USERNAME";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent intent = getIntent();
        uname = intent.getExtras().getString("uname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);
        setTitle("Add Activity");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setDisplayHomeAsUpEnabled(true);

        prepareList();

        // prepared arraylist and passed it to the Adapter class
        mAdapter = new GridviewAdapter(this, activity, imageId);

        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                                    long arg3) {
                pos = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(AddActivity.this);
                alert.setTitle("Add Activity ?");
                alert.setMessage("Are you sure you want to add " + mAdapter.getItem(position) + " activity to the list ?");
                alert.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        activityName = mAdapter.getItem(position);
                        Log.v("AddActivity", "Activity Name from Adapter is :" + activityName);


                        if (activityName.equalsIgnoreCase("Other")) {
                            d = new Dialog(AddActivity.this);
                            d.setTitle("Add Activity");
                            d.setContentView(R.layout.custom_activity_name);

                            nameOfTheActivity = (EditText) d.findViewById(R.id.userNameDialog);


                            Button btn = (Button) d.findViewById(R.id.btnSubmit);
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    final String name = nameOfTheActivity.getText().toString();
                                    if (name.equals("")) {
                                        Toast.makeText(getApplicationContext(),
                                                "Please enter a activity name", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                   else if (name.length() >= 16) {
                                        Toast.makeText(getApplicationContext(),
                                                "Please enter a activity name which is less than 15 characters", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    else {
                                        //activityName = name.substring(0,1) + name.substring(1).toLowerCase();
                                        Log.v("AddActivity", "Activity Name from Custom Dialog is :" + name);
                                        dismissDialogBox();
                                        String name1 = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
//                                        Toast.makeText(getApplicationContext(),
//                                                name1, Toast.LENGTH_SHORT)
//                                                .show();
                                        add(name1);
                                    }
                                }
                            });

                            Button cancelBtn = (Button) d.findViewById(R.id.btnCancel);

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialogBox();
                                }
                            });
                            d.show();
                        } else {
                            Log.v("AddActivity", "Activity Name is not 'Other'");
                            Log.v("AddActivity", "Activity Name from Adapter is :" + activityName);
                            dismissDialogBox();
                            add(activityName);

                        }


                    }

                    private void dismissDialogBox() {
                        if ((d != null) && d.isShowing()) {
                            d.dismiss();
                        }
                    }

                    private void add(String activityName) {
                        int message = helper.addActivity(uname, activityName);

                        if(message == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Activity is already in the list", Toast.LENGTH_SHORT)
                                    .show();
                            dismissDialogBox();
                            finish();
                        }

                        if(message == 1) {
                            Toast.makeText(getApplicationContext(),
                                    "Activity added successfully to the list", Toast.LENGTH_SHORT)
                                    .show();
                            sharedPreferences = getApplicationContext().getSharedPreferences(MESSAGE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(MESSAGE, 1);
                            editor.commit();

                            Intent intent = new Intent(AddActivity.this,RecyclerViewActivityHome.class);
                            intent.putExtra("uname", uname);
                            startActivity(intent);
                           // finish();
                        }

                        if(message == 2) {
                            Toast.makeText(getApplicationContext(),
                                    "Activity was not added to the list, please try again", Toast.LENGTH_SHORT)
                                    .show();
                            dismissDialogBox();
                            finish();
                        }

                    }
                });
                alert.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                //
                // Toast.makeText(AddActivity.this, mAdapter.getItem(pos), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.homePage:
                finish();
//                intent = new Intent(AddActivity.this, RecyclerViewActivityHome.class);
//                intent.putExtra("uname", uname);
//                startActivity(intent);
                //showToast("Home Clicked");
                return true;
            case R.id.settings:
                intent = new Intent(AddActivity.this, SettingsActivity.class);
                intent.putExtra("uname", uname);
                startActivity(intent);
                //showToast("Settings Clicked");
                return true;
            case R.id.logout:
                //showToast("Logout Clicked");
                sharedPreferences = getApplicationContext().getSharedPreferences(USER_LOGGED_IN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(USER_LOGGED_IN, is_logged_in);
                editor.putString(USERNAME, uname);
                editor.commit();

                new AlertDialog.Builder(this)
                        .setTitle("Logout?")
                        .setMessage("Are you sure you want to log out?")
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                intent = new Intent(AddActivity.this, HomeActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void prepareList() {
        activity = new ArrayList();

        activity.add("Transport");
        activity.add("Walk");
        activity.add("Cinema");
        activity.add("Sport");
        activity.add("Read");
        activity.add("Internet");
        activity.add("Housework");
        activity.add("Work");
        activity.add("Sleep");
        activity.add("Shop");
        activity.add("Entertainment");
        activity.add("Drink");
        activity.add("Eat");
        activity.add("Study");
        activity.add("Other");

        imageId = new ArrayList();
        imageId.add(R.drawable.transport);
        imageId.add(R.drawable.walk);
        imageId.add(R.drawable.cinema);
        imageId.add(R.drawable.sport);
        imageId.add(R.drawable.read);
        imageId.add(R.drawable.internet);
        imageId.add(R.drawable.housework);
        imageId.add(R.drawable.work);
        imageId.add(R.drawable.sleep);
        imageId.add(R.drawable.shop);
        imageId.add(R.drawable.entertainment);
        imageId.add(R.drawable.drink);
        imageId.add(R.drawable.eat);
        imageId.add(R.drawable.study);
        imageId.add(R.drawable.other);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        AddActivity.super.onBackPressed();
                    }
                }).create().show();
    }


}
package com.example.karup.clockspot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.ProgressDialog.STYLE_SPINNER;

/**
 * Created by karup on 2/1/2017.
 */

public class RecyclerViewActivityHome extends AppCompatActivity {

    ProgressDialog pDialog;
    Intent intent;
    Activity mActivity;
    DatabaseHelperActivity helper = new DatabaseHelperActivity(this);
    String uname;
    RecyclerAdapterActivity r;
    Cursor cursor;
    ArrayList<String> activityList;
    String activityName;
    Toast t;



    boolean is_logged_in = false;
    SharedPreferences sharedPreferences;
    private static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    private static final String USERNAME = "USERNAME";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.options_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        boolean is_logged_in = true;
        mActivity = this;
        final Intent intent = getIntent();
        uname = intent.getExtras().getString("uname");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_recycler_view_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle(uname.toUpperCase()+" Activities");

        cursor = helper.retriveData(uname);

        if (cursor.getCount() == 0) {
            if(t != null) {
                t.cancel();
            }
            Toast.makeText(getApplicationContext(),"No activities in the list \n Please add an activity",Toast.LENGTH_SHORT).show();

            final Intent myIntent = new Intent(RecyclerViewActivityHome.this, AddActivity.class);
            myIntent.putExtra("uname", uname);

            (new Handler())
                    .postDelayed(
                            new Runnable() {
                                public void run() {
                                    // launch your activity here
                                    startActivity(myIntent);
                                }
                            }, 3500);
           //startActivity(myIntent);

        }

        final RecyclerAdapterActivity adapter = new RecyclerAdapterActivity(getApplicationContext(), cursor);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        activityList = helper.getActivitiesName(uname);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Movie movie = movieList.get(position);
             //  Toast.makeText(getApplicationContext(), activityList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, final int position) {
                 activityName = activityList.get(position);
                new AlertDialog.Builder(RecyclerViewActivityHome.this)
                        .setTitle("Really Delete?")
                        .setMessage("Are you sure you want to delete "+activityName+" activity and all it's logs?")
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                try {
                                    helper.deleteActivity(uname, activityName);
                                  t = Toast.makeText(getApplicationContext(),activityName+" log deleted successfully!!",Toast.LENGTH_SHORT);
                                    t.show();
                                }
                                catch (Exception e)
                                {
                                    Log.v("RECYCLERVIEW","Exception Caught"+e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Unable to delete activity," +
                                            " Please try again",Toast.LENGTH_SHORT).show();
                                }
                                finish();
                                //overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                // overridePendingTransition(0, 0);
                            }
                        }).create().show();


            }
        }));

        recyclerView.setAdapter(adapter);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Intent intent = getIntent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        finish();
//        startActivity(intent);
//    }

    public String getMyData() {
        return uname;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.addActivity:
              Intent myIntent = new Intent(RecyclerViewActivityHome.this.getBaseContext(), AddActivity.class);
                myIntent.putExtra("uname", uname);
              //  dismissProgressDialog();
                startActivityForResult(myIntent, 0);

//                Intent myIntent = new Intent(RecyclerViewActivityHome.this.getBaseContext(), AddActivity.class);
//                myIntent.putExtra("uname", uname);
//                startActivityForResult(myIntent, 0);

                //showToast("Add Activity Clicked");
                return true;
            case R.id.viewProgress:
                showVerticalProgressDialog();
                // showToast("View Progress Clicked");

                myIntent = new Intent(RecyclerViewActivityHome.this.getBaseContext(), TotalTimer.class);
                myIntent.putExtra("uname", uname);
                dismissProgressDialog();
                startActivityForResult(myIntent, 0);
//
//                intent = new Intent(RecyclerViewActivityHome.this, TotalTimer.class);
//                intent.putExtra("uname", uname);
//
//                startActivity(intent);
                return true;
            case R.id.settings:

                myIntent = new Intent(RecyclerViewActivityHome.this.getBaseContext(), SettingsActivity.class);
                myIntent.putExtra("uname", uname);
                startActivityForResult(myIntent, 0);
//                intent = new Intent(RecyclerViewActivityHome.this, SettingsActivity.class);
//                intent.putExtra("uname", uname);
//                startActivity(intent);
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
                                intent = new Intent(RecyclerViewActivityHome.this, HomeActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }).create().show();


                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showVerticalProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Progress View Loading.. Please wait...");
        pDialog.setProgressStyle(STYLE_SPINNER);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void dismissProgressDialog() {
        if ((pDialog != null) && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


}

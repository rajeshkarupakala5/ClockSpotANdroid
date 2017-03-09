package com.example.karup.clockspot;

/**
 * Created by karup on 1/29/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;


public class TotalTimer extends AppCompatActivity implements android.support.v7.app.ActionBar.TabListener {
    boolean is_logged_in = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    private static final String USERNAME = "USERNAME";
    Intent intent;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    String uname;

    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        uname = intent.getExtras().getString("uname");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_timer_activity);
        setTitle("Total Time");
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        // Set up the action bar.
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appbarcolor)));

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);


        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

//         For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
                            //.setText(Html.fromHtml("<font color=\"black\">"
                    //+ mAppSectionsPagerAdapter.getPageTitle(i) + "</font>"));

        }


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
//                intent = new Intent(TotalTimer.this, RecyclerViewActivityHome.class);
//                intent.putExtra("uname", uname);
//                startActivity(intent);
                finish();
                //showToast("Home Clicked");
                return true;
            case R.id.settings:
                intent = new Intent(TotalTimer.this, SettingsActivity.class);
                intent.putExtra("uname", uname);
                startActivity(intent);
                //showToast("Settings Clicked");
                return true;
            case R.id.logout:
                // showToast("Logout Clicked");
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
                                intent = new Intent(TotalTimer.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getMyData() {
        return uname;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go Back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        TotalTimer.super.onBackPressed();
                    }
                }).create().show();
    }


//    @Override
//    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//    }
//
//    @Override
//    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//        // When the given tab is selected, switch to the corresponding page in the ViewPager.
//        mViewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }


    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new ProgressFragment();
                case 1:
                    return new LogsFragment();
                default:
                    return new ProgressFragment();
                // The other sections of the app are dummy placeholders.
//                    Fragment fragment = new LogsFragment();
//                Bundle args = new Bundle();
//                args.putInt(LogsFragment.ARG_SECTION_NUMBER, i + 1);
//                fragment.setArguments(args);
//                return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 1) {
                return "Timer";
            } else {
                return "Pie Chart";
            }


        }


    }

}





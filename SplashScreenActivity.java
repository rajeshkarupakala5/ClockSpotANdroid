package com.example.karup.clockspot;

/**
 * Created by karup on 1/15/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

    private boolean is_logged_in = false;
    private String uname;
    private static final String USER_LOGGED_IN = "USER_LOGGED_IN";
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private SharedPreferences sharedPreferences;
    private static final String USERNAME = "USERNAME";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splashscreen);
        sharedPreferences = getSharedPreferences(USER_LOGGED_IN, Context.MODE_PRIVATE);


        //read
        boolean defaultValue = false;
        is_logged_in = sharedPreferences.getBoolean(USER_LOGGED_IN, defaultValue);
        uname = sharedPreferences.getString(USERNAME, uname);

        //
        if (!is_logged_in) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashScreenActivity.this, RecyclerViewActivityHome.class);
                    mainIntent.putExtra("uname", uname);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }


    }
}


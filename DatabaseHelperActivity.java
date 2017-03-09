package com.example.karup.clockspot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by karup on 12/27/2016.
 */

public class DatabaseHelperActivity extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_ID = "_id";     // a column named "_id" is required for cursor
    private static final String DATABASE_NAME = "activities";
    private static final String TABLE_NAME = "contacts";
    private static final String TABLE_NAME1 = "activities";
    private static final String COLUMN_ACTIVITYNAME = "activity";
    private static final String COLUMN_TIMER = "time";
    private static final String COLUMN_TOTAL_TIMER = "totalTime";
    private static final String COLUMN_USERNAME = "uname";
    private static final String COLUMN_USERNAME1 = "username";
    private static final String COLUMN_PASSWORD = "pass";
    private static final String COLUMN_OCCUPATION = "occupation";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String ACTIVITY_TIME = "00:00:00";


    ArrayList<ItemsActivity> myItems = new ArrayList<ItemsActivity>() {{
        add(new ItemsActivity("Eat", ACTIVITY_TIME));
        add(new ItemsActivity("Sleep", ACTIVITY_TIME));
        add(new ItemsActivity("Workout", ACTIVITY_TIME));
        add(new ItemsActivity("Drink", ACTIVITY_TIME));
        add(new ItemsActivity("Entertainment", ACTIVITY_TIME));
        add(new ItemsActivity("Shop", ACTIVITY_TIME));
    }};
    SQLiteDatabase db;


    public DatabaseHelperActivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String buildSQL = "CREATE TABLE "
                + TABLE_NAME + "( "
                + COLUMN_USERNAME + " TEXT PRIMARY KEY , "
                + COLUMN_PASSWORD + " TEXT not null , "
                + COLUMN_MOBILE + " TEXT not null ,"
                + COLUMN_IMAGE + " TEXT not null ,"
                + COLUMN_OCCUPATION + " TEXT not null)";

        String buildSQL1 = "CREATE TABLE "
                + TABLE_NAME1 + "( "
                + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
                + COLUMN_ACTIVITYNAME + " TEXT , "
                + COLUMN_TIMER + " TEXT,"
                + COLUMN_TOTAL_TIMER + " TEXT,"
                + COLUMN_USERNAME1 + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_USERNAME1 + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_USERNAME + "));";

        //+ COLUMN_USERNAME +" TEXT FOREIGN KEY REFERENCES TABLE_NAME(COLUMN_USERNAME), "


        Log.d("TAG", "**********on Create ************");

        db.execSQL(buildSQL);
        db.execSQL(buildSQL1);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String buildSQL1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
        db.execSQL(buildSQL1);
        db.execSQL(buildSQL);       // drop previous table
        this.onCreate(db);

    }

    public void insertContact(ContactActivity c) {
        Log.d("TAG", "**Username Value**" + c.getUname());
        db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, c.getUname());
        values.put(COLUMN_PASSWORD, c.getPass());
        values.put(COLUMN_OCCUPATION, c.getOccupation());
        values.put(COLUMN_MOBILE, c.getMobile());
        values.put(COLUMN_IMAGE, "imageSrc");
        db.insert(TABLE_NAME, null, values);

        switch (c.getOccupation()) {
            case "Student":
                myItems.add(new ItemsActivity("Study", ACTIVITY_TIME));
                myItems.add(new ItemsActivity("School", ACTIVITY_TIME));
                break;
            case "Engineer":
                myItems.add(new ItemsActivity("Work", ACTIVITY_TIME));
                break;
            case "Doctor":
                myItems.add(new ItemsActivity("Hospital", ACTIVITY_TIME));
                myItems.add(new ItemsActivity("Patient", ACTIVITY_TIME));
                break;
            case "Lawyer":
                myItems.add(new ItemsActivity("Court", ACTIVITY_TIME));
                myItems.add(new ItemsActivity("Office", ACTIVITY_TIME));
                break;
            default:
                Log.v("TAG", "***Occupation Other***");
        }

        for (int i = 0; i < myItems.size(); i++) {
            ContentValues values1 = new ContentValues();
            values1.put(COLUMN_ACTIVITYNAME, myItems.get(i).getActivityName());
            values1.put(COLUMN_TIMER, myItems.get(i).getTimer());
            values1.put(COLUMN_TOTAL_TIMER, myItems.get(i).getTimer());
            values1.put(COLUMN_USERNAME1, c.getUname());
            db.insert(TABLE_NAME1, null, values1);
        }
        db.close();


    }

    public String searchPass(String uname) {
        db = this.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        String query = "select uname, pass from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a;
        String b = "not found";

        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                Log.v("Username", "" + a);
                if (a.equals(uname)) {
                    b = cursor.getString(1);
                    Log.v("Password", "=" + b);
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        return b;

    }

    public Cursor retriveData(String uname) {
        db = this.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        String query = "select * from " + TABLE_NAME1 + " where " + COLUMN_USERNAME1 + " = '" + uname + "'";
        Log.v("TAG", "Query :" + query);
        //+ " where "+COLUMN_USERNAME1+ " = " +uname;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }

    public String forgotPassword(String uname, String pin) {
        String pass = "";
        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_NAME + " where " + COLUMN_USERNAME + " = '" + uname + "' " +
                "and " + COLUMN_MOBILE + " = '" + pin + "'; ";
        Log.v("DBHELPER", "Query" + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            Log.v("DBHELPER", "Cursor is Null");
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                pass = cursor.getString(cursor.getColumnIndex("pass"));
                if (pass.equals("")) {
                } else {
                    return pass;
                }
            }
            cursor.moveToNext();
        }
        return pass;
    }

    public ArrayList<String> getActivitiesName(String uname) {
        ArrayList<String> activityList = new ArrayList<>();
        db = this.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        String query = "select * from " + TABLE_NAME1 + " where " + COLUMN_USERNAME1 + " = '" + uname + "'";
        Log.v("TAG", "Query :" + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String dir = cursor.getString(cursor.getColumnIndex("activity"));
                    Log.v("getActivitiesName", "Activity Name" + dir);
                    activityList.add(dir);
                } while (cursor.moveToNext());
            }
        } else {
            Log.v("getActivitiesName", "Cursor is null");
        }


        cursor.close();


        return activityList;
    }

    public ArrayList<String> getTimer(String uname) {
        ArrayList<String> timerList = new ArrayList<>();
        db = this.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        String query = "select * from " + TABLE_NAME1 + " where " + COLUMN_USERNAME1 + " = '" + uname + "'";
        Log.v("TAG", "Query :" + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String data = cursor.getString(cursor.getColumnIndex("totalTime"));
                Log.d("DB", "GetTimer" + data);
                timerList.add(data);
                cursor.moveToNext();
            }
        }


        return timerList;
    }

    public int addActivity(String uname, String activityName) {
        int message = 0;

        ArrayList<String> activitiesname = new ArrayList<>();
        activitiesname = getActivitiesName(uname);

        for (String activity : activitiesname) {
            if (activityName.equalsIgnoreCase(activity)) {

                return message = 0;
            }
        }

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ACTIVITYNAME, activityName);
            values.put(COLUMN_TIMER, ACTIVITY_TIME);
            values.put(COLUMN_USERNAME1, uname);
            values.put(COLUMN_TOTAL_TIMER, ACTIVITY_TIME);
            db.insert(TABLE_NAME1, null, values);

            db.close();

            return message = 1;

        } catch (Exception e) {
            return message = 2;
        }

    }

    public Boolean userNameAvailable(String uname) {
        Boolean result = true;
        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String data = cursor.getString(cursor.getColumnIndex("uname"));
                Log.v("TAG", "****UserName Available Username:" + data);
                if (uname.equals(data))
                    result = false;
                return result;
            }
            cursor.moveToNext();
        }


        return result;
    }

    public void changePassword(String uname, String password1) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password1);
        db.update(TABLE_NAME, values, COLUMN_USERNAME + " = '" + uname + "'", null);
        db.close();

    }

    public void changePin(String uname, String securityPin1) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOBILE, securityPin1);
        db.update(TABLE_NAME, values, COLUMN_USERNAME + " = '" + uname + "'", null);
        db.close();
    }

    public void setImagePath(String uname, String picturePath) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, picturePath);
        db.update(TABLE_NAME, values, COLUMN_USERNAME + " = '" + uname + "'", null);
        db.close();
    }


    public String getImagePath(String uname) {
        String imagePath = "";


        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_NAME + " where " + COLUMN_USERNAME + " = '" + uname + "'; ";
        Log.v("DBHELPER", "Query at getImagePath" + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            Log.v("DBHELPER : getImagePath", "Cursor is Null");
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                imagePath = cursor.getString(cursor.getColumnIndex("image"));
                if (imagePath.equals("")) {
                } else {
                    return imagePath;
                }
            }
            cursor.moveToNext();
        }
        return imagePath;
    }

    public void updateTimer(String username, String totalTime, String activityName) {
        String total = "00:00:00";
        int sec;
        int sec1;
        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_NAME1 + " where " + COLUMN_USERNAME1 + " = '" + username + "' " +
                "and " + COLUMN_ACTIVITYNAME + " = '" + activityName + "'; ";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            total = cursor.getString(cursor.getColumnIndex("totalTime"));
            Log.v("DBHELPER", "UPDATE TIMER VALUE" + total);

        }


        String[] seconds = total.split(":");
        sec = Integer.parseInt(seconds[0]) * 3600 + Integer.parseInt(seconds[1]) * 60 + Integer.parseInt(seconds[2]) * 1;


        String[] seconds1 = totalTime.split(":");
        sec1 = Integer.parseInt(seconds1[0]) * 3600 + Integer.parseInt(seconds1[1]) * 60 + Integer.parseInt(seconds1[2]) * 1;

        sec1 += sec;

        String asText = String.format("%02d:%02d:%02d", (sec1 / 3600),
                (sec1 / 60) % 60, (sec1 % 60));

        Log.v("DBHELPER", "UPDATED TIMER STRING" + asText);


        db = this.getWritableDatabase();
        Log.v("DBHELPER", "TIMER VALUE" + totalTime);
        Log.v("DBHELPER", "ACTIVITY VALUE" + activityName);

        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL_TIMER, asText);

        db.update(TABLE_NAME1,
                values,
                COLUMN_USERNAME1 + " = ? AND " + COLUMN_ACTIVITYNAME + " = ?",
                new String[]{username, activityName});

        db.close();


    }

    public void deleteActivity(String uname, String activityName) {

        db = this.getWritableDatabase();

        db.delete(TABLE_NAME1,COLUMN_USERNAME1+ " = ? and " + COLUMN_ACTIVITYNAME + " = ?",
                new String[]{uname,activityName});

        db.close();
    }
}
package com.outcube.bikeroute.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseProfile extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ProfileHistory";
 
    // History table name
    private static final String TABLE_PROFILE = "ProfileList";
 
    // History Table Columns names
    private static final String KEY_DISTANCE = "jour_dist";
    private static final String KEY_MAXSPEED = "jour_maxspeed";
    private static final String KEY_CALORIES = "jour_cal";
    private static final String KEY_STARTTIME = "jour_startdatetime";
    private static final String KEY_STOPTIME = "jour_stopdatetime";
    private static final String KEY_STARTPOINT = "jour_startpoint";
    private static final String KEY_ENDPOINT = "jour_endpoint";
    
    

    public DatabaseProfile(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_PROFILE + "("
        		+ KEY_DISTANCE + " INTEGER," + KEY_MAXSPEED + " INTEGER," + KEY_CALORIES + " INTEGER,"
                + KEY_STARTTIME + " TEXT," + KEY_STOPTIME + " TEXT," + 
                KEY_STARTPOINT + " TEXT," + KEY_ENDPOINT + " TEXT" + ")";
        db.execSQL(CREATE_EVENT_TABLE);
    }
    

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        // Create tables again
        onCreate(db);
    }
    

	
}

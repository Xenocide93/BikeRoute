package com.outcube.bikeroute.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new journey
    public void addProfile(ProfileForDB profile) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_CALORIES, profile.getJour_cal());
        values.put(KEY_DISTANCE, profile.getJour_dist());
        values.put(KEY_ENDPOINT, profile.getJour_endpoint());
        values.put(KEY_MAXSPEED, profile.getJour_maxspeed());
        values.put(KEY_STARTPOINT, profile.getJour_startpoint());
        values.put(KEY_STARTTIME, profile.getJour_startdatetime());
        values.put(KEY_STOPTIME, profile.getJour_stopdatetime());
 
        // Inserting Row
        db.insert(TABLE_PROFILE, null, values);
        db.close(); // Closing database connection
    }
     
    // Getting All journeys
    public List<ProfileForDB> getAllProfiles() {
        List<ProfileForDB> profileList = new ArrayList<ProfileForDB>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	ProfileForDB profile = new ProfileForDB(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), 
            			Integer.parseInt(cursor.getString(2)),cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                profileList.add(profile);
            } while (cursor.moveToNext());
        }
        return profileList;
    }
 
    public void deleteAll() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_PROFILE, null, null);
    	db.close();
    }
 
 
    // Getting event Count
    public int getEventCount() {
        String historyQuery = "SELECT  * FROM " + TABLE_PROFILE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(historyQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }
	
	
}

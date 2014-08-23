package com.outcube.bikeroute.database;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseEvents extends SQLiteOpenHelper{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "EventsHistory";
 
    // History table name
    private static final String TABLE_EVENT = "EventsList";
 
    // History Table Columns names
    private static final String KEY_ID = "event_id";
    private static final String KEY_NAME = "event_name";
    private static final String KEY_DESCRIPTION = "event_desc";
    private static final String KEY_PHOTO = "event_photo";
    private static final String KEY_LOCATION = "event_location";
    private static final String KEY_START = "event_startdate";
    private static final String KEY_END = "event_enddate";
    

    public DatabaseEvents(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "("
        		+ KEY_ID + " INTEGER," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
                + KEY_PHOTO + " TEXT," + KEY_LOCATION + " TEXT," + 
                KEY_START + " TEXT," + KEY_END + " TEXT" + ")";
        db.execSQL(CREATE_EVENT_TABLE);
    }
    

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        // Create tables again
        onCreate(db);
    }
    

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new event
    public void addEvent(EventsForDB event) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, event.getEvent_desc());
        values.put(KEY_END, event.getEvent_enddate());
        values.put(KEY_ID, event.getId());
        values.put(KEY_LOCATION, event.getEvent_location());
        values.put(KEY_NAME, event.getEvent_name());
        values.put(KEY_PHOTO, event.getEvent_photo());
        values.put(KEY_START, event.getEvent_startdate());
 
        // Inserting Row
        db.insert(TABLE_EVENT, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single event
    public EventsForDB getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_EVENT, new String[] { KEY_ID,
        		KEY_NAME, KEY_DESCRIPTION, KEY_PHOTO, KEY_LOCATION, KEY_START, KEY_END }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        EventsForDB event = new EventsForDB(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), 
        		cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        return event;
    }
     
    // Getting All events
    public List<EventsForDB> getAllEvents() {
        List<EventsForDB> eventList = new ArrayList<EventsForDB>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	EventsForDB event = new EventsForDB(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), 
                		cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        // return contact list
        return eventList;
    }
 
    // Updating single event
    public int updateEvent(EventsForDB event) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        values.put(KEY_DESCRIPTION, event.getEvent_desc());
        values.put(KEY_END, event.getEvent_enddate());
        values.put(KEY_ID, event.getId());
        values.put(KEY_LOCATION, event.getEvent_location());
        values.put(KEY_NAME, event.getEvent_name());
        values.put(KEY_PHOTO, event.getEvent_photo());
        values.put(KEY_START, event.getEvent_startdate());
        
        // updating row
        return db.update(TABLE_EVENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
    }
 
    // Deleting single event
    public void deleteEvent(EventsForDB event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }
    
    public void deleteAll() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_EVENT, null, null);
    	db.close();
    }
 
 
    // Getting event Count
    public int getEventCount() {
        String historyQuery = "SELECT  * FROM " + TABLE_EVENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(historyQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }
	
 
}

package com.outcube.bikeroute.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.outcube.bikeroute.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseEventsJoined extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "EventsJoinedHistory";
 
    // History table name
    private static final String TABLE_JOIN = "EventsJoinedList";
 
    // History Table Columns names
    private static final String KEY_ID = "event_id";
    private static final String KEY_JOIN = "join_status";
    private static final String KEY_STATUS = "status";
    

    public DatabaseEventsJoined(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_JOIN + "("
        		+ KEY_ID + " INTEGER," + KEY_JOIN + " INTEGER," + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_EVENT_TABLE);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOIN);
        // Create tables again
        onCreate(db);
    }
    

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    public void addEventJoined(JoinForDB join) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, join.getEvent_id());
        values.put(KEY_JOIN, join.getJoin_status());
        values.put(KEY_STATUS, join.getStatus());
 
        // Inserting Row
        db.insert(TABLE_JOIN, null, values);
        db.close(); // Closing database connection
    }
 
    public JoinForDB getEventJoined(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        JoinForDB join = null;
        Cursor cursor = db.query(TABLE_JOIN, new String[] { KEY_ID,
        		KEY_JOIN, KEY_STATUS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
        	if(cursor.moveToFirst()) join = new JoinForDB(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
        }
        return join;
    }
     
    public List<JoinForDB> getAllEventsJoined() {
        List<JoinForDB> joinList = new ArrayList<JoinForDB>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_JOIN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	JoinForDB join = new JoinForDB(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2));
            	joinList.add(join);
            } while (cursor.moveToNext());
        }
        // return contact list
        return joinList;
    }
 
    public int updateEventJoined(JoinForDB join) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, join.getEvent_id());
        values.put(KEY_JOIN, join.getJoin_status());
        values.put(KEY_STATUS, join.getStatus());
        
        // updating row
        return db.update(TABLE_JOIN, values, KEY_ID + " = ?",
                new String[] { String.valueOf(join.getEvent_id()) });
    }
 
    // Getting event Count
    public int getEventJoinedCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_JOIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }   
        return count;
    }
	
    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeJSONfromSQLite(){
    	ArrayList<HashMap<String, Integer>> joinList;
    	joinList = new ArrayList<HashMap<String,Integer>>();
    	String selectQuery = "SELECT * FROM " + TABLE_JOIN + " where " + KEY_STATUS + " = 'no'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
            	HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("event_id", Integer.parseInt(cursor.getString(0)));
                map.put("join_status", Integer.parseInt(cursor.getString(1)));
                map.put("user_id", MainActivity.user_id);
                joinList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(joinList);
    }
 
    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "SQLite and Remote MySQL DBs are in Sync!";
        }else{
            msg = "DB Sync needed\n";
        }
        return msg;
    }
 
    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABLE_JOIN + " where " + KEY_STATUS + " = 'no'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }
 
    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void updateSyncStatus(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update " + TABLE_JOIN + " set " + KEY_STATUS + " = '" + status + "' where " + KEY_ID + "='" + id + "'";
        database.execSQL(updateQuery);
        database.close();
    }
	
}

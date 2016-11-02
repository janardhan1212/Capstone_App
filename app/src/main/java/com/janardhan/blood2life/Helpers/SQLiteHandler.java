/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.janardhan.blood2life.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "bloodtolife";

	// Login table name
	private static final String TABLE_LOGIN = "user_table";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_UID = "uid";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_PHNO = "phno";
	private static final String KEY_CAT = "cat";
	private static final String KEY_GROUP = "bgroup";
	private static final String KEY_CITY= "city";
	private static final String KEY_STATE = "state";
	private static final String KEY_pic_url = "profile_pic_url";
	private static final String KEY_TOKEN = "refreshedToken";


	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"+KEY_UID + " TEXT," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_PHNO + " INTEGER,"
				+ KEY_GROUP + " TEXT ,"
				+ KEY_CITY + " TEXT ,"
				+ KEY_STATE + " TEXT ,"
				+ KEY_pic_url + " TEXT ,"
				+ KEY_TOKEN + " TEXT " + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String uid, String name, String email, String phno, String bgroup, String city, String state, String refreshedToken, String pro_url) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_UID, uid);
		values.put(KEY_NAME, name);
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_PHNO, phno); // Name
		values.put(KEY_GROUP, bgroup); // Email
		values.put(KEY_CITY, city);
		values.put(KEY_STATE, state); // Email
		values.put(KEY_pic_url, pro_url);
		values.put(KEY_TOKEN,refreshedToken);
		// Created At

		// Inserting Row
		long id = db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put(KEY_UID, cursor.getString(1));
			user.put("name", cursor.getString(2));

			user.put("email", cursor.getString(3));
			user.put("phno", cursor.getString(4));
			user.put("bgroup", cursor.getString(5));
			user.put("city", cursor.getString(6));
			user.put("state", cursor.getString(7));
			user.put("profile_pic_url", cursor.getString(8));
			user.put("refreshedToken", cursor.getString(9));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}

package com.mapplas.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mapplas.model.SearchValue;
import com.mapplas.model.User;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "MapplasDB";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create user table
		String CREATE_USER_TABLE = "CREATE TABLE " + User.TABLE_USERS + " ( " + "id INTEGER PRIMARY KEY, " + "imei TEXT, " + "pinnedApps TEXT, " + "blockedApps TEXT" + " )";
		String CREATE_SEARCH_VALUES_TABLE = "CREATE TABLE " + SearchValue.TABLE_SEARCHVALUES + " ( " + "id INTEGER PRIMARY KEY, " + "name1 TEXT, " + "name2 TEXT, " + " )";

		// create books table
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_SEARCH_VALUES_TABLE);

		SearchValue s1 = new SearchValue();
		s1.setId(1);
		s1.setName1("Nueva York");
		s1.setName2("New York");
		this.insertSearchValue(s1);

		SearchValue s2 = new SearchValue();
		s2.setId(1);
		s2.setName1("Donostia");
		s2.setName2("San Sebasti‡n");
		this.insertSearchValue(s2);

		SearchValue s3 = new SearchValue();
		s3.setId(1);
		s3.setName1("Madrid");
		s3.setName2("");
		this.insertSearchValue(s3);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older user table if existed
		db.execSQL("DROP TABLE IF EXISTS user");

		// create fresh users table
		this.onCreate(db);
	}

	/**
	 * 
	 * User DB
	 * 
	 */

	public void insertOrUpdateUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(User.KEY_ID, user.getId());
		values.put(User.KEY_IMEI, user.getImei());
		values.put(User.KEY_PINNEDAPPS, user.pinnedApps().toString());
		values.put(User.KEY_BLOKEDAPPS, user.blockedApps().toString());

		int id = (int)db.insertWithOnConflict(User.TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		if(id == -1) {
			db.update(User.TABLE_USERS, values, "id=?", new String[] { String.valueOf(user.getId()) });
		}

		db.close();
	}

	public User getUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		// 2. build query
		Cursor cursor = db.query(User.TABLE_USERS, User.COLUMNS, " id = ?", // c.
																			// selections
			new String[] { String.valueOf(id) }, // d. selections args
			null, // e. group by
			null, // f. having
			null, // g. order by
			null); // h. limit

		if(cursor != null)
			cursor.moveToFirst();

		User user = new User();
		user.setId(Integer.parseInt(cursor.getString(0)));
		user.setImei(cursor.getString(1));
		// user.setPinnedApps(cursor.getString(2));
		// user.setBlockedApps(cursor.getString(3));

		Log.d("getUser(" + id + ")", user.toString());

		return user;
	}

	/**
	 * 
	 * SearchValue DB
	 * 
	 */

	public void insertSearchValue(SearchValue value) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SearchValue.KEY_ID, value.getId());
		values.put(SearchValue.KEY_NAME1, value.getName1());
		values.put(SearchValue.KEY_NAME2, value.getName2());

		db.insert(SearchValue.TABLE_SEARCHVALUES, null, values);

		db.close();
	}
}

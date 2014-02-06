package com.mapplas.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		String CREATE_USER_TABLE = "CREATE TABLE user ( " + "id INTEGER PRIMARY KEY, " + "imei TEXT, " + "pinnedApps TEXT, " + "blockedApps TEXT" + " )";

		// create books table
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older user table if existed
		db.execSQL("DROP TABLE IF EXISTS user");

		// create fresh users table
		this.onCreate(db);
	}

	public void insertOrUpdateUser(User user) {
		Log.d("addUser", user.toString());

		// 1. get reference to writable DB
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

}

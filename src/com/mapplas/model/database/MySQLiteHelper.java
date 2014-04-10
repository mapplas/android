package com.mapplas.model.database;

import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mapplas.model.SearchValue;
import com.mapplas.model.User;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private Context context;

	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "MapplasDB";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create user table
		String CREATE_USER_TABLE = "CREATE TABLE " + User.TABLE_USERS + " ( " + "id INTEGER PRIMARY KEY, " + "imei TEXT, " + "pinnedApps TEXT, " + "blockedApps TEXT" + " )";
		String CREATE_SEARCH_VALUES_TABLE = "CREATE TABLE " + SearchValue.TABLE_SEARCHVALUES + " ( " + "id INTEGER PRIMARY KEY, " + "name1 TEXT, " + "name1_clean TEXT, " + "name2 TEXT, " + "name2_clean TEXT" + " )";

		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_SEARCH_VALUES_TABLE);

		new DbPopulator(this.context, db).populate();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("UPGRADE", "onUpgrade");
		if (oldVersion == 1 && newVersion == 2) {
			Log.d("UPGRADE", "onUpgrade oldVersion == 1 && newVersion == 2");
			// Remove data from TABLE_SEARCHVALUES
			String REMOVE_SEARCH_VALUES_DATA = "DELETE FROM " + SearchValue.TABLE_SEARCHVALUES;
			db.execSQL(REMOVE_SEARCH_VALUES_DATA);
			
			new DbPopulator(this.context, db).populate();
		}
		
		else if (oldVersion == 2 && newVersion == 3) {
			Log.d("UPGRADE", "onUpgrade oldVersion == 2 && newVersion == 3");
			// Remove data from TABLE_SEARCHVALUES
			String REMOVE_SEARCH_VALUES_DATA = "DROP TABLE " + SearchValue.TABLE_SEARCHVALUES;
			db.execSQL(REMOVE_SEARCH_VALUES_DATA);
			
			String CREATE_SEARCH_VALUES_TABLE = "CREATE TABLE " + SearchValue.TABLE_SEARCHVALUES + " ( " + "id INTEGER PRIMARY KEY, " + "name1 TEXT, " + "name1_clean TEXT, " + "name2 TEXT, " + "name2_clean TEXT" + " )";
			db.execSQL(CREATE_SEARCH_VALUES_TABLE);
			
			new DbPopulator(this.context, db).populate();
		}
		
		else {
			// Remove DB tables
			String DROP_SEARCH_VALUES_TABLE = "DROP TABLE " + SearchValue.TABLE_SEARCHVALUES;
			db.execSQL(DROP_SEARCH_VALUES_TABLE);
			String DROP_SEARCH_USERS_TABLE = "DROP TABLE " + User.TABLE_USERS;
			db.execSQL(DROP_SEARCH_USERS_TABLE);

			// Create DB
			this.onCreate(db);
		}
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

//	public void insertSearchValue(SearchValue value) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(SearchValue.KEY_ID, value.getId());
//		values.put(SearchValue.KEY_NAME1, value.getName1());
//		values.put(SearchValue.KEY_NAME2, value.getName2());
//
//		db.insert(SearchValue.TABLE_SEARCHVALUES, null, values);
//
//		db.close();
//	}

	public String[] getSearchValues() {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(SearchValue.TABLE_SEARCHVALUES, new String[]{SearchValue.KEY_NAME1, SearchValue.KEY_NAME2}, null, null, null, null, null);
		
		if(cursor.getCount() > 0) {
			String[] str = new String[cursor.getCount() * 2];
			int i = 0;

			while (cursor.moveToNext()) {
				str[i] = cursor.getString(cursor.getColumnIndex(SearchValue.KEY_NAME1));
				i++;
				if(cursor.getString(cursor.getColumnIndex(SearchValue.KEY_NAME2)) != null) {
					str[i] = cursor.getString(cursor.getColumnIndex(SearchValue.KEY_NAME2));
					i++;
				}
			}

			String[] strWithoutNulls = new String[i];
			int j = 0;
			while (j < i) {
				strWithoutNulls[j] = str[j];
				j++;
			}

			return strWithoutNulls;
		}
		else {
			return new String[] {};
		}
	}

	public int getIdFromName(String searchValueName) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(SearchValue.TABLE_SEARCHVALUES, new String[] { SearchValue.KEY_ID }, SearchValue.KEY_NAME1 + " = ? OR " + SearchValue.KEY_NAME2 + " = ?", new String[] { searchValueName, searchValueName }, null, null, null, null);

		if(cursor != null)
			cursor.moveToFirst();

		db.close();
		return cursor.getInt(0);
	}

	public String[] read(String searchValueName) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		searchValueName = searchValueName.replace("ˆ", "a").replace("‡", "a").replace("Š", "a").replace("‰", "a");
		searchValueName = searchValueName.replace("", "e").replace("Ž", "e").replace("‘", "e").replace("", "e");
		searchValueName = searchValueName.replace("“", "i").replace("’", "i").replace("•", "i").replace("”", "i");
		searchValueName = searchValueName.replace("˜", "o").replace("—", "o").replace("š", "o").replace("™", "o");
		searchValueName = searchValueName.replace("", "u").replace("œ", "u").replace("Ÿ", "u").replace("ž", "u");
		searchValueName = searchValueName.replace("–", "n");

		Cursor cursor = db.query(SearchValue.TABLE_SEARCHVALUES, new String[] { SearchValue.KEY_NAME1}, SearchValue.KEY_NAME1_CLEAN + " LIKE ? ", new String[] { "%"+ searchValueName + "%" }, null, null, SearchValue.KEY_NAME1, null);
		Cursor cursor2 = db.query(SearchValue.TABLE_SEARCHVALUES, new String[] { SearchValue.KEY_NAME2 }, SearchValue.KEY_NAME2_CLEAN + " LIKE ?", new String[] { "%"+ searchValueName + "%" }, null, null, SearchValue.KEY_NAME2, null);
		
		String[] result = new String[cursor.getCount() + cursor2.getCount()];
		int x = 0;

		if(cursor.moveToFirst()) {
			do {
				result[x] = cursor.getString(0);
				x++;
			} while (cursor.moveToNext());
		}
		
       int y = x;
       if(cursor2.moveToFirst()) {
	       do {
	    	   result[y] = cursor2.getString(0);
	    	   y++;
	       } while (cursor2.moveToNext());
       }

		cursor.close();
		cursor2.close();
		db.close();
		
		Arrays.sort(result);
		
		return result;

	}
}

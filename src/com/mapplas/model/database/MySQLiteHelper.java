package com.mapplas.model.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
	private static final int DATABASE_VERSION = 4;

	// Database Name
	private static final String DATABASE_NAME = "MapplasDB";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create user table
		String CREATE_USER_TABLE = "CREATE TABLE " + User.TABLE_USERS + " ( " + User.KEY_ID + " INTEGER PRIMARY KEY, " + User.KEY_IMEI + " TEXT, " + User.KEY_PINNEDAPPS + " TEXT, " + User.KEY_BLOKEDAPPS + " TEXT" + " )";
		String CREATE_SEARCH_VALUES_TABLE = "CREATE TABLE " + SearchValue.TABLE_SEARCHVALUES + " ( " + SearchValue.KEY_ID + " INTEGER PRIMARY KEY, " + SearchValue.KEY_COUNTRY + " TEXT, " + SearchValue.KEY_NAME1 + " TEXT, " + SearchValue.KEY_NAME1_CLEAN + " TEXT, " + SearchValue.KEY_NAME2 + " TEXT, " + SearchValue.KEY_NAME2_CLEAN + " TEXT)";

		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_SEARCH_VALUES_TABLE);

		new DbPopulator(this.context, db).populate();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("UPGRADE", "onUpgrade");
		if(oldVersion == 1 && newVersion == 2) {
			Log.d("UPGRADE", "onUpgrade oldVersion == 1 && newVersion == 2");
			// Remove data from TABLE_SEARCHVALUES
			String REMOVE_SEARCH_VALUES_DATA = "DELETE FROM " + SearchValue.TABLE_SEARCHVALUES;
			db.execSQL(REMOVE_SEARCH_VALUES_DATA);

			new DbPopulator(this.context, db).populate();
		}

		else if(oldVersion == 2 && newVersion == 3) {
			Log.d("UPGRADE", "onUpgrade oldVersion == 2 && newVersion == 3");
			// Remove data from TABLE_SEARCHVALUES
			String REMOVE_SEARCH_VALUES_DATA = "DROP TABLE " + SearchValue.TABLE_SEARCHVALUES;
			db.execSQL(REMOVE_SEARCH_VALUES_DATA);

			String CREATE_SEARCH_VALUES_TABLE = "CREATE TABLE " + SearchValue.TABLE_SEARCHVALUES + " ( " + SearchValue.KEY_ID + " INTEGER PRIMARY KEY, " + SearchValue.KEY_COUNTRY + " TEXT, " + SearchValue.KEY_NAME1 + " TEXT, " + SearchValue.KEY_NAME1_CLEAN + " TEXT, " + SearchValue.KEY_NAME2 + " TEXT, " + SearchValue.KEY_NAME2_CLEAN + " TEXT)";
			db.execSQL(CREATE_SEARCH_VALUES_TABLE);

			new DbPopulator(this.context, db).populate();
		}

		else if(oldVersion == 3 && newVersion == 4) {
			Log.d("UPGRADE", "onUpgrade oldVersion == 2 && newVersion == 3");
			String REMOVE_SEARCH_VALUES_DATA = "DROP TABLE " + SearchValue.TABLE_SEARCHVALUES;
			db.execSQL(REMOVE_SEARCH_VALUES_DATA);

			String CREATE_SEARCH_VALUES_TABLE = "CREATE TABLE " + SearchValue.TABLE_SEARCHVALUES + " ( " + SearchValue.KEY_ID + " INTEGER PRIMARY KEY, " + SearchValue.KEY_COUNTRY + " TEXT, " + SearchValue.KEY_NAME1 + " TEXT, " + SearchValue.KEY_NAME1_CLEAN + " TEXT, " + SearchValue.KEY_NAME2 + " TEXT, " + SearchValue.KEY_NAME2_CLEAN + " TEXT)";
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

	// public void insertSearchValue(SearchValue value) {
	// SQLiteDatabase db = this.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	// values.put(SearchValue.KEY_ID, value.getId());
	// values.put(SearchValue.KEY_NAME1, value.getName1());
	// values.put(SearchValue.KEY_NAME2, value.getName2());
	//
	// db.insert(SearchValue.TABLE_SEARCHVALUES, null, values);
	//
	// db.close();
	// }

	public String[] getSearchValues() {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(SearchValue.TABLE_SEARCHVALUES, new String[] { SearchValue.KEY_NAME1, SearchValue.KEY_NAME2 }, null, null, null, null, null);

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


	@SuppressWarnings("rawtypes")
	public HashMap<Integer, ArrayList<List>> read(String searchValueName) {
		SQLiteDatabase db = this.getReadableDatabase();

		searchValueName = searchValueName.replace("à", "a").replace("á", "a").replace("ä", "a").replace("â", "a");
		searchValueName = searchValueName.replace("è", "e").replace("é", "e").replace("ë", "e").replace("ê", "e");
		searchValueName = searchValueName.replace("ì", "i").replace("í", "i").replace("ï", "i").replace("î", "i");
		searchValueName = searchValueName.replace("ò", "o").replace("ó", "o").replace("ö", "o").replace("ô", "o");
		searchValueName = searchValueName.replace("ù", "u").replace("ú", "u").replace("ü", "u").replace("û", "u");
		searchValueName = searchValueName.replace("ñ", "n");

		Cursor cursor = db.query(SearchValue.TABLE_SEARCHVALUES, new String[] { SearchValue.KEY_NAME1, SearchValue.KEY_ID, SearchValue.KEY_COUNTRY }, SearchValue.KEY_NAME1_CLEAN + " LIKE ? ", new String[] { "%" + searchValueName + "%" }, null, null, SearchValue.KEY_NAME1, null);
		Cursor cursor2 = db.query(SearchValue.TABLE_SEARCHVALUES, new String[] { SearchValue.KEY_NAME2, SearchValue.KEY_ID, SearchValue.KEY_COUNTRY }, SearchValue.KEY_NAME2_CLEAN + " LIKE ?", new String[] { "%" + searchValueName + "%" }, null, null, SearchValue.KEY_NAME2, null);
		
		HashMap<Integer, ArrayList<List>> dict = new HashMap<Integer, ArrayList<List>>();

		int i = 0;
		if(cursor.moveToFirst()) {
			do {
				ArrayList<String> name_country = new ArrayList<String>();
				ArrayList<Integer> id = new ArrayList<Integer>();
				ArrayList<List> name_id_list = new ArrayList<List>();
				
				name_country.add(cursor.getString(0));
				name_country.add(cursor.getString(2));
				id.add(Integer.parseInt(cursor.getString(1)));
				
				name_id_list.add(name_country);
				name_id_list.add(id);
				dict.put(i, name_id_list);
				i++;
			} while (cursor.moveToNext());
		}

		if(cursor2.moveToFirst()) {
			do {
				ArrayList<String> name_country = new ArrayList<String>();
				ArrayList<Integer> id = new ArrayList<Integer>();
				ArrayList<List> name_id_list = new ArrayList<List>();
				
				name_country.add(cursor2.getString(0));
				name_country.add(cursor2.getString(2));
				id.add(Integer.parseInt(cursor2.getString(1)));
				name_id_list.add(name_country);
				name_id_list.add(id);
				dict.put(i, name_id_list);
				i++;
			} while (cursor2.moveToNext());
		}

		cursor.close();
		cursor2.close();
		db.close();

		// Sort dictionary bi entity name
		dict = sortByComparator(dict, true);
		
		return dict;
	}

	private static HashMap<Integer, ArrayList<List>> sortByComparator(HashMap<Integer, ArrayList<List>> unsortMap, final boolean order) {

		List<Entry<Integer, ArrayList<List>>> list = new LinkedList<Entry<Integer, ArrayList<List>>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Integer, ArrayList<List>>>() {

			public int compare(Entry<Integer, ArrayList<List>> o1, Entry<Integer, ArrayList<List>> o2) {
				
				String name1 = (String)o1.getValue().get(0).get(0);
				String name2 = (String)o2.getValue().get(0).get(0);

				if(order) {
					return name1.compareTo(name2);
				}
				else {
					return name2.compareTo(name1);

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		HashMap<Integer, ArrayList<List>> sortedMap = new LinkedHashMap<Integer, ArrayList<List>>();
		int order_for_mixing_name1_and_name2_values = 0;
		for(Entry<Integer, ArrayList<List>> entry : list) {
			sortedMap.put(order_for_mixing_name1_and_name2_values, entry.getValue());
			order_for_mixing_name1_and_name2_values++;
		}

		return sortedMap;
	}
}

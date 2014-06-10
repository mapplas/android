package com.mapplas.model.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import app.mapplas.com.R;

import com.mapplas.model.SearchValue;

public class DbPopulator {

	private Context context;

	private SQLiteDatabase db;

	public DbPopulator(Context context, SQLiteDatabase db) {
		this.context = context;
		this.db = db;
	}

	public void populate() {
		InputStream rawFile = this.context.getResources().openRawResource(R.raw.cities);
		BufferedReader buffreader = null;
		try {
			buffreader = new BufferedReader(new InputStreamReader(rawFile, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String line;

		try {
			while ((line = buffreader.readLine()) != null) {
				String[] splittedLine = line.split("\\|");

				ContentValues values = new ContentValues();
				values.put(SearchValue.KEY_ID, splittedLine[0]);
				values.put(SearchValue.KEY_COUNTRY, splittedLine[1]);
				values.put(SearchValue.KEY_NAME1, splittedLine[2]);
				values.put(SearchValue.KEY_NAME1_CLEAN, splittedLine[3]);

				if(splittedLine.length == 6) {					
					values.put(SearchValue.KEY_NAME2, splittedLine[4]);
					values.put(SearchValue.KEY_NAME2_CLEAN, splittedLine[5]);
				}
				
				db.insert(SearchValue.TABLE_SEARCHVALUES, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

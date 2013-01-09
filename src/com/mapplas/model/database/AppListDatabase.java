package com.mapplas.model.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mapplas.model.AppOrderedList;


public class AppListDatabase extends OrmLiteSqliteOpenHelper {
	
	public static String DATABASE_NAME = "App.db";

	private static int DATABASE_VERSION = 1;

	private Dao<AppOrderedList, Integer> appListDao = null;

	public AppListDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, AppOrderedList.class);
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), "Can't create database " + database, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		// Nothing to do
	}

	public Dao<AppOrderedList, Integer> getAppListDao() throws SQLException {
		if(this.appListDao == null) {
			this.appListDao = this.getDao(AppOrderedList.class);
		}
		return this.appListDao;
	}

	@Override
	public void close() {
		super.close();
		this.appListDao = null;
	}

}

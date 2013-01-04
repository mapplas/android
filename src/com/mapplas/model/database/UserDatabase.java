package com.mapplas.model.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mapplas.model.User;

public class UserDatabase extends OrmLiteSqliteOpenHelper {

	public static String DATABASE_NAME = "Users.db";

	private static int DATABASE_VERSION = 1;

	private Dao<User, Integer> userDao = null;

	public UserDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, User.class);
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), "Can't create database " + database, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		// Nothing to do
	}

	public Dao<User, Integer> getUserDao() throws SQLException {
		if(this.userDao == null) {
			this.userDao = this.getDao(User.class);
		}
		return this.userDao;
	}

	@Override
	public void close() {
		super.close();
		this.userDao = null;
	}

}

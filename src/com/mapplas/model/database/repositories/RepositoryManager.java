package com.mapplas.model.database.repositories;

import java.sql.SQLException;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.mapplas.model.User;
import com.mapplas.model.database.DatabaseManager;
import com.mapplas.model.database.UserDatabase;

public class RepositoryManager {

	private static UserRepository users = null;


	static public UserRepository users(Context context) {
		if(RepositoryManager.users == null) {
			Dao<User, Integer> dao = null;
			UserDatabase userDatabase = DatabaseManager.user(context);
			try {
				dao = (Dao<User, Integer>)userDatabase.getUserDao();
			} catch (SQLException e) {
//				Log.e("RepositoryManager", e.getMessage(), e);
				throw new RuntimeException();
			}

			RepositoryManager.users = new UserRepository(dao, User.TABLE_NAME);
		}
		return RepositoryManager.users;
	}

	static public void close() {
		RepositoryManager.users = null;

		System.gc();

		DatabaseManager.close();
	}
}

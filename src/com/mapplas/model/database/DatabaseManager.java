package com.mapplas.model.database;

import android.content.Context;

public class DatabaseManager {

	private static UserDatabase userDatabase = null;
	
	static public UserDatabase user(Context context) {
		if(DatabaseManager.userDatabase == null) {
			DatabaseManager.userDatabase = new UserDatabase(context);
		}

		return DatabaseManager.userDatabase;
	}

	static public void close() {
		if(DatabaseManager.userDatabase != null && DatabaseManager.userDatabase.isOpen()) {
			DatabaseManager.userDatabase.close();
		}

		DatabaseManager.userDatabase = null;

		System.gc();
	}
}

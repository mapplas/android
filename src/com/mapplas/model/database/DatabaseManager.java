package com.mapplas.model.database;

import android.content.Context;

public class DatabaseManager {

	private static NotificationDatabase notificationDatabase = null;

	private static UserDatabase userDatabase = null;
	
	private static ModelDatabase modelDatabase = null;

	static public NotificationDatabase notification(Context context) {
		if(DatabaseManager.notificationDatabase == null) {
			DatabaseManager.notificationDatabase = new NotificationDatabase(context);
		}

		return DatabaseManager.notificationDatabase;
	}

	static public UserDatabase user(Context context) {
		if(DatabaseManager.userDatabase == null) {
			DatabaseManager.userDatabase = new UserDatabase(context);
		}

		return DatabaseManager.userDatabase;
	}
	
	static public ModelDatabase model(Context context) {
		if(DatabaseManager.modelDatabase == null) {
			DatabaseManager.modelDatabase = new ModelDatabase(context);
		}
		
		return DatabaseManager.modelDatabase;
	}

	static public void close() {
		if(DatabaseManager.notificationDatabase != null && DatabaseManager.notificationDatabase.isOpen()) {
			DatabaseManager.notificationDatabase.close();
		}

		if(DatabaseManager.userDatabase != null && DatabaseManager.userDatabase.isOpen()) {
			DatabaseManager.userDatabase.close();
		}
		
		if(DatabaseManager.modelDatabase != null && DatabaseManager.modelDatabase.isOpen()) {
			DatabaseManager.modelDatabase.close();
		}

		DatabaseManager.notificationDatabase = null;
		DatabaseManager.userDatabase = null;
		DatabaseManager.modelDatabase = null;

		System.gc();
	}
}

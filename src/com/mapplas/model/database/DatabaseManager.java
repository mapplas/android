package com.mapplas.model.database;

import android.content.Context;

public class DatabaseManager {

	private static NotificationDatabase notificationDatabase = null;

	private static UserDatabase userDatabase = null;
	
	private static AppListDatabase appDatabase = null;

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
	
	static public AppListDatabase app(Context context) {
		if(DatabaseManager.appDatabase == null) {
			DatabaseManager.appDatabase = new AppListDatabase(context);
		}
		
		return DatabaseManager.appDatabase;
	}

	static public void close() {
		if(DatabaseManager.notificationDatabase != null && DatabaseManager.notificationDatabase.isOpen()) {
			DatabaseManager.notificationDatabase.close();
		}

		if(DatabaseManager.userDatabase != null && DatabaseManager.userDatabase.isOpen()) {
			DatabaseManager.userDatabase.close();
		}
		
		if(DatabaseManager.appDatabase != null && DatabaseManager.appDatabase.isOpen()) {
			DatabaseManager.appDatabase.close();
		}

		DatabaseManager.notificationDatabase = null;
		DatabaseManager.userDatabase = null;
		DatabaseManager.appDatabase = null;

		System.gc();
	}
}

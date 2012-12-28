package com.mapplas.model.database;

import android.content.Context;

public class DatabaseManager {

	private static NotificationDatabase notificationDatabase = null;
	
	private static UserPrefDatabase userPreferencesDatabase = null;

	static public NotificationDatabase notification(Context context) {
		if(DatabaseManager.notificationDatabase == null) {
			DatabaseManager.notificationDatabase = new NotificationDatabase(context);
		}

		return DatabaseManager.notificationDatabase;
	}
	
	static public UserPrefDatabase userPreferences(Context context) {
		if(DatabaseManager.userPreferencesDatabase == null) {
			DatabaseManager.userPreferencesDatabase = new UserPrefDatabase(context);
		}
		
		return DatabaseManager.userPreferencesDatabase;
	}

	static public void close() {
		if(DatabaseManager.notificationDatabase != null && DatabaseManager.notificationDatabase.isOpen()) {
			DatabaseManager.notificationDatabase.close();
		}
		
		if(DatabaseManager.userPreferencesDatabase != null && DatabaseManager.userPreferencesDatabase.isOpen()) {
			DatabaseManager.userPreferencesDatabase.close();
		}

		DatabaseManager.notificationDatabase = null;
		DatabaseManager.userPreferencesDatabase = null;

		System.gc();
	}
}

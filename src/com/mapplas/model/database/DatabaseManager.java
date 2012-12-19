package com.mapplas.model.database;

import android.content.Context;

public class DatabaseManager {

	private static NotificationDatabase notificationDatabase = null;

	static public NotificationDatabase template(Context context) {
		if(DatabaseManager.notificationDatabase == null) {
			DatabaseManager.notificationDatabase = new NotificationDatabase(context);
		}

		return DatabaseManager.notificationDatabase;
	}

	static public void close() {
		if(DatabaseManager.notificationDatabase != null && DatabaseManager.notificationDatabase.isOpen()) {
			DatabaseManager.notificationDatabase.close();
		}

		DatabaseManager.notificationDatabase = null;

		System.gc();
	}
}

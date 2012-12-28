package com.mapplas.model.database.repositories;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.mapplas.model.database.DatabaseManager;
import com.mapplas.model.database.NotificationDatabase;
import com.mapplas.model.notifications.Notification;

public class RepositoryManager {

	private static NotificationRepository notifications = null;

	static public NotificationRepository notifications(Context context) {
		if(RepositoryManager.notifications == null) {
			Dao<Notification, Integer> dao = null;
			NotificationDatabase notificationDatabase = DatabaseManager.notification(context);
			try {
				dao = (Dao<Notification, Integer>)notificationDatabase.getNotificationDao();
			} catch (SQLException e) {
				Log.e("RepositoryManager", e.getMessage(), e);
				throw new RuntimeException();
			}

			RepositoryManager.notifications = new NotificationRepository(dao, Notification.TABLE_NAME);
		}
		return RepositoryManager.notifications;
	}

	static public void close() {
		RepositoryManager.notifications = null;

		System.gc();

		DatabaseManager.close();
	}
}

package com.mapplas.model.database.repositories;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.mapplas.model.User;
import com.mapplas.model.database.DatabaseManager;
import com.mapplas.model.database.NotificationDatabase;
import com.mapplas.model.database.UserDatabase;
import com.mapplas.model.notifications.Notification;

public class RepositoryManager {

	private static NotificationRepository notifications = null;

	private static UserRepository users = null;
	
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

	static public UserRepository users(Context context) {
		if(RepositoryManager.users == null) {
			Dao<User, Integer> dao = null;
			UserDatabase userDatabase = DatabaseManager.user(context);
			try {
				dao = (Dao<User, Integer>)userDatabase.getUserDao();
			} catch (SQLException e) {
				Log.e("RepositoryManager", e.getMessage(), e);
				throw new RuntimeException();
			}

			RepositoryManager.users = new UserRepository(dao, User.TABLE_NAME);
		}
		return RepositoryManager.users;
	}

	static public void close() {
		RepositoryManager.notifications = null;
		RepositoryManager.users = null;

		System.gc();

		DatabaseManager.close();
	}
}

package com.mapplas.model.database.repositories;

import java.sql.SQLException;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.mapplas.model.Notification;
import com.mapplas.model.database.repositories.base.Repository;

public class NotificationRepository extends Repository {

	public NotificationRepository(Dao<Notification, Integer> dao, String tableName) {
		super(dao, tableName);
	}

	/**
	 * Create DB queries
	 */
	public void insertNotifications(Notification notification) {
		try {
			this.createOrUpdateBatch(notification);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public void setNotificationsAsShown() {
		try {
			UpdateBuilder<Notification, Integer> updateQuery = this.getDao().updateBuilder();
			updateQuery.updateColumnValue("shown", 1);
			this.getDao().update(updateQuery.prepare());
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public void setNotificationAsSeen(int notificationId) {
		try {
			UpdateBuilder<Notification, Integer> updateQuery = this.getDao().updateBuilder();
			updateQuery.updateColumnValue("seen", 1).where().eq("id", notificationId);
			this.getDao().update(updateQuery.prepare());
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

}

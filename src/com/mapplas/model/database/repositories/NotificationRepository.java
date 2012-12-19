package com.mapplas.model.database.repositories;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
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
	
}

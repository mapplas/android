package com.mapplas.model.database.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.base.Repository;
import com.mapplas.model.notifications.Notification;
import com.mapplas.model.notifications.NotificationList;

public class NotificationRepository extends Repository {

	public static int MAX_NOTIFICATIONS_IN_TABLE = 100;

	private static int DUPLICATE_APPS_OLDER_THAN_MSECONDS = 86400000;

	private LinkedHashMap<Integer, Integer> notificationIds = null;

	public NotificationRepository(Dao<Notification, Integer> dao, String tableName) {
		super(dao, tableName);
		this.notificationIds = new LinkedHashMap<Integer, Integer>();
		this.loadNotificationIds();
	}

	@SuppressWarnings("unchecked")
	private void loadNotificationIds() {
		try {
			QueryBuilder<Notification, Integer> queryBuilder = this.getDao().queryBuilder();
			queryBuilder.selectColumns("id");
			List<Notification> notificationList = this.getDao().query(queryBuilder.prepare());

			for(Notification currentNot : notificationList) {
				this.incrementNotificationIntoArrayOfId(currentNot);
			}

		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

	/**
	 * Create DB queries
	 */
	public void insertNotifications(Notification notification) {
		try {
			this.createOrUpdateBatch(notification);
			this.incrementNotificationIntoArrayOfId(notification);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
		}
	}

	public void insertNotificationsByChecking(Notification notification) {
		// If notification does not exists OR is from different day

		if(!this.checkIfNotificationExists(notification)) {
			try {
				this.createOrUpdateBatch(notification);
				this.incrementNotificationIntoArrayOfId(notification);
			} catch (Exception e) {
				Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
			}
		}
		else {
			Notification dbNotification = (Notification)this.findNotificationFromNotificationId(notification.getId());
			try {
				this.createOrUpdateBatch(notification);
				this.incrementNotificationIntoArrayOfId(notification);
				
				if(!this.checkIfExistingNotificationIsEnoughOld(notification, dbNotification)) {
					this.delete(dbNotification);
					this.decrementNotificationIntoArrayOfId(dbNotification);
				}
			} catch (Exception e) {
				Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
			}

		}
	}

	private boolean checkIfExistingNotificationIsEnoughOld(Notification notification, Notification dbNotification) {
		if(dbNotification != null) {
			if(this.moreThanOneDayAgo(notification, dbNotification)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	private boolean moreThanOneDayAgo(Notification notification, Notification dbNotification) {
		return notification.arrivalTimestamp() > dbNotification.arrivalTimestamp() + NotificationRepository.DUPLICATE_APPS_OLDER_THAN_MSECONDS;
	}

	@SuppressWarnings("unchecked")
	private Notification findNotificationFromNotificationId(int id) {
		Notification notification = null;
		try {
			QueryBuilder<Notification, Integer> queryBuilder = this.getDao().queryBuilder();
			queryBuilder.selectColumns("id", "idCompany", "idApp", "name", "description", "date", "hour", "seen", "shown", "arrivalTimestamp", "currentLocation", "dateInMs").where().eq("id", String.valueOf(id));
			List<Notification> notificationList = this.getDao().query(queryBuilder.prepare());

			if(notificationList.size() > 0) {
				notification = notificationList.get(0);
			}
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
		return notification;
	}

	private boolean checkIfNotificationExists(Notification notification) {
		return this.notificationIds.containsKey(notification.getId());
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

	public int getNumberOfRows() {
		try {
			return (int)this.getDao().countOf();
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteRowsUpTo(int numberOfRowsToSave, SuperModel model, App app) {
		try {
			// Get first 100 notifications ordered by date
			QueryBuilder<Notification, Integer> queryBuilder = this.getDao().queryBuilder();
			queryBuilder.selectColumns("id", "idCompany", "idApp", "name", "description", "date", "hour", "seen", "shown", "arrivalTimestamp", "currentLocation", "dateInMs").orderBy("dateInMs", false).limit((long)NotificationRepository.MAX_NOTIFICATIONS_IN_TABLE);
			List<Notification> notificationList = this.getDao().query(queryBuilder.prepare());

			// Delete table
			this.empty();
			this.notificationIds.clear();

			NotificationList appNotifications = new NotificationList();
			// Insert elements in query to database table and model
			for(Notification current : notificationList) {
				current.setAuxApp(app);
				this.insertNotifications(current);

				appNotifications.add(current);
			}

			model.setNotificationList(appNotifications);

			// Load changes into db
			this.createOrUpdateFlush();

		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public LinkedHashMap<Long, ArrayList<Notification>> getNotificationsSeparatedByLocation() {
		// Get different timestamp sorted from newer to older
		ArrayList<Notification> listOfNotificationsTimestamps = getNotificationTimestampList();

		// Get ArrayLists of notifications for each diferent timestamp
		LinkedHashMap<Long, ArrayList<Notification>> notificationData = new LinkedHashMap<Long, ArrayList<Notification>>();
		ArrayList<Notification> listOfNotifications = new ArrayList<Notification>();
		QueryBuilder<Notification, Integer> queryBuilder = this.getDao().queryBuilder();
		long currentNotificationTimestamp = 0;

		for(Notification currentNotification : listOfNotificationsTimestamps) {
			currentNotificationTimestamp = currentNotification.arrivalTimestamp();
			try {
				queryBuilder.selectColumns("id", "idCompany", "idApp", "name", "description", "date", "hour", "seen", "shown", "arrivalTimestamp", "currentLocation", "dateInMs").where().eq("arrivalTimestamp", currentNotificationTimestamp);
				queryBuilder.orderBy("dateInMs", false);
				listOfNotifications = (ArrayList<Notification>)this.getDao().query(queryBuilder.prepare());

				notificationData.put(currentNotificationTimestamp, listOfNotifications);
			} catch (SQLException e) {
				Log.e(this.getClass().getName(), e.getMessage(), e);
			}
		}

		return notificationData;
	}

	public ArrayList<Notification> getTimestamps() {
		return this.getNotificationTimestampList();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Notification> getNotificationTimestampList() {
		ArrayList<Notification> listOfNotificationsTimestamps = null;
		try {
			QueryBuilder<Notification, Integer> queryBuilder = this.getDao().queryBuilder();
			queryBuilder.distinct().selectColumns("arrivalTimestamp").orderBy("arrivalTimestamp", false);
			listOfNotificationsTimestamps = (ArrayList<Notification>)this.getDao().query(queryBuilder.prepare());
		} catch (SQLException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
		return listOfNotificationsTimestamps;
	}

	/**
	 * 
	 * Actions with array list of notification id's
	 * 
	 */
	private void decrementNotificationIntoArrayOfId(Notification notification) {
		int count = this.notificationIds.get(notification.getId());
		if(count > 0) {
			this.notificationIds.put(notification.getId(), count - 1);
		}
		else {
			this.notificationIds.remove(notification.getId());
		}
	}

	private void incrementNotificationIntoArrayOfId(Notification notification) {
		if(!notificationIds.containsKey(notification.getId())) {
			this.notificationIds.put(notification.getId(), 1);
		}
		else {
			int count = this.notificationIds.get(notification.getId());
			this.notificationIds.put(notification.getId(), count + 1);
		}
	}

}

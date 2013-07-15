package com.mapplas.app.async_tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import app.mapplas.com.R;

import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.NotificationRepository;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.notifications.Notification;
import com.mapplas.utils.network.mappers.JsonToNotificationMapper;

public class NotificationDatabaseInserterTask extends AsyncTask<Void, Void, Void> {

	private SuperModel model;

	private Context context;

	private NotificationRepository notificationsRepository;

	private Button notificationButton;
	
	private int count = 0;

	public NotificationDatabaseInserterTask(SuperModel model, Context context, Button notificationButton) {
		this.model = model;
		this.context = context;
		this.notificationsRepository = RepositoryManager.notifications(this.context);
		this.notificationButton = notificationButton;
	}

	@Override
	protected Void doInBackground(Void... params) {
		JsonToNotificationMapper notificationMapper = new JsonToNotificationMapper();
		Notification notification = new Notification();

		long currentTimestamp = System.currentTimeMillis();

		try {
			ArrayList<String> notificationRawList = this.model.notificationRawList();

			for(String currentNotificationJsonArrayString : notificationRawList) {
				JSONArray currentAppNotif = new JSONArray(currentNotificationJsonArrayString);

				for(int j = 0; j < currentAppNotif.length(); j++) {
					this.count ++;
					
					JSONObject currentNotification = currentAppNotif.getJSONObject(j);
					notification = notificationMapper.map(currentNotification);

					App notificationApp = this.getAppForId(notification.getAppId());

					notification.setAuxApp(notificationApp);
					notification.setArrivalTimestamp(currentTimestamp);
					notification.setCurrentLocation(this.model.currentDescriptiveGeoLoc());
					notification.setDateInMiliseconds(getMsFromDateAndHour(notification.getDate(), notification.getHour()));

					this.model.notificationList().add(notification);

					// First, remove from db duplicated apps in same day
					this.notificationsRepository.checkSameApps(notification);

					// Then, insert new notification to db
					this.notificationsRepository.insertNotifications(notification);
					Log.d(this.getClass().getSimpleName(), "NOTIFICATION INSERTED. ID: " + notification.getId() + " NAME: " + notification.getName());					
				}
			}
			this.notificationsRepository.createOrUpdateFlush();

			this.deleteNotificationsUpTo(this.model);

		} catch (Exception e) {
			this.model.setOperationError(true);
			this.model.setErrorText(e.getMessage());
			e.printStackTrace();
		}

		try {
			this.notificationsRepository.createOrUpdateFlush();
			Log.d(this.getClass().getSimpleName(), "NOTIFICATIONS FLUSHED");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		this.notificationButton.setEnabled(true);
		if(this.model.notificationList().size() > 0) {
			this.notificationButton.setText(String.valueOf(this.count));
			this.notificationButton.setBackgroundResource(R.drawable.menu_notifications_number_button);
		}
		else {
			this.notificationButton.setText("");
			this.notificationButton.setBackgroundResource(R.drawable.menu_notifications_button);
		}
		
		super.onPostExecute(result);
	}

	private App getAppForId(int appId) {
		ArrayList<App> appList = this.model.appList().getAppList();
		for(App app : appList) {
			if(app.getId() == appId) {
				return app;
			}
		}
		return null;
	}

	private void deleteNotificationsUpTo(SuperModel model) {
		int numberOfNotificationsInTable = this.notificationsRepository.getNumberOfRows();

		if(numberOfNotificationsInTable > NotificationRepository.MAX_NOTIFICATIONS_IN_TABLE) {
			// Delete rows up to defined number
			this.notificationsRepository.deleteRowsUpTo(NotificationRepository.MAX_NOTIFICATIONS_IN_TABLE, model);
		}
	}

	private long getMsFromDateAndHour(String date, String hour) {
		long totalMs = 0;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		try {
			d1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		totalMs += d1.getTime();

		if(!hour.equals("00:00")) {
			SimpleDateFormat hourFormat = new SimpleDateFormat("kk:mm");
			Date d2 = null;
			try {
				d2 = hourFormat.parse(hour);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			totalMs += d2.getTime();
		}

		return totalMs;
	}

}

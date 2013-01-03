package com.mapplas.model.database.inserters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.NotificationRepository;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.notifications.Notification;
import com.mapplas.utils.mappers.JsonToNotificationMapper;

public class NotificationInserter {

	private Context context;

	private NotificationRepository notificationsRepository;

	public NotificationInserter(Context context) {
		this.context = context;
		this.notificationsRepository = RepositoryManager.notifications(this.context);
	}

	public void insert(JSONArray jArray, int i, App app, SuperModel model, long currentTimestamp) {
		// Parse notifications
		JsonToNotificationMapper notificationMapper = new JsonToNotificationMapper();
		Notification notification = new Notification();

		boolean inserted = false;

		try {
			JSONArray auxArray = jArray.getJSONObject(i).getJSONArray("AuxNews");
			for(int j = 0; j < auxArray.length(); j++) {

				JSONObject currentNotification = auxArray.getJSONObject(j);
				notification = notificationMapper.map(currentNotification);
				notification.setAuxApp(app);
				notification.setArrivalTimestamp(currentTimestamp);
				notification.setCurrentLocation(model.currentDescriptiveGeoLoc());
				notification.setDateInMiliseconds(getMsFromDateAndHour(notification.getDate(), notification.getHour()));
				
				model.notificationList().add(notification);

				this.notificationsRepository.insertNotifications(notification);
				inserted = true;
				Log.d(this.getClass().getSimpleName(), "NOTIFICATION INSERTED. ID: " + notification.getId() + " NAME: " + notification.getName());
			}

			if(inserted) {
				this.notificationsRepository.createOrUpdateFlush();
				Log.d(this.getClass().getSimpleName(), "NOTIFICATIONS FLUSHED");
			}

			this.deleteNotificationsUpTo(app, model);

		} catch (Exception e) {
			model.setOperationError(true);
			model.setErrorText(e.getMessage());
			e.printStackTrace();
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

	private void deleteNotificationsUpTo(App app, SuperModel model) {
		int numberOfNotificationsInTable = this.notificationsRepository.getNumberOfRows();

		if(numberOfNotificationsInTable > NotificationRepository.MAX_NOTIFICATIONS_IN_TABLE) {
			// Delete rows up to defined number
			this.notificationsRepository.deleteRowsUpTo(NotificationRepository.MAX_NOTIFICATIONS_IN_TABLE, model, app);
		}
	}
}

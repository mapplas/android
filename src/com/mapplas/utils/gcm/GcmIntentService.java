package com.mapplas.utils.gcm;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import app.mapplas.com.R;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mapplas.app.activities.AppDetail;
import com.mapplas.model.App;
import com.mapplas.model.Constants;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	
	private NotificationManager mNotificationManager;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if(!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//				sendNotification("Send error: " + extras.toString());
			}
			else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//				sendNotification("Deleted messages on server: " + extras.toString());
			}
			else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				showNotification(extras);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	@SuppressLint("NewApi")
	private void showNotification(Bundle bundle) {
		
		String title = "";
		String message = "";
		if(bundle.containsKey("t") && bundle.containsKey("m")) {
			title = bundle.getString("t");
			message = bundle.getString("m");
		}
		
		App app = new App();
		if(bundle.containsKey("aid") && bundle.containsKey("at") && bundle.containsKey("ad") && bundle.containsKey("al") && bundle.containsKey("as")) {
			app.setId(bundle.getString("aid"));
			app.setName(bundle.getString("at"));
			app.setAppDescription(bundle.getString("ad"));
			app.setAppLogo(bundle.getString("al"));
			app.setAppPrice(bundle.getString("ap"));
			app.setAuxPin(Integer.parseInt(bundle.getString("aip")));
			
			this.parseScreenshots(bundle.getString("as"), app);
		}
		
		mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_action_rate)
			.setContentTitle(title)
			.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
			.setContentText(message)
			.setAutoCancel(true)
			.setTicker("Text in notification bar")
			.setSound(null)
			.setWhen(System.currentTimeMillis());

		
		Intent resultIntent = new Intent(this, AppDetail.class);
		
		resultIntent.putExtra(Constants.MAPPLAS_DETAIL_APP, app);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(AppDetail.class);
		stackBuilder.addNextIntent(resultIntent);
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
			
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	private void parseScreenshots(String screenshots, App app) {
		String[] screenshotList = screenshots.split(",");
		ArrayList<String> photos = new ArrayList<String>();

		for(int i = 0; i < screenshotList.length; i++) {
			photos.add(screenshotList[i]);
		}

		app.setAuxPhotos(photos);
	}

}

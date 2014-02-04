package com.mapplas.utils.gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import app.mapplas.com.R;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.Constants;
import com.mapplas.utils.language.LanguageSetter;

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
				// sendNotification("Send error: " + extras.toString());
			}
			else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				// sendNotification("Deleted messages on server: " +
				// extras.toString());
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
		String title_es = "";
		String title_en = "";
		String title_eu = "";
		
		String message = "";
		String message_es = "";
		String message_en = "";
		String message_eu = "";
		
		String textForNotificationBar = "";
		String textForNotificationBar_es = "";
		String textForNotificationBar_en = "";
		String textForNotificationBar_eu = "";
		
		if(bundle.containsKey("t_es") && bundle.containsKey("m_es") && bundle.containsKey("nbt_es") && bundle.containsKey("t_en") && bundle.containsKey("m_en") && bundle.containsKey("nbt_en") && bundle.containsKey("t_eu") && bundle.containsKey("m_eu") && bundle.containsKey("nbt_eu")) {
			title_es = bundle.getString("t_es");
			title_en = bundle.getString("t_en");
			title_eu = bundle.getString("t_eu");
			
			message_es = bundle.getString("m_es");
			message_en = bundle.getString("m_en");
			message_eu = bundle.getString("m_eu");
			
			textForNotificationBar_es = bundle.getString("nbt_es");
			textForNotificationBar_en = bundle.getString("nbt_en");
			textForNotificationBar_eu = bundle.getString("nbt_eu");
		}
		
		if(new LanguageSetter(this).getLanguageConstantFromPhone().equals(Constants.SPANISH)) {
			title = title_es;
			message = message_es;
			textForNotificationBar = textForNotificationBar_es;
		}
		else if(new LanguageSetter(this).getLanguageConstantFromPhone().equals(Constants.BASQUE)) {
			title = title_eu;
			message = message_eu;
			textForNotificationBar = textForNotificationBar_eu;
		}
		else {
			title = title_en;
			message = message_en;
			textForNotificationBar = textForNotificationBar_en;
		}
		
		mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_notification)
			.setContentTitle(title)
			.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
			.setContentText(message)
			.setAutoCancel(true)
			.setTicker(textForNotificationBar)
			.setSound(null)
			.setWhen(System.currentTimeMillis());

		Intent notificationIntent = new Intent(this, MapplasActivity.class);
	    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		mBuilder.setContentIntent(intent);
		
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}

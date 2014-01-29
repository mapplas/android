package com.mapplas.utils.geofences;

import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.mapplas.utils.network.async_tasks.GeofenceNotificationRequesterTask;

public class ReceiveTransitionsIntentService extends IntentService {

	/**
	 * Sets an identifier for the service
	 */
	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
	}

	/**
	 * Handles incoming intents
	 * 
	 * @param intent
	 *            The Intent sent by Location Services. This Intent is provided
	 *            to Location Services (inside a PendingIntent) when you call
	 *            addGeofences()
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		
		int user_id = -1;
		
		if(intent.hasExtra("username")) {
			user_id = intent.getIntExtra("username", -1);
		}

		// Vibrate for 500 milliseconds
		Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);

		if(LocationClient.hasError(intent)) {
			int errorCode = LocationClient.getErrorCode(intent);
			Log.e("ReceiveTransitionsIntentService", "Location Services error: " + Integer.toString(errorCode));
			Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
		}

		else {

			Toast.makeText(this, "INSIDE", Toast.LENGTH_LONG).show();

			int transitionType = LocationClient.getGeofenceTransition(intent);
			if((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) || (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {

				List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
				String[] triggerIds = new String[1000];

				for(int i = 0; i < triggerList.size(); i++) {
					// Store the Id of each geofence
					triggerIds[i] = triggerList.get(i).getRequestId();
				}
								
				new GeofenceNotificationRequesterTask(user_id, Integer.parseInt(triggerIds[0])).execute();
			}
			else {
				Log.e("ReceiveTransitionsIntentService", "Geofence transition error: " + Integer.toString(transitionType));
			}
		}
	}
}

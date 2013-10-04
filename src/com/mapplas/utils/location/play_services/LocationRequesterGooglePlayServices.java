package com.mapplas.utils.location.play_services;

import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.Constants;
import com.mapplas.utils.location.UserLocationListener;

public class LocationRequesterGooglePlayServices implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, Handler.Callback, LocationListener {

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private final int TIMEOUT_MESSAGE_ID = 0;

	private LocationClient locationClient;

	private Context context;

	private MapplasActivity mainActivity;
	
	private UserLocationListener userLocationListener;

	private Location location;

	private Handler timerHandler;

	public LocationRequesterGooglePlayServices(Context context, MapplasActivity mainActivity, UserLocationListener userLocatioinListener) {
		this.context = context;
		this.mainActivity = mainActivity;
		this.userLocationListener = userLocatioinListener;

		this.locationClient = new LocationClient(this.context, this, this);
		this.timerHandler = new Handler(this);
	}

	public void start() {
		this.locationClient.connect();

		this.timerHandler.sendEmptyMessageDelayed(TIMEOUT_MESSAGE_ID, Constants.LOCATION_TIMEOUT_IN_MILLISECONDS);
	}

	public void stop() {
		this.locationClient.disconnect();
		this.timerHandler.removeMessages(TIMEOUT_MESSAGE_ID);
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle arg0) {		
		LocationRequest locRequest = LocationRequest.create();
		locRequest.setExpirationDuration(Constants.LOCATION_TIMEOUT_IN_MILLISECONDS);
		locRequest.setInterval(2000);
		locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		this.locationClient.requestLocationUpdates(locRequest, this);
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		this.locationClient.connect();
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if(connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this.mainActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		}
		else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			// showErrorDialog(connectionResult.getErrorCode());
		}
	}

	/*
	 * Timer handler callback
	 * 
	 */
	@Override
	public boolean handleMessage(Message msg) {
		this.locationClient.disconnect();
		this.userLocationListener.locationSearchDidTimeout(this.location);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
//		Log.e("fdsa",""+location);
		this.locationClient.removeLocationUpdates(this);
		this.location = location;
		this.userLocationListener.locationSearchEnded(this.location);
	}

}

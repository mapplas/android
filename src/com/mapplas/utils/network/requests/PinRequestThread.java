package com.mapplas.utils.network.requests;

import android.location.Location;
import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.utils.network.connectors.PinRequestConnector;

public class PinRequestThread {

	private String action;

	private App app;

	private String uid;
	
	private Location currentLocation;
	
	private String reverseGeocodedLocation;

	public PinRequestThread(String action, App anonLoc, String uid, Location location, String reverseGeocodedLocation) {
		this.action = action;
		this.app = anonLoc;
		this.uid = uid;
		this.currentLocation = location;
		this.reverseGeocodedLocation = reverseGeocodedLocation;
	}

	public Runnable getThread() {
				
		return new Runnable() {

			@Override
			public void run() {
				
				try {
					if(currentLocation == null) {
						PinRequestConnector.request(action, app.getId(), uid, 0, 0, "");
					} else {
						PinRequestConnector.request(action, app.getId(), uid, currentLocation.getLongitude(), currentLocation.getLatitude(), reverseGeocodedLocation);
					}
					
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Action Pin: " + e);
				}
			}
		};
	}
}

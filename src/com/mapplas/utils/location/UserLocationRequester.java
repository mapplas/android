package com.mapplas.utils.location;

import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UserLocationRequester implements LocationListener, Handler.Callback {

	private final int TIMEOUT_MESSAGE_ID = 0;
	
	private final long LOCATION_AGE_IN_SECONDS = 60;
	
	protected LocationManager manager;

	protected UserLocationListener listener;
	
	private int locationTimeout;

	protected Location currentBestLocation = null;

	private Handler timerHandler;
	
	private boolean locationSearched = false;

	public UserLocationRequester(LocationManager manager, UserLocationListener listener, int locationTimeout) {
		this.manager = manager;
		this.listener = listener;
		this.locationTimeout = locationTimeout;
		this.timerHandler = new Handler(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if(!locationSearched) {
			this.locationSearched = true;
			this.currentBestLocation = location;
			this.finishLocationSearch();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		this.restart();
	}

	@Override
	public void onProviderEnabled(String provider) {
		this.restart();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/*
	 * Timer handler callback
	 * 
	 */
	@Override
	public boolean handleMessage(Message msg) {
		this.stopListeningForLocationUpdates();
		this.listener.locationSearchDidTimeout(this.currentBestLocation);
		return true;
	}

	/*
	 * Start/Stop listening methods
	 */
	public void startListeningForLocationUpdates() {
		this.startAllActiveProviders();
		this.initializeBestLocationFromAvailableProvidersCache();

		timerHandler.sendEmptyMessageDelayed(TIMEOUT_MESSAGE_ID, this.locationTimeout);
	}

	public void stopListeningForLocationUpdates() {
		manager.removeUpdates(this);
		timerHandler.removeMessages(TIMEOUT_MESSAGE_ID);
	}
	
	public void resetLocationBoolean() {
		this.locationSearched = false;
	}

	/*
	 * Private helper methods
	 */
	protected void startAllActiveProviders() {
		List<String> providers = manager.getProviders(true);
		for(String activeProvider : providers) {
			manager.requestLocationUpdates(activeProvider, 0, 0, this);
		}
	}

	protected void initializeBestLocationFromAvailableProvidersCache() {
		List<String> providers = manager.getProviders(true);
		for(String activeProvider : providers) {
			Location providerCachedLocation = manager.getLastKnownLocation(activeProvider);
			if(this.newLocationHasBetterAccuracyThanCurrentLocation(providerCachedLocation) && this.locationIsNotTooOld(providerCachedLocation)) {
				this.currentBestLocation = providerCachedLocation;
			}
		}
	}
	
	/*
	 * helper methods used to determine whether store or discard a received location
	 */
	protected boolean newLocationHasBetterAccuracyThanCurrentLocation(Location location) {
		if(this.currentBestLocation == null || location == null) {
			return true;
		}
		else {
			return (Float.compare(location.getAccuracy(), this.currentBestLocation.getAccuracy()) < 0);
		}
	}
	
	protected boolean locationIsNotTooOld(Location location) {
		if(location == null){
			return false;
		}
		else{
			long locationAge = System.currentTimeMillis() - location.getTime();
			return (locationAge < this.LOCATION_AGE_IN_SECONDS * 1000);			
		}
	}

	private void finishLocationSearch() {
		this.stopListeningForLocationUpdates();
		this.listener.locationSearchEnded(this.currentBestLocation);
	}

	private void restart() {
		this.stopListeningForLocationUpdates();
		this.startListeningForLocationUpdates();
	}
}

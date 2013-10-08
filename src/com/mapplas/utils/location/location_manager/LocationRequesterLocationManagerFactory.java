package com.mapplas.utils.location.location_manager;

import android.location.LocationManager;

import com.mapplas.model.Constants;
import com.mapplas.utils.location.UserLocationListener;

public class LocationRequesterLocationManagerFactory {

	public LocationRequesterLocationManager create(LocationManager manager, UserLocationListener listener) {
		return new LocationRequesterLocationManager(manager, listener, Constants.LOCATION_TIMEOUT_IN_MILLISECONDS);
	}
}

package com.mapplas.utils.location;

import com.mapplas.model.Constants;

import android.location.LocationManager;

public class UserLocationRequesterFactory {

	public UserLocationRequester create(LocationManager manager, UserLocationListener listener) {
		return new UserLocationRequester(manager, listener, Constants.LOCATION_TIMEOUT_IN_MILLISECONDS);
	}
}

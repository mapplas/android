package com.mapplas.utils.location;

import android.location.LocationManager;

import com.mapplas.model.Constants;

public class UserLocationRequesterFactory {

	public UserLocationRequester create(LocationManager manager, UserLocationListener listener) {
		return new UserLocationRequester(manager, listener, Constants.LOCATION_TIMEOUT_IN_MILLISECONDS);
	}
}

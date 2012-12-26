package com.mapplas.utils.location;

import android.location.LocationManager;


public class UserLocationRequesterFactory {

	public UserLocationRequester create(LocationManager manager, UserLocationListener listener, int locationTimeout) {
		return new UserLocationRequester(manager, listener, locationTimeout);
	}
}

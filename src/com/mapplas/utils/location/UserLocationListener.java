package com.mapplas.utils.location;

import android.location.Location;

public interface UserLocationListener {

	public void locationSearchEnded(Location location);

	public void locationSearchDidTimeout(Location location);

}

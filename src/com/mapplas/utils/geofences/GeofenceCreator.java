package com.mapplas.utils.geofences;

import android.content.Context;

import com.google.android.gms.location.Geofence;
import com.mapplas.model.GeoFence;
import com.mapplas.model.database.repositories.GeoFenceRepository;
import com.mapplas.model.database.repositories.RepositoryManager;

public class GeofenceCreator {

	/*
	 * Use to set an expiration time for a geofence. After this amount of time
	 * Location Services will stop tracking the geofence.
	 */
	private static final long SECONDS_PER_HOUR = 60;

	private static final long MILLISECONDS_PER_SECOND = 1000;

	private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

	private static final long GEOFENCE_EXPIRATION_TIME = GEOFENCE_EXPIRATION_IN_HOURS * SECONDS_PER_HOUR * MILLISECONDS_PER_SECOND;

	public void createOne(Context context) {
		String geofenceIdentifier = "";
		GeoFenceRepository repo = RepositoryManager.geofences(context);
		repo.get(geofenceIdentifier);

		try {
			repo.createOrUpdateBatch(new GeoFence("fsafasdfsd", 42.3453453, -1.4353453, 45343, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

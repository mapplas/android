package com.mapplas.utils.network.async_tasks;

import android.location.Location;
import android.os.AsyncTask;

import com.mapplas.utils.network.connectors.UserPlayStoreInteractionConnector;

public class UserPlayStoreInteractionTask extends AsyncTask<Void, Void, Void> {

	private String appId;

	private int userId;

	private Location location;

	public UserPlayStoreInteractionTask(String app_id, int user_id, Location location) {
		this.appId = app_id;
		this.userId = user_id;
		this.location = location;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			UserPlayStoreInteractionConnector.request(this.appId, this.userId, this.location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
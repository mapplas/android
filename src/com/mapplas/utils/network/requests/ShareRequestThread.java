package com.mapplas.utils.network.requests;

import android.location.Location;

import com.mapplas.utils.network.connectors.AppShareConnector;

public class ShareRequestThread {

	private String appId;

	private int userId;

	private Location location;

	public ShareRequestThread(String app_id, int user_id, Location location) {
		this.appId = app_id;
		this.userId = user_id;
		this.location = location;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					AppShareConnector.request(appId, userId, location);
				} catch (Exception e) {
//					Log.i(getClass().getSimpleName(), "Thread Action Like: " + e);
				}
			}
		};
	}
}

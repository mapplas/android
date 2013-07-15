package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.model.User;
import com.mapplas.utils.network.requests.NetRequests;

public class ActivityRequestThread {

	private String currentLocation;

	private App app = null;

	private User user;
	
	private String action;

	public ActivityRequestThread(String currentLocation, App anonLoc, User user, String action) {
		this.currentLocation = currentLocation;
		this.app = anonLoc;
		this.user = user;
		this.action = action;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					
					String appId = "0";
					if(app != null) {
						appId = String.valueOf(app.getId());
					}
					
					NetRequests.ActivityRequest(currentLocation, action, appId, String.valueOf(user.getId()));
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Action " + action + " " + e);
				}
			}
		};
	}
}

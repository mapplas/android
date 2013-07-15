package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.utils.network.requests.NetRequests;

public class PinRequestThread {

	private String action;

	private App app;

	private String uid;
	
	private String currentLocation;

	public PinRequestThread(String action, App anonLoc, String uid, String currentLocation) {
		this.action = action;
		this.app = anonLoc;
		this.uid = uid;
		this.currentLocation = currentLocation;
	}

	public Runnable getThread() {
				
		return new Runnable() {

			@Override
			public void run() {
				String[] splitedLocation = currentLocation.split(",");
				try {
					String response = NetRequests.PinRequest(action, String.valueOf(app.getId()), uid, splitedLocation[1], splitedLocation[0]);
					response = response + ".";
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Action Pin: " + e);
				}
			}
		};
	}
}

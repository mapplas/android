package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.JsonParser;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.NetRequests;

public class ServerIdentificationThread {

	private SuperModel model;

	public ServerIdentificationThread(SuperModel model) {
		this.model = model;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					String response = NetRequests.UserIRequest(model.currentLocation(), model.currentIMEI());
					JsonParser jp = new JsonParser();

					model.setCurrentUser(jp.ParseUser(response));

				} catch (Exception e) {
					model.setCurrentUser(null);
					Log.d(this.getClass().getSimpleName(), "Login: " + e);
				}
			}
		};
	}
}

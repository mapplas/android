package com.mapplas.app.threads;

import android.content.Context;
import android.util.Log;

import com.mapplas.model.JsonParser;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.NetRequests;

public class ServerIdentificationThread {

	private SuperModel model;
	
	private Context context;

	public ServerIdentificationThread(SuperModel model, Context context) {
		this.model = model;
		this.context = context;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					String response = NetRequests.UserIRequest(model.currentIMEI());
					JsonParser jp = new JsonParser(context);

					model.setCurrentUser(jp.ParseUser(response));

				} catch (Exception e) {
					model.setCurrentUser(null);
					Log.d(this.getClass().getSimpleName(), "Login: " + e);
				}
			}
		};
	}
}

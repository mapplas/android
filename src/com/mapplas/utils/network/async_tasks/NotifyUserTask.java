package com.mapplas.utils.network.async_tasks;

import android.os.AsyncTask;

import com.mapplas.utils.network.connectors.NotifyUserConnector;


public class NotifyUserTask extends AsyncTask <Void, Void, Void> {
	
	private int userId;
	
	private String email;
	
	private double latitude;
	
	private double longitude;
	
	public NotifyUserTask(int userId, String email, double latitude, double longitude) {
		this.userId = userId;
		this.email = email;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			NotifyUserConnector.request(this.userId, this.email, this.latitude, this.longitude);
		} catch (Exception e) {
//			Log.d(this.getClass().getSimpleName(), "Get pin and blocks", e);
		}
		
		return null;
	}

}

package com.mapplas.app.async_tasks.user_form;

import android.os.AsyncTask;
import android.util.Log;

import com.mapplas.model.JsonParser;
import com.mapplas.model.User;
import com.mapplas.utils.network.requests.NetRequests;

public class UserBlocksTask extends AsyncTask<Void, Void, String> {

	private User user;
	
	private JsonParser parser;

	public UserBlocksTask(User user, JsonParser parser) {
		super();
		this.user = user;
		this.parser = parser;
	}

	@Override
	protected String doInBackground(Void... params) {
		String response = "";
		try {
			response = NetRequests.UserBlocksRequest(String.valueOf(this.user.getId()));
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "Get PinUps", e);
		}
		return response;
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);
		
		// Parse result and insert into user
		this.user.setBlockedApps(this.parser.parseApps(response));
	}
}
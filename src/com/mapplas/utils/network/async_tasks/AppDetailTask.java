package com.mapplas.utils.network.async_tasks;

import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.model.App;
import com.mapplas.utils.network.connectors.AppDetailConnector;
import com.mapplas.utils.network.mappers.JsonToAppDetailMapper;

public class AppDetailTask extends AsyncTask<Void, Void, String> {

	private AppDetail appDetail;

	private App app;

	private Location location;

	public AppDetailTask(AppDetail detailActivity, App app, Location location) {
		super();
		this.appDetail = detailActivity;
		this.app = app;
		this.location = location;
	}

	@Override
	protected String doInBackground(Void... params) {
		String server_response = "";
		try {
			server_response = AppDetailConnector.request(this.app.getId(), this.location);
		} catch (Exception e) {
			e.printStackTrace();
			this.appDetail.detailRequestFinishedNok();
		}
		return server_response;
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);

		// Parse response
		try {
			new JsonToAppDetailMapper().map(new JSONObject(response), this.app);
		} catch (Exception e) {
			this.appDetail.detailRequestFinishedNok();
		}

		this.appDetail.detailRequestFinishedOk();
	}

}
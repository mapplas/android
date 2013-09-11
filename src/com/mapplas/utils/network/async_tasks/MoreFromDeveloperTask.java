package com.mapplas.utils.network.async_tasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.mapplas.app.activities.MoreFromDeveloperActivity;
import com.mapplas.model.App;
import com.mapplas.utils.network.connectors.MoreFromDeveloperConnector;
import com.mapplas.utils.network.mappers.JsonToMoreFromDeveloperActivityMapper;

public class MoreFromDeveloperTask extends AsyncTask<Void, Void, String> {

	private MoreFromDeveloperActivity moreFromDevActivity;

	private App app;

	private String countryCode;
	
	private Context context;

	public MoreFromDeveloperTask(MoreFromDeveloperActivity moreFromDevActivity, App app, String country_code, Context context) {
		this.moreFromDevActivity = moreFromDevActivity;
		this.app = app;
		this.countryCode = country_code;
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		String server_response = "";
		try {
			server_response = MoreFromDeveloperConnector.request(this.app.getId(), this.countryCode);
		} catch (Exception e) {
			e.printStackTrace();
			this.moreFromDevActivity.requestFinishedNok();
		}
		return server_response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		// Parse response
		try {
			new JsonToMoreFromDeveloperActivityMapper(this.context).map(new JSONObject(result), this.app);
		} catch (Exception e) {
			this.moreFromDevActivity.requestFinishedNok();
		}

		this.moreFromDevActivity.requestFinishedOk();
	}

}

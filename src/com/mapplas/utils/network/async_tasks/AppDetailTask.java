package com.mapplas.utils.network.async_tasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.model.App;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.network.connectors.AppDetailConnector;
import com.mapplas.utils.network.mappers.JsonToAppDetailMapper;

public class AppDetailTask extends AsyncTask<Void, Void, String> {

	private AppDetail appDetail;

	private App app;
	
	private Context context;

	public AppDetailTask(AppDetail detailActivity, App app, Context context) {
		super();
		this.appDetail = detailActivity;
		this.app = app;
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		String server_response = "";
		try {
			
			server_response = AppDetailConnector.request(this.app.getId(), new LanguageSetter(this.context).getLanguageConstantFromPhone());
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
			new JsonToAppDetailMapper(this.context).map(new JSONObject(response), this.app);
		} catch (Exception e) {
			this.appDetail.detailRequestFinishedNok();
		}

		this.appDetail.detailRequestFinishedOk();
	}

}
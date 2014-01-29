package com.mapplas.utils.network.async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.network.connectors.GeofenceNotificationConnector;

public class GeofenceNotificationRequesterTask extends AsyncTask<Void, Void, String> {

	private int user_id;

	private int geofence_id;
	
	private Context context;

	public GeofenceNotificationRequesterTask(int uid, int gid, Context context) {
		this.user_id = uid;
		this.geofence_id = gid;
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		String server_response = "";
		try {
			server_response = GeofenceNotificationConnector.request(this.user_id, this.geofence_id, new LanguageSetter(this.context).getLanguageConstantFromPhone());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server_response;
	}

}

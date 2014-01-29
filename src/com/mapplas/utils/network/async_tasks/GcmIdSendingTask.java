package com.mapplas.utils.network.async_tasks;

import android.os.AsyncTask;

import com.mapplas.utils.network.connectors.GcmIdSendConnector;

public class GcmIdSendingTask extends AsyncTask<Void, Void, Void> {

	private int user_id;

	private String gcm_id;

	public GcmIdSendingTask(int user_id, String gcm_id) {
		this.user_id = user_id;
		this.gcm_id = gcm_id;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
//			server_response = GcmIdSendConnector.request(this.user_id, this.gcm_id);
			GcmIdSendConnector.request(this.user_id, this.gcm_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

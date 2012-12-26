package com.mapplas.app.async_tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mapplas.app.activities.UserForm;
import com.mapplas.model.Constants;
import com.mapplas.utils.NetRequests;

public class UserBlocksTask extends AsyncTask<Void, Void, Void> {

	private Handler messageHandler;

	private int userId;

	public UserBlocksTask(Handler handler, int userId) {
		super();
		this.messageHandler = handler;
		this.userId = userId;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			UserForm.currentResponse = NetRequests.UserBlocksRequest(String.valueOf(userId));
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "Get PinUps", e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Message.obtain(messageHandler, Constants.SYNESTH_USER_BLOCKS_ID, null).sendToTarget();
	}
}
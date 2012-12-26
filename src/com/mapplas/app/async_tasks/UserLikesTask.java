package com.mapplas.app.async_tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mapplas.app.activities.UserForm;
import com.mapplas.model.Constants;
import com.mapplas.utils.NetRequests;

public class UserLikesTask extends AsyncTask<Void, Void, Void> {

	private Handler messageHandler;

	private int userId;

	public UserLikesTask(Handler handler, int userId) {
		super();
		this.messageHandler = handler;
		this.userId = userId;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			UserForm.currentResponse = NetRequests.UserLikesRequest(String.valueOf(userId));
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "Get PinUps", e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Message.obtain(messageHandler, Constants.SYNESTH_USER_LIKES_ID, null).sendToTarget();
	}

}

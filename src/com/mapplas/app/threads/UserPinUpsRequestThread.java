package com.mapplas.app.threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mapplas.model.Constants;
import com.mapplas.utils.NetRequests;


public class UserPinUpsRequestThread {
	
	private Handler messageHandler;
	
//	private String response;
	
	private int userId;
	
	public UserPinUpsRequestThread(Handler handler, int userId) {
		this.messageHandler = handler;
//		this.response = response;
		this.userId = userId;
	}
	
	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
//					response = NetRequests.UserPinUpsRequest(String.valueOf(userId));
					NetRequests.UserPinUpsRequest(String.valueOf(userId));
					Message.obtain(messageHandler, Constants.SYNESTH_USER_PINUPS_ID, null).sendToTarget();

				} catch (Exception exc) {
					Log.d(this.getClass().getSimpleName(), "Get PinUps", exc);
				}

			}
		};
	}

}

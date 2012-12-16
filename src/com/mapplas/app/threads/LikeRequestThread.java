package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.utils.NetRequests;


public class LikeRequestThread {
	
	private String action;
	
	private App app;
	
	private String uid;
	
	public LikeRequestThread(String action, App anonLoc, String uid) {
		this.action = action;
		this.app = anonLoc;
		this.uid = uid;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					NetRequests.LikeRequest(action, Constants.SYNESTH_SERVER, Constants.SYNESTH_SERVER_PORT, Constants.SYNESTH_SERVER_PATH, app.getId() + "", uid);
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Action Like: " + e);
				}
			}
		};
	}

}

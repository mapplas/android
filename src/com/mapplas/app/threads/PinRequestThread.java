package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.utils.NetRequests;

public class PinRequestThread {

	private String action;

	private App app;

	private String uid;

	public PinRequestThread(String action, App anonLoc, String uid) {
		this.action = action;
		this.app = anonLoc;
		this.uid = uid;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					NetRequests.PinRequest(action, String.valueOf(app.getId()), uid);
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Action Pin: " + e);
				}
			}
		};
	}
}

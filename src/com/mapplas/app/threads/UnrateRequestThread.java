package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.utils.NetRequests;

public class UnrateRequestThread {
	
	private App app;
	
	private String uid;
	
	public UnrateRequestThread(App app, String ui) {
		this.app = app;
		this.uid = ui;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					NetRequests.UnrateRequest(String.valueOf(app.getId()), uid);
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Action Pin: " + e);
				}
			}
		};
	}
}

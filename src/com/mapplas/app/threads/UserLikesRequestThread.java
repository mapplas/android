package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.utils.NetRequests;

public class UserLikesRequestThread {

	private int uid;

	public UserLikesRequestThread(int uid) {
		this.uid = uid;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					NetRequests.UserLikesRequest(String.valueOf(uid));
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Edit User: " + e);
				}
			}
		};
	}
}

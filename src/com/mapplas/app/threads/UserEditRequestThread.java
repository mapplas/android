package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.User;
import com.mapplas.utils.NetRequests;


public class UserEditRequestThread {
	
	private User user;
	
	public UserEditRequestThread(User user) {
		this.user = user;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					NetRequests.UserEditRequest(user.getName(), user.getEmail(), user.getImei(), String.valueOf(user.getId()));
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Edit User: " + e);
				}
			}
		};
	}
}

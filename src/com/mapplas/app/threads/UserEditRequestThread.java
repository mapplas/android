package com.mapplas.app.threads;

import android.util.Log;

import com.mapplas.model.User;
import com.mapplas.utils.NetRequests;


public class UserEditRequestThread {
	
	private User user;
	
	private String response;
	
	public UserEditRequestThread(User user, String serverResponse) {
		this.user = user;
		this.response = serverResponse;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					response = NetRequests.UserEditRequest(user.getName(), user.getEmail(), user.getImei(), String.valueOf(user.getId()));
					user.setLoggedIn(true);
				} catch (Exception e) {
					Log.i(getClass().getSimpleName(), "Thread Edit User: " + e);
				}
			}
		};
	}
}

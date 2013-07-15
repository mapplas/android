package com.mapplas.utils.network.requests;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.connectors.UserIdentificationConnector;
import com.mapplas.utils.network.mappers.JsonToUserMapper;

public class UserIdentificationRequester {

	private SuperModel model;
	
	public UserIdentificationRequester(SuperModel model) {
		this.model = model;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					String response = UserIdentificationConnector.request(model.currentIMEI());
					
					JsonToUserMapper userMapper = new JsonToUserMapper();
					model.setCurrentUser(userMapper.map(new JSONObject(response)));

				} catch (Exception e) {
					model.setCurrentUser(null);
					Log.d(this.getClass().getSimpleName(), "Login: " + e);
				}
			}
		};
	}
}

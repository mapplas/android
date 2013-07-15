package com.mapplas.utils.network.requests;

import android.content.Context;
import android.util.Log;

import com.mapplas.model.JsonParser;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.connectors.UserIdentificationConnector;
import com.mapplas.utils.network.mappers.JsonToUserMapper;

public class UserIdentificationRequester {

	private SuperModel model;
	
	private Context context;

	public UserIdentificationRequester(SuperModel model, Context context) {
		this.model = model;
		this.context = context;
	}

	public Runnable getThread() {
		return new Runnable() {

			@Override
			public void run() {
				try {
					String response = UserIdentificationConnector.request(model.currentIMEI());
					JsonParser jp = new JsonParser(context);

					JsonToUserMapper userMapper = new JsonToUserMapper();
					//model.setCurrentUser(userMapper.map(response));

				} catch (Exception e) {
					model.setCurrentUser(null);
					Log.d(this.getClass().getSimpleName(), "Login: " + e);
				}
			}
		};
	}
}

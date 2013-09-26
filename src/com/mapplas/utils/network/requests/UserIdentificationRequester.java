package com.mapplas.utils.network.requests;

import org.json.JSONObject;

import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
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

					if(response.equals(Constants.SERVER_RESPONSE_ERROR_USER_IDENTIFICATION)) {
						setMockedUserToModel();
					}
					else {
						JsonToUserMapper userMapper = new JsonToUserMapper();
						model.setCurrentUser(userMapper.map(new JSONObject(response)));
					}

				} catch (Exception e) {
					setMockedUserToModel();
					// Log.d(this.getClass().getSimpleName(), "Login: " + e);
				}
			}
		};
	}

	private void setMockedUserToModel() {
		User mockedUser = new User();
		mockedUser.setId(-1);
		model.setCurrentUser(mockedUser);
	}
}

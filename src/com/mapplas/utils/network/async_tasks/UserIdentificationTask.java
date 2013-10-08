package com.mapplas.utils.network.async_tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.utils.network.connectors.UserIdentificationConnector;
import com.mapplas.utils.network.mappers.JsonToUserMapper;

public class UserIdentificationTask extends AsyncTask<Object, Object, String> {

	private SuperModel model;

	private Context context;

	private MapplasActivity mainActivity;

	private int retries;

	public UserIdentificationTask(SuperModel model, Context context, MapplasActivity mainActivity, int retries) {
		this.model = model;
		this.context = context;
		this.mainActivity = mainActivity;
		this.retries = retries;
	}

	@Override
	protected String doInBackground(Object... arg0) {
		return UserIdentificationConnector.request(model.currentIMEI(), context);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if(result.equals(Constants.USER_IDENTIFICATION_SERVER_RESPONSE_ERROR) && this.retries <= Constants.NUMBER_OF_REQUEST_RETRIES) {
			this.retries = this.retries + 1;
			new UserIdentificationTask(this.model, this.context, this.mainActivity, this.retries).execute();
		}
		else if(result.equals(Constants.USER_IDENTIFICATION_SOCKET_ERROR) && this.retries <= Constants.NUMBER_OF_REQUEST_RETRIES) {
			this.retries = this.retries + 1;
			new UserIdentificationTask(this.model, this.context, this.mainActivity, this.retries).execute();
		}
		else {
			JsonToUserMapper userMapper = new JsonToUserMapper();
			try {
				this.model.setCurrentUser(userMapper.map(new JSONObject(result)));
			} catch (JSONException e) {
				this.setMockedUserToModel();
			}
			this.mainActivity.continueActivityAfterUserIdentification();
		}
	}

	private void setMockedUserToModel() {
		User mockedUser = new User();
		mockedUser.setId(-1);
		this.model.setCurrentUser(mockedUser);
	}
}

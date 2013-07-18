package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.mappers.generic.JsonToAppMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToAppReponseMapper implements TargetMapper {

	private boolean resetPagination;

	public void setResetPagination(boolean reset) {
		this.resetPagination = reset;
	}

	@Override
	public void map(JSONObject json, Object target) {

		JsonToAppMapper appMapper = new JsonToAppMapper();
		SuperModel model = (SuperModel)target;

		try {
			if(json.has("last")) {
				int last = json.getInt("last");
				model.setMoreData(last == 0);
			}
			else {
				model.setMoreData(false);
			}

			if(json.has("apps")) {
				JSONArray apps = json.getJSONArray("apps");

				AppOrderedList appOrderedList = model.appList();
				ArrayList<App> mappedList = appMapper.map(apps);

				if(this.resetPagination) {
					appOrderedList = new AppOrderedList();
					appOrderedList.setAppList(mappedList);
				}
				else {
					for(int i = 0; i < mappedList.size(); i++) {
						appOrderedList.add(mappedList.get(i));
					}
				}

				model.setAppList(appOrderedList);
			}
			else {
				this.setMockedAppToList(model);
			}

		} catch (JSONException e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
			
			this.setMockedAppToList(model);
		}
	}
	
	public void setMockedAppToList(SuperModel model) {
		App mockedApp = new App();
		mockedApp.setId("");
		mockedApp.setAppType(Constants.MAPPLAS_APPLICATION_TYPE_MOCK);
		AppOrderedList list = new AppOrderedList();
		list.add(mockedApp);
		model.setAppList(list);
	}
}
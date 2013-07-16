package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.model.AppOrderedList;
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
			int last = json.getInt("last");
			model.setMoreData(last == 0);
			
			JSONArray apps = json.getJSONArray("apps");
			
			AppOrderedList appOrderedList = model.appList();
			ArrayList<App> mappedList = appMapper.map(apps);
			
			if(this.resetPagination) {
				appOrderedList = new AppOrderedList();
				appOrderedList.setAppList(mappedList);
			} else {
				for (int i=0; i<mappedList.size(); i++) {
					appOrderedList.add(mappedList.get(i));
				}
			}
			
			model.setAppList(appOrderedList);
			
		} catch (JSONException e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());		}
	}
}
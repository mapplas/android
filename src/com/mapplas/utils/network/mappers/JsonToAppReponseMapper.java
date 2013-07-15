package com.mapplas.utils.network.mappers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.AppOrderedList;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.mappers.generic.JsonToAppMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToAppReponseMapper implements TargetMapper {

	@Override
	public void map(JSONObject json, Object target) {
		
		JsonToAppMapper appMapper = new JsonToAppMapper();
		SuperModel model = (SuperModel)target;
		
		try {
			int last = json.getInt("last");
			
			JSONArray apps = json.getJSONArray("apps");
			AppOrderedList appOrderedList = new AppOrderedList();
			appOrderedList.setAppList(appMapper.map(apps));
			model.setAppList(appOrderedList);
			
		} catch (JSONException e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());		}
	}
}
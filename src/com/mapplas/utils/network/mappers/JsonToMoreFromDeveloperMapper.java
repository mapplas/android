package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;

public class JsonToMoreFromDeveloperMapper implements IteratingMapper {

	@Override
	public ArrayList<MoreFromDeveloperApp> map(JSONArray json) {

		JsonToMoreAppsMapper subMapper = new JsonToMoreAppsMapper();
		ArrayList<MoreFromDeveloperApp> appList = new ArrayList<MoreFromDeveloperApp>();

		for(int i = 0; i < json.length(); i++) {

			try {
				appList.add(subMapper.map(json.getJSONObject(i)));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return appList;
	}

}

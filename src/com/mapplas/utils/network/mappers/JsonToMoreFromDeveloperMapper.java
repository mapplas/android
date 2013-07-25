package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;

public class JsonToMoreFromDeveloperMapper implements IteratingMapper {

	private ArrayList<MoreFromDeveloperApp> mapList;

	public JsonToMoreFromDeveloperMapper(ArrayList<MoreFromDeveloperApp> mapList) {
		this.mapList = mapList;
	}

	@Override
	public ArrayList<MoreFromDeveloperApp> map(JSONArray json) {

		JsonToMoreAppsMapper subMapper = new JsonToMoreAppsMapper();

		for(int i = 0; i < json.length(); i++) {

			try {
				this.mapList.add(subMapper.map(json.getJSONObject(i)));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return this.mapList;
	}

}

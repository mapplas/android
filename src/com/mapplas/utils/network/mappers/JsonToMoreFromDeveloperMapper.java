package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;

public class JsonToMoreFromDeveloperMapper implements IteratingMapper {

	private ArrayList<MoreFromDeveloperApp> mapList;
	
	private Context context;

	public JsonToMoreFromDeveloperMapper(ArrayList<MoreFromDeveloperApp> mapList, Context context) {
		this.mapList = mapList;
		this.context = context;
	}

	@Override
	public ArrayList<MoreFromDeveloperApp> map(JSONArray json) {

		JsonToMoreAppsMapper subMapper = new JsonToMoreAppsMapper(this.context);

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

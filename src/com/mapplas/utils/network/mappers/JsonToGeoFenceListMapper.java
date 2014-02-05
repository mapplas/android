package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.mapplas.model.GeoFence;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;

public class JsonToGeoFenceListMapper implements IteratingMapper {

	private JsonToGeoFenceMapper geoFenceMapper;

	public JsonToGeoFenceListMapper(JsonToGeoFenceMapper geoFenceMapper) {
		this.geoFenceMapper = geoFenceMapper;
	}

	@Override
	public ArrayList<GeoFence> map(JSONArray json) {

		ArrayList<GeoFence> fences = new ArrayList<GeoFence>();

		for(int i = 0; i < json.length(); ++i) {
			try {
				fences.add(this.geoFenceMapper.map(json.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return fences;
	}

}
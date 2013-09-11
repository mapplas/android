package com.mapplas.utils.network.mappers.generic.base;

import java.lang.reflect.Method;

import org.json.JSONObject;

import android.util.Log;

public class KeySubmapperMapper implements TargetMapper {

	private String key;

	private Method method;

	private ReturnMapper subMapper;

	public KeySubmapperMapper(String key, Method method, ReturnMapper subMapper) {
		this.key = key;
		this.method = method;
		this.subMapper = subMapper;
	}

	@Override
	public void map(JSONObject json, Object target) {
		try {
			JSONObject subJson = json.getJSONObject(this.key);
			Object mappedObject = this.subMapper.map(subJson);
			method.invoke(target, mappedObject);
		} catch (Exception e) {
			Log.i(this.getClass().getSimpleName(), "Mapping Value Failed. " + e.getMessage());
		}
	}

}

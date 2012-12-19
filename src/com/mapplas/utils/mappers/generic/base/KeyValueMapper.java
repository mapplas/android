package com.mapplas.utils.mappers.generic.base;

import java.lang.reflect.Method;

import org.json.JSONObject;

import android.util.Log;

public class KeyValueMapper implements TargetMapper {

	private final String key;

	private final Method method;

	public KeyValueMapper(String key, Method method) {
		this.key = key;
		this.method = method;
	}

	@Override
	public void map(JSONObject json, Object target) {
		try {
			Object mappedValue = json.getInt(this.key);
			method.invoke(target, mappedValue);
		} catch (Exception e) {
			Log.w(this.getClass().getSimpleName(), "Mapping Value Failed. " + e.getMessage());
		}
	}
}

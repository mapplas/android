package com.mapplas.utils.network.mappers.generic.base;

import java.lang.reflect.Method;

import org.json.JSONObject;

public class KeyIntegerValueMapper implements TargetMapper {

	private final String key;

	private final Method method;

	public KeyIntegerValueMapper(String key, Method method) {
		this.key = key;
		this.method = method;
	}

	@Override
	public void map(JSONObject json, Object target) {
		try {
			Object mappedValue = json.getInt(this.key);
			method.invoke(target, mappedValue);
		} catch (Exception e) {
//			Log.w(this.getClass().getSimpleName(), "Mapping Value Failed. " + e.getMessage());
		}
	}
}

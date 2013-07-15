package com.mapplas.utils.network.mappers.generic.base;

import java.lang.reflect.Method;

import org.json.JSONObject;

import android.util.Log;

public class KeyValueScapedMapper implements TargetMapper {

	private final String key;

	private final Method method;

	public KeyValueScapedMapper(String key, Method method) {
		this.key = key;
		this.method = method;
	}

	@Override
	public void map(JSONObject json, Object target) {
		try {
			String mappedValue = json.getString(this.key);
//			String escapedValue = StringEscapeUtils.unescapeHtml4(mappedValue);
			method.invoke(target, mappedValue);
		} catch (Exception e) {
			Log.i(this.getClass().getSimpleName(), "Mapping Value Failed. " + e.getMessage());
		}
	}
}

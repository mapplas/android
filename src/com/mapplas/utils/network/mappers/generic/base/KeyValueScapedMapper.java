package com.mapplas.utils.network.mappers.generic.base;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

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
			String escapedValue = StringEscapeUtils.unescapeHtml4(json.getString(this.key));
			method.invoke(target, escapedValue);
		} catch (Exception e) {
//			Log.i(this.getClass().getSimpleName(), "Mapping Value Failed. " + e.getMessage());
		}
	}
}

package com.mapplas.utils.mappers.generic.base;

import org.json.JSONObject;


public interface TargetMapper {
	public void map(JSONObject json, Object target);
}

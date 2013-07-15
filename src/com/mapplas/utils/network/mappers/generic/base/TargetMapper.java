package com.mapplas.utils.network.mappers.generic.base;

import org.json.JSONObject;


public interface TargetMapper {
	public void map(JSONObject json, Object target);
}

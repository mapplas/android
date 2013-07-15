package com.mapplas.utils.network.mappers.generic.base;

import java.util.ArrayList;

import org.json.JSONArray;

public interface IteratingMapper {
	public ArrayList<?> map(JSONArray json);
}

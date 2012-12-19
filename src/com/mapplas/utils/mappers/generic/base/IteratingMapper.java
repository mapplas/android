package com.mapplas.utils.mappers.generic.base;

import java.util.ArrayList;

import org.json.JSONArray;

public interface IteratingMapper {
	public ArrayList<?> map(JSONArray json);
}

package com.mapplas.utils.mappers.generic;

import java.util.ArrayList;

import org.json.JSONObject;

import com.mapplas.utils.mappers.generic.base.TargetMapper;

public class GenericMapper implements TargetMapper {

	private ArrayList<TargetMapper> mappers;

	public GenericMapper(ArrayList<TargetMapper> mappers) {
		this.mappers = mappers;
	}

	public void map(JSONObject json, Object target) {
		for(TargetMapper actualMapper : this.mappers) {
			actualMapper.map(json, target);
		}
	}
}

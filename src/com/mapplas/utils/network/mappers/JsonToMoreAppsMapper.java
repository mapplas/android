package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.ReturnMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToMoreAppsMapper implements ReturnMapper {

	@Override
	public MoreFromDeveloperApp map(JSONObject json) {

		MoreFromDeveloperApp app = new MoreFromDeveloperApp();

		try {
			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyValueScapedMapper("i", MoreFromDeveloperApp.class.getMethod("setId", String.class)));
			mappers.add(new KeyValueScapedMapper("t", MoreFromDeveloperApp.class.getMethod("setName", String.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, app);

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}

		return app;
	}

}

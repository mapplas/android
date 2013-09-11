package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.mapplas.model.App;
import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToMoreFromDeveloperActivityMapper implements TargetMapper {

	private Context context;

	public JsonToMoreFromDeveloperActivityMapper(Context context) {
		this.context = context;
	}

	@Override
	public void map(JSONObject json, Object target) {
		App app = (App)target;

		try {
			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyValueScapedMapper("d", App.class.getMethod("setDeveloperName", String.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, app);

			this.moreFromDeveloper(json.getJSONArray("apps"), app);

		} catch (Exception e) {
//			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}
	}

	private void moreFromDeveloper(JSONArray json, App app) {
		app.moreFromDev().clear();
		ArrayList<MoreFromDeveloperApp> mapList = app.moreFromDev();
		new JsonToMoreFromDeveloperMapper(mapList, this.context).map(json);
	}

}

package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToAppDetailMapper implements TargetMapper {

	@Override
	public void map(JSONObject json, Object target) {
		App app = (App)target;
		
		try {
			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyValueScapedMapper("d", App.class.getMethod("setAppDescription", String.class)));
			mappers.add(new KeyValueScapedMapper("surl", App.class.getMethod("setAppDeveloperEmail", String.class)));
			mappers.add(new KeyValueScapedMapper("curl", App.class.getMethod("setAppDeveloperWeb", String.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, app);
			
			this.parseScreenshots(json.getString("scr"), app);

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}
	}

	private void parseScreenshots(String screenshots, App app) {
		String[] screenshotList = screenshots.split(",");
		ArrayList<String> photos = new ArrayList<String>();
	
		for(int i = 0; i < screenshotList.length; i++) {
			photos.add(screenshotList[i]);
		}
		
		app.setAuxPhotos(photos);
	}

}

package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.mapplas.model.App;
import com.mapplas.utils.image.PixelDensityImageChooser;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;

public class JsonToBlockedAppsMapper implements IteratingMapper {

	private Context context;

	public JsonToBlockedAppsMapper(Context context) {
		this.context = context;
	}

	@Override
	public ArrayList<App> map(JSONArray json) {
		ArrayList<App> blockedApps = new ArrayList<App>();

		for(int i = 0; i < json.length(); i++) {

			try {
				JSONObject currentObject = (JSONObject)json.get(i);

				App app = new App();
				app.setId(currentObject.getString("id"));
				app.setAppLogo(currentObject.getString("i"));
				app.setName(currentObject.getString("n"));

				this.changeLogoUrlDependingOnDensity(app);

				blockedApps.add(app);
			} catch (Exception e) {

			}
		}

		return blockedApps;
	}

	private void changeLogoUrlDependingOnDensity(App app) {
		app.setAppLogo(new PixelDensityImageChooser(this.context).getImageUrlDependingOnDensity(app.getAppLogo()));
	}
}

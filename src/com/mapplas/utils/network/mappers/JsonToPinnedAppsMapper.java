package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.mapplas.model.App;
import com.mapplas.utils.image.PixelDensityImageChooser;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;

public class JsonToPinnedAppsMapper implements IteratingMapper {

	private Context context;

	public JsonToPinnedAppsMapper(Context context) {
		this.context = context;
	}

	@Override
	public ArrayList<App> map(JSONArray json) {
		ArrayList<App> pinnedApps = new ArrayList<App>();

		for(int i = 0; i < json.length(); i++) {

			try {
				JSONObject currentObject = (JSONObject)json.get(i);

				App app = new App();
				app.setId(currentObject.getString("id"));
				app.setAppLogo(currentObject.getString("i"));
				app.setName(currentObject.getString("n"));
				app.setAdress(currentObject.getString("a"));

				this.changeLogoUrlDependingOnDensity(app);

				pinnedApps.add(app);
			} catch (Exception e) {

			}
		}

		return pinnedApps;
	}

	private void changeLogoUrlDependingOnDensity(App app) {
		app.setAppLogo(new PixelDensityImageChooser(this.context).getImageUrlDependingOnDensity(app.getAppLogo()));
	}

}

package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.mapplas.model.App;
import com.mapplas.utils.image.PixelDensityImageChooser;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

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

				ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

				mappers.add(new KeyValueScapedMapper("id", App.class.getMethod("setId", String.class)));
				mappers.add(new KeyValueScapedMapper("i", App.class.getMethod("setAppLogo", String.class)));
				mappers.add(new KeyValueScapedMapper("n", App.class.getMethod("setName", String.class)));

				GenericMapper mapper = new GenericMapper(mappers);
				mapper.map(currentObject, app);

				this.changeLogoUrlDependingOnDensity(app);

				blockedApps.add(app);
			} catch (Exception e) {
//				Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
			}
		}

		return blockedApps;
	}

	private void changeLogoUrlDependingOnDensity(App app) {
		app.setAppLogo(new PixelDensityImageChooser(this.context).getImageUrlDependingOnDensity(app.getAppLogo()));
	}
}
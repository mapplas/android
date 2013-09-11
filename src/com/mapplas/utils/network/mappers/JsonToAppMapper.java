package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mapplas.model.App;
import com.mapplas.utils.image.PixelDensityImageChooser;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.IteratingMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyIntegerValueMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToAppMapper implements IteratingMapper {

	private Context context;

	public JsonToAppMapper(Context context) {
		this.context = context;
	}

	@Override
	public ArrayList<App> map(JSONArray json) {

		ArrayList<App> apps = new ArrayList<App>();

		for(int i = 0; i < json.length(); ++i) {
			try {
				apps.add(this.map(json.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return apps;
	}

	private App map(JSONObject json) {
		App app = null;

		try {
			app = new App();

			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyValueScapedMapper("id", App.class.getMethod("setId", String.class)));
			mappers.add(new KeyValueScapedMapper("n", App.class.getMethod("setName", String.class)));
			mappers.add(new KeyValueScapedMapper("i", App.class.getMethod("setAppLogo", String.class)));
			mappers.add(new KeyValueScapedMapper("sd", App.class.getMethod("setAppShortDescription", String.class)));
			mappers.add(new KeyIntegerValueMapper("pin", App.class.getMethod("setAuxPin", int.class)));
			mappers.add(new KeyValueScapedMapper("pr", App.class.getMethod("setAppPrice", String.class)));
			mappers.add(new KeyValueScapedMapper("r", App.class.getMethod("setRating", String.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, app);

			this.changeLogoUrlDependingOnDensity(app);

		} catch (Exception e) {
//			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}

		return app;
	}

	private void changeLogoUrlDependingOnDensity(App app) {
		app.setAppLogo(new PixelDensityImageChooser(this.context).getImageUrlDependingOnDensity(app.getAppLogo()));
	}
}
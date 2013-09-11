package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;

import com.mapplas.model.MoreFromDeveloperApp;
import com.mapplas.utils.image.PixelDensityImageChooser;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.ReturnMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToMoreAppsMapper implements ReturnMapper {

	private Context context;

	public JsonToMoreAppsMapper(Context context) {
		this.context = context;
	}

	@Override
	public MoreFromDeveloperApp map(JSONObject json) {

		MoreFromDeveloperApp app = new MoreFromDeveloperApp();

		try {
			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyValueScapedMapper("i", MoreFromDeveloperApp.class.getMethod("setId", String.class)));
			mappers.add(new KeyValueScapedMapper("t", MoreFromDeveloperApp.class.getMethod("setName", String.class)));
			mappers.add(new KeyValueScapedMapper("l", MoreFromDeveloperApp.class.getMethod("setLogo", String.class)));
			mappers.add(new KeyValueScapedMapper("d", MoreFromDeveloperApp.class.getMethod("setShortDescription", String.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, app);

			this.changeLogoUrlDependingOnDensity(app);

		} catch (Exception e) {
//			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}

		return app;
	}

	private void changeLogoUrlDependingOnDensity(MoreFromDeveloperApp app) {
		app.setLogo(new PixelDensityImageChooser(this.context).getImageUrlDependingOnDensity(app.logo()));
	}

}

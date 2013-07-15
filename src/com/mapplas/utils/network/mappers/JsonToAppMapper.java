package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueMapper;
import com.mapplas.utils.network.mappers.generic.base.ReturnMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToAppMapper implements ReturnMapper {

	@Override
	public App map(JSONObject json) {
		App app = null;

		try {
			app = new App();

			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyValueMapper("IDLocalization", App.class.getMethod("setIDLocalization", int.class)));
			mappers.add(new KeyValueMapper("Name", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("Latitude", App.class.getMethod("setIDLocalization", double.class)));
			mappers.add(new KeyValueMapper("Longitude", App.class.getMethod("setIDLocalization", double.class)));
			mappers.add(new KeyValueMapper("Address", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("ZipCode", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("State", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("City", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("Country", App.class.getMethod("setIDLocalization", String.class)));

			mappers.add(new KeyValueMapper("Type", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("IDCompany", App.class.getMethod("setIDLocalization", int.class)));

			mappers.add(new KeyValueMapper("OfferID", App.class.getMethod("setIDLocalization", int.class)));
			mappers.add(new KeyValueMapper("OfferName", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("OfferLogo", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("OfferLogoMini", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("OfferURL", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("OfferText", App.class.getMethod("setIDLocalization", String.class)));

			mappers.add(new KeyValueMapper("URLID", App.class.getMethod("setIDLocalization", int.class)));
			mappers.add(new KeyValueMapper("URLName", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("URLLogo", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("URLLogoMini", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("URLValue", App.class.getMethod("setIDLocalization", String.class)));
			mappers.add(new KeyValueMapper("URLText", App.class.getMethod("setIDLocalization", String.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, app);

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}

		return app;
	}

}

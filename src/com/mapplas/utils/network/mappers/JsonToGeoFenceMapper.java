package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.GeoFence;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyFloatValueMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyIntegerValueMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.ReturnMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToGeoFenceMapper implements ReturnMapper {

	@Override
	public GeoFence map(JSONObject json) {
		GeoFence geoFence = new GeoFence();

		try {
			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyIntegerValueMapper("id", GeoFence.class.getMethod("setId", int.class)));
			mappers.add(new KeyValueScapedMapper("lat", GeoFence.class.getMethod("setLatitude", String.class)));
			mappers.add(new KeyValueScapedMapper("lon", GeoFence.class.getMethod("setLongitude", String.class)));
			mappers.add(new KeyFloatValueMapper("r", GeoFence.class.getMethod("setRadius", float.class)));
			mappers.add(new KeyIntegerValueMapper("t", GeoFence.class.getMethod("setTransitionType", int.class)));
			mappers.add(new KeyIntegerValueMapper("e", GeoFence.class.getMethod("setExpirationDuration", int.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, geoFence);
			
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), e.toString());
		}
		return geoFence;
	}
}

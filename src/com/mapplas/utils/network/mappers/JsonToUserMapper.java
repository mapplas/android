package com.mapplas.utils.network.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.User;
import com.mapplas.utils.network.mappers.generic.GenericMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyIntegerValueMapper;
import com.mapplas.utils.network.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.network.mappers.generic.base.ReturnMapper;
import com.mapplas.utils.network.mappers.generic.base.TargetMapper;

public class JsonToUserMapper implements ReturnMapper {

	@Override
	public User map(JSONObject json) {
		User user = null;

		try {
			user = new User();

			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyIntegerValueMapper("user", User.class.getMethod("setId", int.class)));
			mappers.add(new KeyValueScapedMapper("tel", User.class.getMethod("setTelf", String.class)));
			mappers.add(new KeyValueScapedMapper("imei", User.class.getMethod("setImei", String.class)));

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, user);

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}

		return user;
	}
}

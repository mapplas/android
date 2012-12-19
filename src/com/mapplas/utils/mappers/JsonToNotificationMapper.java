package com.mapplas.utils.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.notifications.Notification;
import com.mapplas.utils.mappers.generic.GenericMapper;
import com.mapplas.utils.mappers.generic.base.KeyValueMapper;
import com.mapplas.utils.mappers.generic.base.KeyValueScapedMapper;
import com.mapplas.utils.mappers.generic.base.ReturnMapper;
import com.mapplas.utils.mappers.generic.base.TargetMapper;

public class JsonToNotificationMapper implements ReturnMapper {

	@Override
	public Notification map(JSONObject json) {
		Notification notification = null;

		try {
			notification = new Notification();

			ArrayList<TargetMapper> mappers = new ArrayList<TargetMapper>();

			mappers.add(new KeyValueMapper("IDNewsfeed", Notification.class.getMethod("setId", int.class)));
			mappers.add(new KeyValueMapper("IDCompany", Notification.class.getMethod("setIdCompany", int.class)));
			mappers.add(new KeyValueMapper("IDLocalization", Notification.class.getMethod("setIdLocalization", int.class)));

			mappers.add(new KeyValueScapedMapper("Title", Notification.class.getMethod("setName", String.class)));
			mappers.add(new KeyValueScapedMapper("Body", Notification.class.getMethod("setDescription", String.class)));
			mappers.add(new KeyValueScapedMapper("Hour", Notification.class.getMethod("setHour", String.class)));
			mappers.add(new KeyValueScapedMapper("Date", Notification.class.getMethod("setDate", String.class)));
			
			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, notification);

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}

		return notification;
	}

}

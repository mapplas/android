package com.mapplas.utils.mappers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.mapplas.model.App;
import com.mapplas.utils.mappers.generic.GenericMapper;
import com.mapplas.utils.mappers.generic.base.KeyValueMapper;
import com.mapplas.utils.mappers.generic.base.ReturnMapper;
import com.mapplas.utils.mappers.generic.base.TargetMapper;


public class JSONToAppMapper implements ReturnMapper {

	

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

//			TODO: finish

//			loc.setAppId(jArray.getJSONObject(i).getInt("AppID"));
//			loc.setAppName(jArray.getJSONObject(i).getString("AppName"));
//			loc.setAppLogo(jArray.getJSONObject(i).getString("AppLogo"));
//			loc.setAppLogoMini(jArray.getJSONObject(i).getString("AppLogoMini"));
//			loc.setAppUrl(jArray.getJSONObject(i).getString("AppURL"));
//			loc.setAppDescription(jArray.getJSONObject(i).getString("AppDescription"));
//			loc.setAppType(jArray.getJSONObject(i).getString("AppType"));
//
//			loc.setUserAlarmId(jArray.getJSONObject(i).getInt("UserAlarmID"));
//			loc.setUserAlarmName(jArray.getJSONObject(i).getString("UserAlarmName"));
//
//			loc.setUserUrlId(jArray.getJSONObject(i).getInt("UserURLID"));
//			loc.setUserUrlTags(jArray.getJSONObject(i).getString("UserURLTags"));
//			loc.setUserUrlComment(jArray.getJSONObject(i).getString("UserURLComment"));
//			loc.setUserUrlPhoto(jArray.getJSONObject(i).getString("UserURLPhoto"));
//			loc.setUserUrlValue(jArray.getJSONObject(i).getString("UserURLValue"));
//			loc.setUserUrlDescription(jArray.getJSONObject(i).getString("UserURLDescription"));
//
//			loc.setIdUser(jArray.getJSONObject(i).getInt("IDUser"));
//
//			loc.setRadius(jArray.getJSONObject(i).getDouble("Radius"));
//
//			loc.setPhone(jArray.getJSONObject(i).getString("Phone"));
//			loc.setWifi(jArray.getJSONObject(i).getString("Wifi"));
//			loc.setBluetooth(jArray.getJSONObject(i).getString("Bluetooth"));
//			loc.setLocation(jArray.getJSONObject(i).getString("Location"));
//
//			loc.setAppPrice((float)jArray.getJSONObject(i).getDouble("AppPrice"));
//
//			loc.setAuxPlus(jArray.getJSONObject(i).getInt("AuxPlus"));
//			loc.setAuxMinus(jArray.getJSONObject(i).getInt("AuxMinus"));
//
//			loc.setAuxFavourite(jArray.getJSONObject(i).getBoolean("AuxFavourite"));
//
//			loc.setAuxPin(jArray.getJSONObject(i).getBoolean("AuxPin"));
//
//			loc.setAuxRate((float)jArray.getJSONObject(i).getDouble("AuxRate"));
//			loc.setAuxComment(jArray.getJSONObject(i).getString("AuxComment"));
//
//			loc.setAuxTotalRate((float)jArray.getJSONObject(i).getDouble("AuxTotalRate"));
//			loc.setAuxTotalPins(jArray.getJSONObject(i).getInt("AuxTotalPins"));
//			loc.setAuxTotalComments(jArray.getJSONObject(i).getInt("AuxTotalComments"));
	

			GenericMapper mapper = new GenericMapper(mappers);
			mapper.map(json, app);

		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "Failed mapping, reason: " + e.getMessage());
		}
		
		return app;
	}

}

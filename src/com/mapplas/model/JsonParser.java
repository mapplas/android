package com.mapplas.model;


import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;



public class JsonParser implements ISynesthParser{

	private JSONArray jArray;
	private JSONObject jObject;
	
	@Override
	public void Init() {
		
	}

	@Override
	public void Delete() {
	
		
	}

	@Override
	public void ParseLocalizations(String input, SuperModel model, boolean append) {
		model.ResetError();
		String jString = input;
		
		if(!append)
		{
			model.ResetModel();
		}
		
		try {
			jArray = new JSONArray(jString);
			
			for(int i = 0; i < jArray.length(); i++)
			{
				Localization loc = new Localization();
				loc.setId(jArray.getJSONObject(i).getInt("IDLocalization"));
				loc.setName(jArray.getJSONObject(i).getString("Name"));
				loc.setLatitude(jArray.getJSONObject(i).getDouble("Latitude"));
				loc.setLongitude(jArray.getJSONObject(i).getDouble("Longitude"));
				loc.setAddress(jArray.getJSONObject(i).getString("Address"));
				loc.setZipCode(jArray.getJSONObject(i).getString("ZipCode"));
				loc.setState(jArray.getJSONObject(i).getString("State"));
				loc.setCity(jArray.getJSONObject(i).getString("City"));
				loc.setCountry(jArray.getJSONObject(i).getString("Country"));
				
				loc.setType(jArray.getJSONObject(i).getString("Type"));
				
				loc.setIdCompany(jArray.getJSONObject(i).getInt("IDCompany"));
				
				loc.setOfferId(jArray.getJSONObject(i).getInt("OfferID"));
				loc.setOfferName(jArray.getJSONObject(i).getString("OfferName"));
				loc.setOfferLogo(jArray.getJSONObject(i).getString("OfferLogo"));
				loc.setOfferLogoMini(jArray.getJSONObject(i).getString("OfferLogoMini"));
				loc.setOfferURL(jArray.getJSONObject(i).getString("OfferURL"));
				loc.setOfferText(jArray.getJSONObject(i).getString("OfferText"));
				
				loc.setUrlId(jArray.getJSONObject(i).getInt("URLID"));
				loc.setUrlName(jArray.getJSONObject(i).getString("URLName"));
				loc.setUrlLogo(jArray.getJSONObject(i).getString("URLLogo"));
				loc.setUrlLogoMini(jArray.getJSONObject(i).getString("URLLogoMini"));
				loc.setUrlValue(jArray.getJSONObject(i).getString("URLValue"));
				loc.setUrlText(jArray.getJSONObject(i).getString("URLText"));
				
				loc.setAppId(jArray.getJSONObject(i).getInt("AppID"));
				loc.setAppName(jArray.getJSONObject(i).getString("AppName"));
				loc.setAppLogo(jArray.getJSONObject(i).getString("AppLogo"));
				loc.setAppLogoMini(jArray.getJSONObject(i).getString("AppLogoMini"));
				loc.setAppUrl(jArray.getJSONObject(i).getString("AppURL"));
				loc.setAppDescription(jArray.getJSONObject(i).getString("AppDescription"));
				loc.setAppType(jArray.getJSONObject(i).getString("AppType"));
				
				loc.setUserAlarmId(jArray.getJSONObject(i).getInt("UserAlarmID"));
				loc.setUserAlarmName(jArray.getJSONObject(i).getString("UserAlarmName"));
				
				loc.setUserUrlId(jArray.getJSONObject(i).getInt("UserURLID"));
				loc.setUserUrlTags(jArray.getJSONObject(i).getString("UserURLTags"));
				loc.setUserUrlComment(jArray.getJSONObject(i).getString("UserURLComment"));
				loc.setUserUrlPhoto(jArray.getJSONObject(i).getString("UserURLPhoto"));
				loc.setUserUrlValue(jArray.getJSONObject(i).getString("UserURLValue"));
				loc.setUserUrlDescription(jArray.getJSONObject(i).getString("UserURLDescription"));
				
				loc.setIdUser(jArray.getJSONObject(i).getInt("IDUser"));
				
				loc.setRadius(jArray.getJSONObject(i).getDouble("Radius"));
				
				loc.setPhone(jArray.getJSONObject(i).getString("Phone"));
				loc.setWifi(jArray.getJSONObject(i).getString("Wifi"));
				loc.setBluetooth(jArray.getJSONObject(i).getString("Bluetooth"));
				loc.setLocation(jArray.getJSONObject(i).getString("Location"));
				
				loc.setAppPrice((float)jArray.getJSONObject(i).getDouble("AppPrice"));
				
				loc.setAuxPlus(jArray.getJSONObject(i).getInt("AuxPlus"));
				loc.setAuxMinus(jArray.getJSONObject(i).getInt("AuxMinus"));
				
				loc.setAuxFavourite(jArray.getJSONObject(i).getBoolean("AuxFavourite"));
				
				loc.setAuxPin(jArray.getJSONObject(i).getBoolean("AuxPin"));
				
				loc.setAuxRate((float) jArray.getJSONObject(i).getDouble("AuxRate"));
				loc.setAuxComment(jArray.getJSONObject(i).getString("AuxComment"));
				
				loc.setAuxTotalRate((float)jArray.getJSONObject(i).getDouble("AuxTotalRate"));
				loc.setAuxTotalPins(jArray.getJSONObject(i).getInt("AuxTotalPins"));
				loc.setAuxTotalComments(jArray.getJSONObject(i).getInt("AuxTotalComments"));
				
				JSONArray auxArray = jArray.getJSONObject(i).getJSONArray("AuxNews");
				for(int j = 0; j < auxArray.length(); j++)
				{
					AppNotification not = new AppNotification();
					not.setId(auxArray.getJSONObject(j).getInt("IDNewsfeed"));
					not.setIdCompany(auxArray.getJSONObject(j).getInt("IDCompany"));
					not.setIdLocalization(auxArray.getJSONObject(j).getInt("IDLocalization"));
					not.setName(auxArray.getJSONObject(j).getString("Title"));
					not.setDescription(URLDecoder.decode(auxArray.getJSONObject(j).getString("Body")));
					not.setHour(auxArray.getJSONObject(j).getString("Hour"));
					not.setDate(auxArray.getJSONObject(j).getString("Date"));
					
					not.setAuxLocalization(loc);
					
					model.notifications.add(not);
				}
				
				auxArray = jArray.getJSONObject(i).getJSONArray("AuxComments");
				for(int j = 0; j < auxArray.length(); j++)
				{
					Comment com = new Comment();
					com.setId(auxArray.getJSONObject(j).getInt("IDComment"));
					com.setIdLocalization(auxArray.getJSONObject(j).getInt("IDLocalization"));
					com.setComment(URLDecoder.decode(auxArray.getJSONObject(j).getString("Comment")));
					com.setDate(auxArray.getJSONObject(j).getString("Date"));
					com.setHour(auxArray.getJSONObject(j).getString("Hour"));
					com.setRate((float)auxArray.getJSONObject(j).getDouble("Rate"));
					com.setIdUser(auxArray.getJSONObject(j).getInt("IDUser"));
					
					com.setAuxLocalization(loc);
					
					loc.getAuxComments().add(com);
				}
				
				auxArray = jArray.getJSONObject(i).getJSONArray("AuxPhotos");
				for(int j = 0; j < auxArray.length(); j++)
				{
					Photo pho = new Photo();
					pho.setId(auxArray.getJSONObject(j).getInt("IDPhoto"));
					pho.setIdLocalization(auxArray.getJSONObject(j).getInt("IDLocalization"));
					pho.setComment(auxArray.getJSONObject(j).getString("Comment"));
					pho.setDate(auxArray.getJSONObject(j).getString("Date"));
					pho.setHour(auxArray.getJSONObject(j).getString("Hour"));
					pho.setPhoto(auxArray.getJSONObject(j).getString("Photo"));
					pho.setIdUser(auxArray.getJSONObject(j).getInt("IDUser"));
					
					pho.setAuxLocalization(loc);
					
					loc.getAuxPhotos().add(pho);
				}
				
				model.localizations.add(loc);
			}
			
		} catch (Exception e) {
			model.operationError = true;
			model.errorText = e.getMessage();
			e.printStackTrace();
		}
	}

	@Override
	public User ParseUser(String input) {
		String jString = input;
		
		try {
			jObject = new JSONObject(jString);
			
			User usr = new User();
				
			usr.setId(jObject.getInt("IDUser"));
			usr.setName(jObject.getString("Name"));
			usr.setLastname(jObject.getString("Lastname"));
			usr.setGender(jObject.getString("Gender"));
			usr.setBirthdate(jObject.getString("Birthdate"));
			usr.setLogin(jObject.getString("Login"));
			usr.setPassword(jObject.getString("Password"));
			usr.setEmail(jObject.getString("Email"));
			usr.setImei(jObject.getString("Imei"));
			
			return usr;

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Localization ParseLocalization(String input) {
		Localization loc = new Localization();
		String jString = input;
		
		
		try {
			jObject = new JSONObject(jString);
			

			loc.setId(jObject.getInt("IDLocalization"));
			loc.setName(jObject.getString("Name"));
			loc.setLatitude(jObject.getDouble("Latitude"));
			loc.setLongitude(jObject.getDouble("Longitude"));
			loc.setAddress(jObject.getString("Address"));
			loc.setZipCode(jObject.getString("ZipCode"));
			loc.setState(jObject.getString("State"));
			loc.setCity(jObject.getString("City"));
			loc.setCountry(jObject.getString("Country"));
			
			loc.setType(jObject.getString("Type"));
			
			loc.setIdCompany(jObject.getInt("IDCompany"));
			
			loc.setOfferId(jObject.getInt("OfferID"));
			loc.setOfferName(jObject.getString("OfferName"));
			loc.setOfferLogo(jObject.getString("OfferLogo"));
			loc.setOfferLogoMini(jObject.getString("OfferLogoMini"));
			loc.setOfferURL(jObject.getString("OfferURL"));
			loc.setOfferText(jObject.getString("OfferText"));
			
			loc.setUrlId(jObject.getInt("URLID"));
			loc.setUrlName(jObject.getString("URLName"));
			loc.setUrlLogo(jObject.getString("URLLogo"));
			loc.setUrlLogoMini(jObject.getString("URLLogoMini"));
			loc.setUrlValue(jObject.getString("URLValue"));
			loc.setUrlText(jObject.getString("URLText"));
			
			loc.setAppId(jObject.getInt("AppID"));
			loc.setAppName(jObject.getString("AppName"));
			loc.setAppLogo(jObject.getString("AppLogo"));
			loc.setAppLogoMini(jObject.getString("AppLogoMini"));
			loc.setAppUrl(jObject.getString("AppURL"));
			loc.setAppDescription(jObject.getString("AppDescription"));
			loc.setAppType(jObject.getString("AppType"));
			
			loc.setUserAlarmId(jObject.getInt("UserAlarmID"));
			loc.setUserAlarmName(jObject.getString("UserAlarmName"));
			
			loc.setUserUrlId(jObject.getInt("UserURLID"));
			loc.setUserUrlTags(jObject.getString("UserURLTags"));
			loc.setUserUrlComment(jObject.getString("UserURLComment"));
			loc.setUserUrlPhoto(jObject.getString("UserURLPhoto"));
			loc.setUserUrlValue(jObject.getString("UserURLValue"));
			loc.setUserUrlDescription(jObject.getString("UserURLDescription"));
			
			loc.setIdUser(jObject.getInt("IDUser"));
			
			loc.setRadius(jObject.getDouble("Radius"));
			
			loc.setPhone(jObject.getString("Phone"));
			loc.setWifi(jObject.getString("Wifi"));
			loc.setBluetooth(jObject.getString("Bluetooth"));
			loc.setLocation(jObject.getString("Location"));
			
			loc.setAppPrice((float)jObject.getDouble("AppPrice"));
			
			loc.setAuxPlus(jObject.getInt("AuxPlus"));
			loc.setAuxMinus(jObject.getInt("AuxMinus"));
			
			loc.setAuxFavourite(jObject.getBoolean("AuxFavourite"));
			
			loc.setAuxPin(jObject.getBoolean("AuxPin"));
			
			loc.setAuxRate((float) jObject.getDouble("AuxRate"));
			loc.setAuxComment(jObject.getString("AuxComment"));
			
			loc.setAuxTotalRate((float)jObject.getDouble("AuxTotalRate"));
			
			loc.setAuxTotalPins(jObject.getInt("AuxTotalPins"));
			loc.setAuxTotalComments(jObject.getInt("AuxTotalComments"));
			
			
		} catch (Exception e) {
			loc = null;
			e.printStackTrace();
		}
		
		return loc;
	}

	@Override
	public ArrayList<Localization> SimpleParseLocalizations(String input) {

		String jString = input;
		
		ArrayList<Localization> ret = new ArrayList<Localization>();
		
		try {
			jArray = new JSONArray(jString);
			
			for(int i = 0; i < jArray.length(); i++)
			{
				Localization loc = new Localization();
				loc.setId(jArray.getJSONObject(i).getInt("IDLocalization"));
				loc.setName(jArray.getJSONObject(i).getString("Name"));
				loc.setLatitude(jArray.getJSONObject(i).getDouble("Latitude"));
				loc.setLongitude(jArray.getJSONObject(i).getDouble("Longitude"));
				loc.setAddress(jArray.getJSONObject(i).getString("Address"));
				loc.setZipCode(jArray.getJSONObject(i).getString("ZipCode"));
				loc.setState(jArray.getJSONObject(i).getString("State"));
				loc.setCity(jArray.getJSONObject(i).getString("City"));
				loc.setCountry(jArray.getJSONObject(i).getString("Country"));
				
				loc.setType(jArray.getJSONObject(i).getString("Type"));
				
				loc.setIdCompany(jArray.getJSONObject(i).getInt("IDCompany"));
				
				loc.setOfferId(jArray.getJSONObject(i).getInt("OfferID"));
				loc.setOfferName(jArray.getJSONObject(i).getString("OfferName"));
				loc.setOfferLogo(jArray.getJSONObject(i).getString("OfferLogo"));
				loc.setOfferLogoMini(jArray.getJSONObject(i).getString("OfferLogoMini"));
				loc.setOfferURL(jArray.getJSONObject(i).getString("OfferURL"));
				loc.setOfferText(jArray.getJSONObject(i).getString("OfferText"));
				
				loc.setUrlId(jArray.getJSONObject(i).getInt("URLID"));
				loc.setUrlName(jArray.getJSONObject(i).getString("URLName"));
				loc.setUrlLogo(jArray.getJSONObject(i).getString("URLLogo"));
				loc.setUrlLogoMini(jArray.getJSONObject(i).getString("URLLogoMini"));
				loc.setUrlValue(jArray.getJSONObject(i).getString("URLValue"));
				loc.setUrlText(jArray.getJSONObject(i).getString("URLText"));
				
				loc.setAppId(jArray.getJSONObject(i).getInt("AppID"));
				loc.setAppName(jArray.getJSONObject(i).getString("AppName"));
				loc.setAppLogo(jArray.getJSONObject(i).getString("AppLogo"));
				loc.setAppLogoMini(jArray.getJSONObject(i).getString("AppLogoMini"));
				loc.setAppUrl(jArray.getJSONObject(i).getString("AppURL"));
				loc.setAppDescription(jArray.getJSONObject(i).getString("AppDescription"));
				loc.setAppType(jArray.getJSONObject(i).getString("AppType"));
				
				loc.setUserAlarmId(jArray.getJSONObject(i).getInt("UserAlarmID"));
				loc.setUserAlarmName(jArray.getJSONObject(i).getString("UserAlarmName"));
				
				loc.setUserUrlId(jArray.getJSONObject(i).getInt("UserURLID"));
				loc.setUserUrlTags(jArray.getJSONObject(i).getString("UserURLTags"));
				loc.setUserUrlComment(jArray.getJSONObject(i).getString("UserURLComment"));
				loc.setUserUrlPhoto(jArray.getJSONObject(i).getString("UserURLPhoto"));
				loc.setUserUrlValue(jArray.getJSONObject(i).getString("UserURLValue"));
				loc.setUserUrlDescription(jArray.getJSONObject(i).getString("UserURLDescription"));
				
				loc.setIdUser(jArray.getJSONObject(i).getInt("IDUser"));
				
				loc.setRadius(jArray.getJSONObject(i).getDouble("Radius"));
				
				loc.setPhone(jArray.getJSONObject(i).getString("Phone"));
				loc.setWifi(jArray.getJSONObject(i).getString("Wifi"));
				loc.setBluetooth(jArray.getJSONObject(i).getString("Bluetooth"));
				loc.setLocation(jArray.getJSONObject(i).getString("Location"));
				
				loc.setAppPrice((float)jArray.getJSONObject(i).getDouble("AppPrice"));
				
				ret.add(loc);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}

}

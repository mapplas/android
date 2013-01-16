package com.mapplas.model;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.mapplas.model.database.inserters.NotificationInserter;
import com.mapplas.utils.LocationCurrency;

public class JsonParser {

	private JSONArray jArray;

	private JSONObject jObject;

	private Context context;

	public JsonParser(Context context) {
		this.context = context;
	}

	public void parseApps(String input, SuperModel model, boolean append) {
		model.resetError();

		if(!append) {
			model.resetModel();
		}

		try {
			this.jArray = new JSONArray(input);

			long currentTimestamp = System.currentTimeMillis();
			NotificationInserter notifInserter = new NotificationInserter(this.context);
			JSONObject currentJson = new JSONObject();
			
			for(int i = 0; i < this.jArray.length(); i++) {
				App loc = new App();
				currentJson = this.jArray.getJSONObject(i);
				
				loc.setId(currentJson.getInt("IDLocalization"));
				loc.setName(currentJson.getString("Name"));
				loc.setLatitude(currentJson.getDouble("Latitude"));
				loc.setLongitude(currentJson.getDouble("Longitude"));

				loc.setType(currentJson.getString("Type"));

				loc.setAppName(currentJson.getString("AppName"));
				loc.setAppLogo(currentJson.getString("AppLogo"));
				loc.setAppLogoMini(currentJson.getString("AppLogoMini"));
				loc.setAppUrl(currentJson.getString("AppURL"));
				loc.setAppDescription(currentJson.getString("AppDescription"));

				loc.setPhone(currentJson.getString("Phone"));

				loc.setAppPrice((float)currentJson.getDouble("AppPrice"));

				loc.setAuxFavourite(currentJson.getBoolean("AuxFavourite"));

				loc.setAuxPin(currentJson.getBoolean("AuxPin"));

				loc.setAuxRate((float)currentJson.getDouble("AuxRate"));
				loc.setAuxComment(currentJson.getString("AuxComment"));

				loc.setAuxTotalRate((float)currentJson.getDouble("AuxTotalRate"));
				loc.setAuxTotalPins(currentJson.getInt("AuxTotalPins"));
				loc.setAuxTotalComments(currentJson.getInt("AuxTotalComments"));

				// Parse notifications
				notifInserter.insert(this.jArray, i, loc, model, currentTimestamp);

				// Parse comments
				JSONArray auxArray = currentJson.getJSONArray("AuxComments");
				JSONObject currentComment = new JSONObject();
				for(int j = 0; j < auxArray.length(); j++) {
					Comment com = new Comment();
					currentComment = auxArray.getJSONObject(j);
					
					com.setId(currentComment.getInt("IDComment"));
					com.setIdLocalization(currentComment.getInt("IDLocalization"));
					com.setComment(URLDecoder.decode(currentComment.getString("Comment")));
					com.setDate(currentComment.getString("Date"));
					com.setHour(currentComment.getString("Hour"));
					com.setRate((float)currentComment.getDouble("Rate"));
					com.setIdUser(currentComment.getInt("IDUser"));

					// com.setAuxLocalization(loc);

					loc.getAuxComments().add(com);
				}

				auxArray = currentJson.getJSONArray("AuxPhotos");
				JSONObject currentPhoto = new JSONObject();
				for(int j = 0; j < auxArray.length(); j++) {
					Photo pho = new Photo();
					currentPhoto = auxArray.getJSONObject(j);
					
					pho.setId(currentPhoto.getInt("IDPhoto"));
					pho.setIdLocalization(currentPhoto.getInt("IDLocalization"));
					pho.setComment(currentPhoto.getString("Comment"));
					pho.setDate(currentPhoto.getString("Date"));
					pho.setHour(currentPhoto.getString("Hour"));
					pho.setPhoto(currentPhoto.getString("Photo"));
					pho.setIdUser(currentPhoto.getInt("IDUser"));

					// pho.setAuxLocalization(loc);

					loc.getAuxPhotos().add(pho);
				}

				String location = currentJson.getString("Market");
				if(location.equals("Usa")) {
					loc.setLocationCurrency(LocationCurrency.DOLAR);
				}
				else {
					loc.setLocationCurrency(LocationCurrency.EURO);
				}

				model.appList().add(loc);
			}
			
			model.appList().sort();
			notifInserter.flush();

		} catch (Exception e) {
			model.setOperationError(true);
			model.setErrorText(e.getMessage());
			e.printStackTrace();
		}

	}

	public User ParseUser(String input) {
		String jString = input;

		try {
			this.jObject = new JSONObject(jString);

			User usr = new User();

			usr.setId(this.jObject.getInt("IDUser"));
			usr.setName(this.jObject.getString("Name"));
			usr.setLastname(this.jObject.getString("Lastname"));
			usr.setGender(this.jObject.getString("Gender"));
			usr.setBirthdate(this.jObject.getString("Birthdate"));
			usr.setLogin(this.jObject.getString("Login"));
			usr.setPassword(this.jObject.getString("Password"));
			usr.setEmail(this.jObject.getString("Email"));
			usr.setImei(this.jObject.getString("Imei"));

			return usr;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// public App ParseLocalization(String input) {
	// App loc = new App();
	// String jString = input;
	//
	// try {
	// jObject = new JSONObject(jString);
	//
	// loc.setId(jObject.getInt("IDLocalization"));
	// loc.setName(jObject.getString("Name"));
	// loc.setLatitude(jObject.getDouble("Latitude"));
	// loc.setLongitude(jObject.getDouble("Longitude"));
	// loc.setAddress(jObject.getString("Address"));
	// loc.setZipCode(jObject.getString("ZipCode"));
	// loc.setState(jObject.getString("State"));
	// loc.setCity(jObject.getString("City"));
	// loc.setCountry(jObject.getString("Country"));
	//
	// loc.setType(jObject.getString("Type"));
	//
	// loc.setIdCompany(jObject.getInt("IDCompany"));
	//
	// loc.setOfferId(jObject.getInt("OfferID"));
	// loc.setOfferName(jObject.getString("OfferName"));
	// loc.setOfferLogo(jObject.getString("OfferLogo"));
	// loc.setOfferLogoMini(jObject.getString("OfferLogoMini"));
	// loc.setOfferURL(jObject.getString("OfferURL"));
	// loc.setOfferText(jObject.getString("OfferText"));
	//
	// loc.setUrlId(jObject.getInt("URLID"));
	// loc.setUrlName(jObject.getString("URLName"));
	// loc.setUrlLogo(jObject.getString("URLLogo"));
	// loc.setUrlLogoMini(jObject.getString("URLLogoMini"));
	// loc.setUrlValue(jObject.getString("URLValue"));
	// loc.setUrlText(jObject.getString("URLText"));
	//
	// loc.setAppId(jObject.getInt("AppID"));
	// loc.setAppName(jObject.getString("AppName"));
	// loc.setAppLogo(jObject.getString("AppLogo"));
	// loc.setAppLogoMini(jObject.getString("AppLogoMini"));
	// loc.setAppUrl(jObject.getString("AppURL"));
	// loc.setAppDescription(jObject.getString("AppDescription"));
	// loc.setAppType(jObject.getString("AppType"));
	//
	// loc.setUserAlarmId(jObject.getInt("UserAlarmID"));
	// loc.setUserAlarmName(jObject.getString("UserAlarmName"));
	//
	// loc.setUserUrlId(jObject.getInt("UserURLID"));
	// loc.setUserUrlTags(jObject.getString("UserURLTags"));
	// loc.setUserUrlComment(jObject.getString("UserURLComment"));
	// loc.setUserUrlPhoto(jObject.getString("UserURLPhoto"));
	// loc.setUserUrlValue(jObject.getString("UserURLValue"));
	// loc.setUserUrlDescription(jObject.getString("UserURLDescription"));
	//
	// loc.setIdUser(jObject.getInt("IDUser"));
	//
	// loc.setRadius(jObject.getDouble("Radius"));
	//
	// loc.setPhone(jObject.getString("Phone"));
	// loc.setWifi(jObject.getString("Wifi"));
	// loc.setBluetooth(jObject.getString("Bluetooth"));
	// loc.setLocation(jObject.getString("Location"));
	//
	// loc.setAppPrice((float)jObject.getDouble("AppPrice"));
	//
	// loc.setAuxPlus(jObject.getInt("AuxPlus"));
	// loc.setAuxMinus(jObject.getInt("AuxMinus"));
	//
	// loc.setAuxFavourite(jObject.getBoolean("AuxFavourite"));
	//
	// loc.setAuxPin(jObject.getBoolean("AuxPin"));
	//
	// loc.setAuxRate((float)jObject.getDouble("AuxRate"));
	// loc.setAuxComment(jObject.getString("AuxComment"));
	//
	// loc.setAuxTotalRate((float)jObject.getDouble("AuxTotalRate"));
	//
	// loc.setAuxTotalPins(jObject.getInt("AuxTotalPins"));
	// loc.setAuxTotalComments(jObject.getInt("AuxTotalComments"));
	//
	// } catch (Exception e) {
	// loc = null;
	// e.printStackTrace();
	// }
	//
	// return loc;
	// }

	public ArrayList<App> parseApps(String input) {

		String jString = input;

		ArrayList<App> ret = new ArrayList<App>();

		try {
			jArray = new JSONArray(jString);

			for(int i = 0; i < jArray.length(); i++) {
				App loc = new App();
				loc.setId(jArray.getJSONObject(i).getInt("IDLocalization"));
				loc.setName(jArray.getJSONObject(i).getString("Name"));
				loc.setLatitude(jArray.getJSONObject(i).getDouble("Latitude"));
				loc.setLongitude(jArray.getJSONObject(i).getDouble("Longitude"));

				loc.setType(jArray.getJSONObject(i).getString("Type"));

				loc.setAppName(jArray.getJSONObject(i).getString("AppName"));
				loc.setAppLogo(jArray.getJSONObject(i).getString("AppLogo"));
				loc.setAppLogoMini(jArray.getJSONObject(i).getString("AppLogoMini"));
				loc.setAppUrl(jArray.getJSONObject(i).getString("AppURL"));
				loc.setAppDescription(jArray.getJSONObject(i).getString("AppDescription"));

				loc.setPhone(jArray.getJSONObject(i).getString("Phone"));

				loc.setAppPrice((float)jArray.getJSONObject(i).getDouble("AppPrice"));

				this.setPinnedAppLocation(jArray.getJSONObject(i), loc);

				ret.add(loc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	private void setPinnedAppLocation(JSONObject jsonObject, App loc) throws JSONException {
		if(jsonObject.has("la") && jsonObject.has("lo")) {
			String auxLatitude = jsonObject.getString("la");
			loc.setPinnedLatitude(Float.parseFloat(auxLatitude));
			String auxLongitude = jsonObject.getString("lo");
			loc.setPinnedLongitude(Float.parseFloat(auxLongitude));

			Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());
			String addresText = "";

			List<Address> addresses = null;
			try {
				// Call the synchronous getFromLocation() method by passing in
				// the lat/long values.
				addresses = geocoder.getFromLocation(loc.getPinnedLatitude(), loc.getPinnedLongitude(), 1);
			} catch (IOException e) {
				try {
					// Call the synchronous getFromLocation() method by passing
					// in the lat/long values.
					addresses = geocoder.getFromLocation(loc.getPinnedLatitude(), loc.getPinnedLongitude(), 1);
				} catch (IOException e2) {
					e.printStackTrace();
				}
			}
			if(addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				// Format the first line of address (if available), city, and
				// country name.
				addresText = String.format("%s, %s.", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality());
			}

			loc.setPinnedGeocodedLocation(addresText);
		}
	}

}

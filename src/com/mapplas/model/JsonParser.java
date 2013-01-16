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
		String jString = input;

		if(!append) {
			model.resetModel();
		}

		try {
			jArray = new JSONArray(jString);

			long currentTimestamp = System.currentTimeMillis();
			NotificationInserter notifInserter = new NotificationInserter(this.context);

			for(int i = 0; i < jArray.length(); i++) {
				App loc = new App();
				JSONObject currentJson = jArray.getJSONObject(i);
				
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
				notifInserter.insert(jArray, i, loc, model, currentTimestamp);

				// Parse comments
				JSONArray auxArray = currentJson.getJSONArray("AuxComments");
				for(int j = 0; j < auxArray.length(); j++) {
					Comment com = new Comment();
					com.setId(auxArray.getJSONObject(j).getInt("IDComment"));
					com.setIdLocalization(auxArray.getJSONObject(j).getInt("IDLocalization"));
					com.setComment(URLDecoder.decode(auxArray.getJSONObject(j).getString("Comment")));
					com.setDate(auxArray.getJSONObject(j).getString("Date"));
					com.setHour(auxArray.getJSONObject(j).getString("Hour"));
					com.setRate((float)auxArray.getJSONObject(j).getDouble("Rate"));
					com.setIdUser(auxArray.getJSONObject(j).getInt("IDUser"));

					// com.setAuxLocalization(loc);

					loc.getAuxComments().add(com);
				}

				auxArray = currentJson.getJSONArray("AuxPhotos");
				for(int j = 0; j < auxArray.length(); j++) {
					Photo pho = new Photo();
					pho.setId(auxArray.getJSONObject(j).getInt("IDPhoto"));
					pho.setIdLocalization(auxArray.getJSONObject(j).getInt("IDLocalization"));
					pho.setComment(auxArray.getJSONObject(j).getString("Comment"));
					pho.setDate(auxArray.getJSONObject(j).getString("Date"));
					pho.setHour(auxArray.getJSONObject(j).getString("Hour"));
					pho.setPhoto(auxArray.getJSONObject(j).getString("Photo"));
					pho.setIdUser(auxArray.getJSONObject(j).getInt("IDUser"));

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
				model.appList().sort();
			}

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
				// loc.setAddress(jArray.getJSONObject(i).getString("Address"));
				// loc.setZipCode(jArray.getJSONObject(i).getString("ZipCode"));
				// loc.setState(jArray.getJSONObject(i).getString("State"));
				// loc.setCity(jArray.getJSONObject(i).getString("City"));
				// loc.setCountry(jArray.getJSONObject(i).getString("Country"));

				loc.setType(jArray.getJSONObject(i).getString("Type"));

				// loc.setIdCompany(jArray.getJSONObject(i).getInt("IDCompany"));
				//
				// loc.setOfferId(jArray.getJSONObject(i).getInt("OfferID"));
				// loc.setOfferName(jArray.getJSONObject(i).getString("OfferName"));
				// loc.setOfferLogo(jArray.getJSONObject(i).getString("OfferLogo"));
				// loc.setOfferLogoMini(jArray.getJSONObject(i).getString("OfferLogoMini"));
				// loc.setOfferURL(jArray.getJSONObject(i).getString("OfferURL"));
				// loc.setOfferText(jArray.getJSONObject(i).getString("OfferText"));

				// loc.setUrlId(jArray.getJSONObject(i).getInt("URLID"));
				// loc.setUrlName(jArray.getJSONObject(i).getString("URLName"));
				// loc.setUrlLogo(jArray.getJSONObject(i).getString("URLLogo"));
				// loc.setUrlLogoMini(jArray.getJSONObject(i).getString("URLLogoMini"));
				// loc.setUrlValue(jArray.getJSONObject(i).getString("URLValue"));
				// loc.setUrlText(jArray.getJSONObject(i).getString("URLText"));

				// loc.setAppId(jArray.getJSONObject(i).getInt("AppID"));
				loc.setAppName(jArray.getJSONObject(i).getString("AppName"));
				loc.setAppLogo(jArray.getJSONObject(i).getString("AppLogo"));
				loc.setAppLogoMini(jArray.getJSONObject(i).getString("AppLogoMini"));
				loc.setAppUrl(jArray.getJSONObject(i).getString("AppURL"));
				loc.setAppDescription(jArray.getJSONObject(i).getString("AppDescription"));
				// loc.setAppType(jArray.getJSONObject(i).getString("AppType"));

				// loc.setUserAlarmId(jArray.getJSONObject(i).getInt("UserAlarmID"));
				// loc.setUserAlarmName(jArray.getJSONObject(i).getString("UserAlarmName"));
				//
				// loc.setUserUrlId(jArray.getJSONObject(i).getInt("UserURLID"));
				// loc.setUserUrlTags(jArray.getJSONObject(i).getString("UserURLTags"));
				// loc.setUserUrlComment(jArray.getJSONObject(i).getString("UserURLComment"));
				// loc.setUserUrlPhoto(jArray.getJSONObject(i).getString("UserURLPhoto"));
				// loc.setUserUrlValue(jArray.getJSONObject(i).getString("UserURLValue"));
				// loc.setUserUrlDescription(jArray.getJSONObject(i).getString("UserURLDescription"));
				//
				// loc.setIdUser(jArray.getJSONObject(i).getInt("IDUser"));

				// loc.setRadius(jArray.getJSONObject(i).getDouble("Radius"));

				loc.setPhone(jArray.getJSONObject(i).getString("Phone"));
				// loc.setWifi(jArray.getJSONObject(i).getString("Wifi"));
				// loc.setBluetooth(jArray.getJSONObject(i).getString("Bluetooth"));
				// loc.setLocation(jArray.getJSONObject(i).getString("Location"));

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

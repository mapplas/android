package com.mapplas.utils.network.async_tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.model.SuperModel;

public class ReverseGeocodingTask extends android.os.AsyncTask<Location, Void, String> {

	private Context context;

	private SuperModel model;

	private TextView listViewHeaderStatusMessage;

	public ReverseGeocodingTask(Context context, SuperModel model, TextView listViewHeaderStatusMessage) {
		super();
		this.context = context;
		this.model = model;
		this.listViewHeaderStatusMessage = listViewHeaderStatusMessage;
	}

	@Override
	protected String doInBackground(Location... params) {
		Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());
		String addresText = this.context.getString(R.string.descriptive_geoloc_error);

		Location loc = params[0];
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();
		List<Address> addresses = null;
		try {
			// Call the synchronous getFromLocation() method by passing in
			// the lat/long values.
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			try {
				// Call the synchronous getFromLocation() method by passing
				// in the lat/long values.
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e2) {
				e.printStackTrace();
				return getAdressFromGoogleWebApi(String.valueOf(latitude), String.valueOf(longitude));
			}
		}
		if(addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);

			String firstAddressLine = "";
			if(address.getMaxAddressLineIndex() > 0) {
				firstAddressLine = address.getAddressLine(0);
			}

			String locality = address.getLocality();
			if(locality == null || firstAddressLine.contains(locality)) {
				addresText = String.format("%s.", firstAddressLine);
			}
			else {
				addresText = String.format("%s, %s.", firstAddressLine, locality);
			}

			if(addresText.equals("")) {
				addresText = this.context.getString(R.string.descriptive_geoloc_error);
			}
		}
		else {
			addresText = this.context.getString(R.string.descriptive_geoloc_error);
		}

		return addresText;
	}

	@Override
	protected void onPostExecute(String addresText) {
		super.onPostExecute(addresText);

		this.model.setCurrentDescriptiveGeoLoc(addresText);

		if(listViewHeaderStatusMessage != null) {
			listViewHeaderStatusMessage.setText(addresText);
		}
	}

	private String getAdressFromGoogleWebApi(String latitude, String longitude) {
		String googleUrl = "http://maps.googleapis.com/maps/api/geocode/json?";

		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(googleUrl);

		sbuilder.append("latlng=" + latitude + "," + longitude);
		sbuilder.append("&sensor=true");
		String url = sbuilder.toString();
		
		String address = latitude + ", " + longitude;
		String response = "";
		
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse httpresponse = httpclient.execute(httppost);
			HttpEntity httpentity = httpresponse.getEntity();
			InputStream is = httpentity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			is.close();
			response = sb.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
//		Parse result
		try {
			JSONObject json = new JSONObject(response);
			JSONArray results = json.getJSONArray("results");
			JSONObject jsone = (JSONObject)results.get(0);
			address = StringEscapeUtils.unescapeHtml4(jsone.getString("formatted_address"));
		}
		catch (Exception e) {
			return address;
		}
		return address;
	}

}

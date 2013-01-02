package com.mapplas.app.async_tasks;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;

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
		String addresText = "";

		Location loc = params[0];
		List<Address> addresses = null;
		try {
			// Call the synchronous getFromLocation() method by passing in
			// the lat/long values.
			addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
		} catch (IOException e) {
			try {
				// Call the synchronous getFromLocation() method by passing
				// in the lat/long values.
				addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
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
}

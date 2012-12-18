package com.mapplas.app.async_tasks;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import app.mapplas.com.R;

import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;

public class ReverseGeocodingTask extends android.os.AsyncTask<Location, Void, Void> {

	private Context context;

	private SuperModel model;

	private Handler messageHandler;

	public ReverseGeocodingTask(Context context, SuperModel model, Handler handler) {
		super();
		this.context = context;
		this.model = model;
		this.messageHandler = handler;
	}

	@Override
	protected Void doInBackground(Location... params) {
		Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());

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
				// Update UI field with the exception.
				Message.obtain(this.messageHandler, Constants.SYNESTH_MAIN_STATUS_ID, this.context.getString(R.string.unable_geoloc)).sendToTarget();

				this.model.currentDescriptiveGeoLoc = "";
			}
		}
		if(addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);
			// Format the first line of address (if available), city, and
			// country name.
			String addressText = String.format("%s, %s.", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getLocality());

			this.model.currentDescriptiveGeoLoc = addressText;

			// Update the UI via a message handler.
			Message.obtain(this.messageHandler, Constants.SYNESTH_MAIN_STATUS_ID, addressText).sendToTarget();
		}
		return null;
	}
}

package com.mapplas.utils.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.AwesomeListView;
import com.mapplas.app.async_tasks.AppGetterTask;
import com.mapplas.app.async_tasks.ReverseGeocodingTask;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.NetworkConnectionChecker;

public class AroundRequester implements UserLocationListener {

	public static final int LOCATION_TIMEOUT_IN_MILLISECONDS = 5000;

	private final UserLocationRequester userLocationRequester;

	private Context context;

	private TextView listViewHeaderStatusMessage;

	private ImageView listViewHeaderImage;

	private SuperModel model;

	private Handler messageHandler;
	
	private AwesomeListView listView;

	public AroundRequester(UserLocationRequesterFactory userLocationRequesterFactory, LocationManager locationManager, int timeOut, Context context, TextView listViewHeaderStatusMessage, ImageView listViewHeaderImage, SuperModel model, Handler messageHandler, AwesomeListView listView) {
		this.context = context;
		this.listViewHeaderStatusMessage = listViewHeaderStatusMessage;
		this.listViewHeaderImage = listViewHeaderImage;
		this.model = model;
		this.messageHandler = messageHandler;
		this.listView = listView;
		this.userLocationRequester = userLocationRequesterFactory.create(locationManager, this, timeOut);
	}

	public void start() {
		this.userLocationRequester.resetLocationBoolean();
		this.userLocationRequester.startListeningForLocationUpdates();
	}

	public void stop() {
		this.userLocationRequester.stopListeningForLocationUpdates();
	}

	@Override
	public void locationSearchEnded(Location location) {
		// locationManager.removeUpdates(locationListener);
		this.loadTasks(location);
	}

	@Override
	public void locationSearchDidTimeout(Location location) {
		if(location == null) {
			Toast toast = Toast.makeText(this.context, "TIMEOUT TOAST", Toast.LENGTH_LONG);
			toast.show();
		}
		else {
			this.loadTasks(location);
		}
	}

	private void loadTasks(Location location) {
		this.listViewHeaderStatusMessage.setText(R.string.location_done);
		this.listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

		this.model.setCurrentLocation(location.getLatitude() + "," + location.getLongitude());

		NetworkConnectionChecker networkChecker = new NetworkConnectionChecker();
		if(!networkChecker.isWifiConnected(this.context) && !networkChecker.isNetworkConnectionConnected(this.context)) {
			Toast.makeText(this.context, R.string.connection_error_toast, Toast.LENGTH_LONG).show();
			this.listView.finishRefresing();
		}
		else {
			try {
				this.listViewHeaderStatusMessage.setText(R.string.location_searching);
				this.listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

				(new AppGetterTask(this.context, model, messageHandler)).execute(new Location[] { location });
				(new ReverseGeocodingTask(this.context, model, messageHandler)).execute(new Location[] { location });

			} catch (Exception e) {
				Log.i(getClass().getSimpleName(), e.toString());
			}
		}
	}
}

package com.mapplas.utils.location.location_manager;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.location.LocationManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.location.UserLocationListener;
import com.mapplas.utils.network.async_tasks.AppGetterTask;
import com.mapplas.utils.network.async_tasks.ReverseGeocodingTask;
import com.mapplas.utils.searcher.SearchManager;
import com.mapplas.utils.visual.helpers.AppGetterTaskViewsContainer;

public class AroundRequesterLocationManager implements UserLocationListener {

	private final LocationRequesterLocationManager userLocationRequester;

	private Context context;

	private TextView listViewHeaderStatusMessage;

	private ImageView listViewHeaderImage;

	private SuperModel model;

	private ArrayList<ApplicationInfo> appsInstalledList;

	private MapplasActivity mainActivity;

	private AppGetterTaskViewsContainer container;

	public AroundRequesterLocationManager(LocationRequesterLocationManagerFactory userLocationRequesterFactory, LocationManager locationManager, Context context, TextView listViewHeaderStatusMessage, ImageView listViewHeaderImage, SuperModel model, ArrayList<ApplicationInfo> appsInstalledList, MapplasActivity mainActivity, AppGetterTaskViewsContainer container) {
		this.context = context;
		this.listViewHeaderStatusMessage = listViewHeaderStatusMessage;
		this.listViewHeaderImage = listViewHeaderImage;
		this.model = model;
		this.userLocationRequester = userLocationRequesterFactory.create(locationManager, this);
		this.appsInstalledList = appsInstalledList;
		this.mainActivity = mainActivity;
		this.container = container;
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
		this.loadTasks(location, true);
	}

	@Override
	public void locationSearchDidTimeout(Location location) {
		if(location == null) {
			Toast toast = Toast.makeText(this.context, R.string.location_error_toast, Toast.LENGTH_LONG);
			toast.show();
			((MapplasActivity)this.context).finish();
		}
		else {
			this.loadTasks(location, true);
		}
	}

	private void loadTasks(Location location, boolean reset_pagination) {

		// TODO: uncomment for emulator or mocked location use
		// location.setLatitude(40.492523);
		// location.setLongitude(-3.59589);

		SearchManager.APP_REQUEST_TYPE_BEING_DONE = Constants.APP_REQUEST_TYPE_LOCATION;

		this.listViewHeaderStatusMessage.setText(R.string.location_done);
		this.listViewHeaderImage.setBackgroundResource(R.drawable.ic_map);

		this.model.setCurrentLocation(location.getLatitude() + "," + location.getLongitude());
		this.model.appList().setCurrentLocation(location.getLatitude() + "," + location.getLongitude());
		this.model.setLocation(location);

		// App request
		this.listViewHeaderStatusMessage.setText(R.string.location_searching);
		this.listViewHeaderImage.setBackgroundResource(R.drawable.ic_map);

		this.model.initializeForNewAppRequest();

		int requestNumber = 0;
		new AppGetterTask(this.context, this.model, this.appsInstalledList, this.mainActivity, requestNumber, Constants.APP_REQUEST_TYPE_LOCATION, this.container).execute(new Location(location), reset_pagination, -1);
		new ReverseGeocodingTask(this.context, this.model, this.listViewHeaderStatusMessage).execute(new Location(location));
	}
}

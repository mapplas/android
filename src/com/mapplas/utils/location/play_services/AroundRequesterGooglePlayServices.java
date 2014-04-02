package com.mapplas.utils.location.play_services;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.location.Location;
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

public class AroundRequesterGooglePlayServices implements UserLocationListener {

	private LocationRequesterGooglePlayServices locationRequester;

	private Context context;

	private TextView listViewHeaderStatusMessage;

	private ImageView listViewHeaderImage;

	private SuperModel model;

	private ArrayList<ApplicationInfo> appsInstalledList;

	private MapplasActivity mainActivity;

	private AppGetterTaskViewsContainer container;

	public AroundRequesterGooglePlayServices(Context context, TextView listViewHeaderStatusMessage, ImageView listViewHeaderImage, SuperModel model, ArrayList<ApplicationInfo> appsInstalledList, MapplasActivity mainActivity, AppGetterTaskViewsContainer container) {
		this.context = context;
		this.listViewHeaderStatusMessage = listViewHeaderStatusMessage;
		this.listViewHeaderImage = listViewHeaderImage;
		this.model = model;
		this.appsInstalledList = appsInstalledList;
		this.mainActivity = mainActivity;
		this.container = container;

		this.locationRequester = new LocationRequesterGooglePlayServices(this.context, this.mainActivity, this);
	}

	public void start() {
		this.locationRequester.start();
	}

	@Override
	public void locationSearchEnded(Location location) {
		if(location == null) {
			this.showErrorToastAndQuit(R.string.location_error_toast);
		}
		else {
			this.loadTasks(location, true);
		}
		this.locationRequester.stop();
	}

	@Override
	public void locationSearchDidTimeout(Location location) {
		if(location == null) {
			this.showErrorToastAndQuit(R.string.location_error_toast);
		}
		else {
			this.loadTasks(location, true);
		}
	}

	private void showErrorToastAndQuit(int toastMessage) {
		Toast.makeText(this.context, toastMessage, Toast.LENGTH_LONG).show();
		((MapplasActivity)this.context).finish();
	}

	private void loadTasks(Location location, boolean reset_pagination) {

		// TODO: uncomment for emulator or mocked location use
//		NY
//		location.setLatitude(40.7154);
//		location.setLongitude(-74.0046);
//		London
//		location.setLatitude(51.5053);
//		location.setLongitude(-0.1431);
//		Rome
//		location.setLatitude(41.8890);
//		location.setLongitude(12.4867);
//		Miami
//		location.setLatitude(25.7828);
//		location.setLongitude(-80.2099);
//		San Francisco
//		location.setLatitude(37.756007);
//		location.setLongitude(-122.454499);

		
		
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

package com.mapplas.utils.location;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.AwesomeListView;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.async_tasks.AppGetterTask;
import com.mapplas.app.async_tasks.ReverseGeocodingTask;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.NetworkConnectionChecker;

public class AroundRequester implements UserLocationListener {

	public static final int LOCATION_TIMEOUT_IN_MILLISECONDS = 9000;

	private final UserLocationRequester userLocationRequester;

	private Context context;

	private TextView listViewHeaderStatusMessage;

	private ImageView listViewHeaderImage;

	private SuperModel model;

	private AwesomeListView listView;

	private AppAdapter appAdapter;

	private List<ApplicationInfo> applicationList;

	private ActivityManager activityManager;

	private Button notificationsButton;

	public AroundRequester(UserLocationRequesterFactory userLocationRequesterFactory, LocationManager locationManager, int timeOut, Context context, TextView listViewHeaderStatusMessage, ImageView listViewHeaderImage, SuperModel model, AwesomeListView listView, AppAdapter appAdapter, List<ApplicationInfo> applicationList, ActivityManager activityManager, Button notificationsButton) {
		this.context = context;
		this.listViewHeaderStatusMessage = listViewHeaderStatusMessage;
		this.listViewHeaderImage = listViewHeaderImage;
		this.model = model;
		this.listView = listView;
		this.userLocationRequester = userLocationRequesterFactory.create(locationManager, this, timeOut);
		this.appAdapter = appAdapter;
		this.applicationList = applicationList;
		this.activityManager = activityManager;
		this.notificationsButton = notificationsButton;
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
		this.loadTasks(location);
	}

	@Override
	public void locationSearchDidTimeout(Location location) {
		if(location == null) {
			Toast toast = Toast.makeText(this.context, R.string.location_error_toast, Toast.LENGTH_LONG);
			toast.show();
			((MapplasActivity)this.context).finish();
		}
		else {
			this.loadTasks(location);
		}
	}

	private void loadTasks(Location location) {
		this.listViewHeaderStatusMessage.setText(R.string.location_done);
		this.listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

		this.model.setCurrentLocation(location.getLatitude() + "," + location.getLongitude());
		this.model.appList().setCurrentLocation(location.getLatitude() + "," + location.getLongitude());

		ConnectivityManager connManager = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		NetworkConnectionChecker networkChecker = new NetworkConnectionChecker();
		if(!networkChecker.isWifiConnected(this.context) && !networkChecker.isNetworkConnectionConnected(this.context)) {
			Toast.makeText(this.context, R.string.location_error_toast, Toast.LENGTH_LONG).show();
			this.listView.finishRefresing();

			this.listViewHeaderStatusMessage.setText(R.string.location_needed);
		}
		else if(mWifi.isConnected() || mMobile.isConnected()) {
			try {
				this.listViewHeaderStatusMessage.setText(R.string.location_searching);
				this.listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

				new AppGetterTask(this.context, this.model, this.appAdapter, this.listView, this.applicationList, this.activityManager, this.notificationsButton).execute(new Location(location));
				new ReverseGeocodingTask(this.context, this.model, this.listViewHeaderStatusMessage).execute(new Location(location));

			} catch (Exception e) {
				Log.i(getClass().getSimpleName(), e.toString());
			}
		}
		else {
			Toast.makeText(this.context, R.string.location_error_toast, Toast.LENGTH_LONG).show();
			this.listView.finishRefresing();

			this.listViewHeaderStatusMessage.setText(R.string.location_needed);
		}
	}
}

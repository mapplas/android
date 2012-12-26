package com.mapplas.app.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import app.mapplas.com.R;

import com.mapplas.app.AwesomeListView;
import com.mapplas.app.adapters.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.app.async_tasks.AppGetterTask;
import com.mapplas.app.async_tasks.ReverseGeocodingTask;
import com.mapplas.app.handlers.MessageHandlerFactory;
import com.mapplas.app.threads.ServerIdentificationThread;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.NetworkConnectionChecker;
import com.mapplas.utils.static_intents.AppAdapterSingleton;
import com.mapplas.utils.static_intents.SuperModelSingleton;

public class MapplasActivity extends Activity {

	/* Debug Values */
	public final static boolean mDebug = false;

	/* Properties */
	private SuperModel model = new SuperModel();

	private Handler messageHandler = null;

	private boolean isSplashActive = true;

	private LocationManager locationManager = null;

	private LocationListener locationListener = null;

	public List<ApplicationInfo> applicationList = null;

	private AwesomeListView listView = null;

	private AppAdapter listViewAdapter = null;

//	private static SharedPreferences sharedPreferences = null;

	private TextView listViewHeaderStatusMessage = null;

	private ImageView listViewHeaderImage = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Load typefaces from MapplasApplication
		((MapplasApplication)this.getApplicationContext()).loadTypefaces();

		// Obtenemos el IMEI como identificador (ANDROID_ID da problemas)
		TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		this.model.setCurrentIMEI(manager.getDeviceId());

//		this.sharedPreferences = getApplicationContext().getSharedPreferences("synesth", Context.MODE_PRIVATE);
		this.isSplashActive = true;
		this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		// Identificamos contra el servidor
		try {
			Thread serverIdentificationThread = new Thread(new ServerIdentificationThread(this.model, this).getThread());
			serverIdentificationThread.run();
		} catch (Exception e) {
			this.model.setCurrentUser(null);
			Log.d(this.getClass().getSimpleName(), "Login: " + e);
		}

		// Get user application list
		final PackageManager pm = getPackageManager();
		this.applicationList = pm.getInstalledApplications(PackageManager.GET_META_DATA);

		// Load layout components
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		this.loadLayoutComponents(normalTypeFace);
		this.setClickListenersToButtons(normalTypeFace);

		// Load list
		this.loadApplicationsListView();

		this.messageHandler = new MessageHandlerFactory().getMapplasActivityMessageHandler(listViewHeaderStatusMessage, isSplashActive, model, listViewAdapter, listView, applicationList, this);

		// Check if wifi is enabled
		this.checkWifiStatus();
		
		this.loadLocalization();
		// TODO: uncomment for emulator use
//		Location location = new Location("");
//		(new AppGetterTask(MapplasActivity.this, model, messageHandler)).execute(new Location[] { location });
//		(new ReverseGeocodingTask(MapplasActivity.this, model, messageHandler)).execute(new Location[] { location });
	}

	/**
	 * 
	 * Private methods
	 * 
	 */

	/**
	 * Loads screen components
	 * 
	 * @param normalTypeFace
	 */
	private void loadLayoutComponents(Typeface normalTypeFace) {
		this.listView = (AwesomeListView)findViewById(R.id.lvLista);

		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout headerLayout = (LinearLayout)inflater.inflate(R.layout.ptr_header, null);

		// ListView header status message
		this.listViewHeaderStatusMessage = (TextView)headerLayout.findViewById(R.id.lblStatus);
		listViewHeaderStatusMessage.setTypeface(normalTypeFace);
		listViewHeaderStatusMessage.setText(R.string.location_searching);

		// ListView header status image
		this.listViewHeaderImage = (ImageView)headerLayout.findViewById(R.id.imgMap);
		listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);
	}

	private void setClickListenersToButtons(Typeface normalTypeFace) {
		// User profile button
		Button userProfileButton = (Button)findViewById(R.id.btnProfile);
		userProfileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapplasActivity.this, UserForm.class);
				intent.putExtra(Constants.MAPPLAS_LOGIN_USER_ID, model.currentUser());
				intent.putExtra(Constants.MAPPLAS_LOGIN_LOCATION_ID, model.currentLocation());

				MapplasActivity.this.startActivityForResult(intent, Constants.SYNESTH_USER_ID);
			}
		});

		// Notifications button
		Button notificationsButton = (Button)findViewById(R.id.btnNotifications);
		notificationsButton.setTypeface(normalTypeFace);
		notificationsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapplasActivity.this, AppNotifications.class);
				SuperModelSingleton.model = model;
//				intent.putExtra(Constants.MAPPLAS_NOTIFICATION_MODEL, model);
				MapplasActivity.this.startActivity(intent);
			}
		});
	}

	private void loadApplicationsListView() {
		// Add header to list
		LinearLayout listViewHeader = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.ptr_header, null);
		RelativeLayout headerLayout = (RelativeLayout)listViewHeader.findViewById(R.id.llInnerPtr);
		TextView headerTV = (TextView)listViewHeader.findViewById(R.id.lblAction);
		TextView wifiDisabledTV = (TextView)listViewHeader.findViewById(R.id.lblWifiDisabledMessage);
		ImageView headerIV = (ImageView)listViewHeader.findViewById(R.id.ivImage);
		
		Drawable pullToRefreshArrow = this.getResources().getDrawable(R.drawable.ic_pulltorefresh_arrow);
		Drawable ic_refreshImage = this.getResources().getDrawable(R.drawable.ic_refresh_photo);
		
		String pullToRefresh = this.getResources().getString(R.string.ptr_pull_to_refresh);
		String releaseToRefresh = this.getResources().getString(R.string.ptr_release_to_refresh);
		String loadingApps = this.getResources().getString(R.string.ptr_refreshing);

		this.listView.insertHeader(listViewHeader, headerLayout, headerTV, wifiDisabledTV, headerIV, pullToRefreshArrow, ic_refreshImage, pullToRefresh, releaseToRefresh, loadingApps);

		// Set release header listener
		this.listView.setOnReleasehHeaderListener(new AwesomeListView.OnRelease() {

			@Override
			public void onRelease() {
				try {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
				} catch (Exception e) {
					Log.i(this.getClass().getSimpleName(), e.toString());
				}
			}
		});

		// Set adapter
		this.listViewAdapter = new AppAdapter(this, this.listView, R.layout.rowloc, this.model.appList(), this.model.currentLocation(), this.model.currentDescriptiveGeoLoc(), this.model.currentUser());
		this.listView.setAdapter(this.listViewAdapter);
		AppAdapterSingleton.appAdapter = this.listViewAdapter;

		if(this.listView != null) {

			// Set scroll listener
			this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

					listView.mScrollState = scrollState;

					for(int i = 0; i < listView.getCount(); i++) {
						View v = listView.getChildAt(i);
						if(v != null) {
							ViewFlipper vf = (ViewFlipper)v.findViewById(R.id.vfRowLoc);
							if(vf != null) {
								vf.setInAnimation(null);
								vf.setOutAnimation(null);
								vf.setDisplayedChild(0);
							}
						}
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				}
			});
		}
	}

	/**
	 * Load localization
	 */
	private void loadLocalization() {
		try {

			if(this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

				this.locationListener = new LocationListener() {

					private String textoToast = "";

					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
//						textoToast = "onStatusChanged: " + provider + ", " + status + ", " + extras;
//						Toast.makeText(getBaseContext(), textoToast, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onProviderEnabled(String provider) {
//						textoToast = "onProviderEnabled: " + provider;
//						Toast.makeText(getBaseContext(), textoToast, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onProviderDisabled(String provider) {
//						textoToast = "onProviderDisabled: " + provider;
//						Toast.makeText(getBaseContext(), textoToast, Toast.LENGTH_LONG).show();
					}
					
					@Override
					public void onLocationChanged(Location location) {
						locationManager.removeUpdates(locationListener);

						listViewHeaderStatusMessage.setText(R.string.location_done);
						listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

						model.setCurrentLocation(location.getLatitude() + "," + location.getLongitude());

						try {
							listViewHeaderStatusMessage.setText(R.string.location_searching);
							listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

							(new AppGetterTask(MapplasActivity.this, model, messageHandler)).execute(new Location[] { location });
							(new ReverseGeocodingTask(MapplasActivity.this, model, messageHandler)).execute(new Location[] { location });

						} catch (Exception e) {
							Log.i(getClass().getSimpleName(), "LocationListener.onLocationChanged: " + e);
						}
					}
				};

				try {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
				} catch (Exception e1) {
					Log.i(getClass().getSimpleName(), "SynesthActivity.onCreate: " + e1);
				}
			}
			else {
				listViewHeaderStatusMessage.setText(R.string.location_error);
				Toast.makeText(getBaseContext(), R.string.location_error, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			listViewHeaderStatusMessage.setText(R.string.location_needed);
			Toast.makeText(getBaseContext(), R.string.location_needed, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	private void checkWifiStatus() {
		if (!new NetworkConnectionChecker().isWifiEnabled(this)) {
		    Toast.makeText(this, R.string.wifi_error_toast, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Getter and setters
	 */
	public AwesomeListView getListView() {
		return listView;
	}

	public void setListView(AwesomeListView listView) {
		this.listView = listView;
	}

	
}

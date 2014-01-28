package com.mapplas.app.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.GeoFence;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.model.database.repositories.GeoFenceRepository;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.database.repositories.UserRepository;
import com.mapplas.utils.gcm.GcmRegistrationManager;
import com.mapplas.utils.geofences.ReceiveTransitionsIntentService;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.location.location_manager.AroundRequesterLocationManager;
import com.mapplas.utils.location.location_manager.LocationRequesterLocationManagerFactory;
import com.mapplas.utils.location.play_services.AroundRequesterGooglePlayServices;
import com.mapplas.utils.network.NetworkConnectionChecker;
import com.mapplas.utils.network.async_tasks.UserIdentificationTask;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.third_party.RefreshableListView.OnRefreshListener;
import com.mapplas.utils.visual.custom_views.RobotoTextView;
import com.todddavies.components.progressbar.ProgressWheel;

public class MapplasActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, OnAddGeofencesResultListener, OnRemoveGeofencesResultListener { // languageactivity

	public static String PACKAGE_NAME = "";

	/* Debug Values */
	public final static boolean mDebug = false;

	/* Properties */
	private SuperModel model = new SuperModel();

	public ArrayList<ApplicationInfo> appsInstalledList = null;

	private RefreshableListView listView = null;

	private AppAdapter listViewAdapter = null;

	private TextView listViewHeaderStatusMessage = null;

	private ImageView listViewHeaderImage = null;

	private AroundRequesterGooglePlayServices appsRequester = null;

	private AroundRequesterLocationManager aroundRequester = null;

	private GcmRegistrationManager gcmManager;

	private GeoFence geofence1;

	private GeoFence geofence2;

	List<Geofence> mGeofenceList;

	private static final long SECONDS_PER_HOUR = 60;

	private static final long MILLISECONDS_PER_SECOND = 1000;

	private static final long GEOFENCE_EXPIRATION_IN_HOURS = 48;

	private static final long GEOFENCE_EXPIRATION_TIME = GEOFENCE_EXPIRATION_IN_HOURS * SECONDS_PER_HOUR * MILLISECONDS_PER_SECOND;

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private LocationClient locationClient;

	private PendingIntent pendingIntent;

	public enum REQUEST_TYPE {
		ADD, REMOVE_INTENT
	};

	private REQUEST_TYPE requestType;

	private boolean requestInProgress;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		MapplasActivity.PACKAGE_NAME = this.getApplicationContext().getPackageName();

		// Get phone IMEI as identifier (problems with ANDROID_ID)
		TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = manager.getDeviceId();
		if(imei == null) {
			imei = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		}
		this.model.setCurrentIMEI(imei);

		String networkCountryIso = manager.getNetworkCountryIso();
		this.model.setDeviceCountry(networkCountryIso);

		this.gcmManager = new GcmRegistrationManager(this, this);

		// Load typefaces from MapplasApplication
		((MapplasApplication)this.getApplicationContext()).loadTypefaces();
		new LanguageSetter(this).setLanguageToApp(((MapplasApplication)this.getApplicationContext()).getLanguage());

		this.startScreenAnimation();

		// Identificamos contra el servidor
		int requestNumber = 0;
		new UserIdentificationTask(this.model, this, this, requestNumber).execute();

		this.requestInProgress = false;
	}

	// Play Services APK check here too. NEEDED.
	@Override
	protected void onResume() {
		super.onResume();
		this.gcmManager.checkPlayServices();
	}

	public void continueActivityAfterUserIdentification() {
		// Register for notifications
		this.gcmManager.registerForC2dmNotifications(this.model.currentUser());

		// Get user application list
		this.appsInstalledList = new ArrayList<ApplicationInfo>();

		// Load layout components
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		this.setClickListenersToButtons(normalTypeFace);

		// Load list
		this.loadApplicationsListView(normalTypeFace);
		this.listViewAdapter = new AppAdapter(this, this.listView, this.model, this.appsInstalledList, this);
		this.listView.setAdapter(this.listViewAdapter);

		// Load location requesters
		this.appsRequester = new AroundRequesterGooglePlayServices(this, listViewHeaderStatusMessage, listViewHeaderImage, this.model, this.listViewAdapter, this.listView, this.appsInstalledList, this);

		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		this.aroundRequester = new AroundRequesterLocationManager(new LocationRequesterLocationManagerFactory(), locationManager, this, listViewHeaderStatusMessage, listViewHeaderImage, this.model, this.listViewAdapter, this.listView, this.appsInstalledList, this);

		// Check network status
		this.checkNetworkStatus();

		this.loadLocalization();
		// TODO: uncomment for emulator or mocked location use
		// Location location = new Location("");
		// location.setLatitude(40.492523);
		// location.setLongitude(-3.59589);
		//
		// this.model.setLocation(location);
		// new ReverseGeocodingTask(this, this.model,
		// this.listViewHeaderStatusMessage).execute(new Location(location));
		// new AppGetterTask(this, this.model, this.listViewAdapter,
		// this.listView, this.appsInstalledList, this, 0).execute(new
		// Location(location), true);
		
		this.addGeoFences();
	}

	@Override
	protected void onStart() {
		if(AppChangedSingleton.somethingChanged) {
			AppChangedSingleton.somethingChanged = false;
			if(AppChangedSingleton.changedList != null) {
				AppOrderedList changedList = AppChangedSingleton.changedList;
				this.model.updateAppList(changedList);
				AppChangedSingleton.changedList = null;
			}

			this.listView.updateAdapter(this, this.model, this.appsInstalledList);
		}
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.SYNESTH_USER_ID) {
			if(data != null && data.getExtras() != null && data.getExtras().containsKey(Constants.MAPPLAS_LOGIN_USER)) {
				this.model.setCurrentUser((User)data.getExtras().getParcelable(Constants.MAPPLAS_LOGIN_USER));
			}
		}
		else if(requestCode == Constants.MAPPLAS_GOOLE_POSITIONING_SETTINGS_CHANGED) {
			this.loadLocalization();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = this.getMenuInflater();
		menuInflater.inflate(R.layout.refresh_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.loadLocalization();
		return true;
	}

	/**
	 * 
	 * Private methods
	 * 
	 */

	private void setClickListenersToButtons(Typeface normalTypeFace) {
		// User profile button
		Button userProfileButton = (Button)findViewById(R.id.btnProfile);
		userProfileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapplasActivity.this, UserForm.class);
				intent.putExtra(Constants.MAPPLAS_LOGIN_USER, model.currentUser());
				intent.putExtra(Constants.MAPPLAS_LOGIN_USER_ID, model.currentUser().getId());
				intent.putExtra(Constants.MAPPLAS_LOGIN_LOCATION, model.currentLocation());
				intent.putExtra(Constants.MAPPLAS_LOGIN_APP_LIST, model.appList());

				// Save current user into DB
				try {
					UserRepository userRepo = RepositoryManager.users(MapplasActivity.this);
					userRepo.createOrUpdate(model.currentUser());
				} catch (SQLException e) {
					// Log.e(MapplasActivity.this.getClass().getSimpleName(),
					// e.toString());
				}

				MapplasActivity.this.startActivityForResult(intent, Constants.SYNESTH_USER_ID);
			}
		});
	}

	private void loadApplicationsListView(Typeface normalTypeface) {
		this.listView = (RefreshableListView)findViewById(R.id.lvLista);
		LinearLayout listViewHeader = this.listView.getHeader();

		// ListView header status message
		this.listViewHeaderStatusMessage = (TextView)listViewHeader.findViewById(R.id.lblStatus);
		this.listViewHeaderStatusMessage.setTypeface(normalTypeface);
		this.listViewHeaderStatusMessage.setText(R.string.location_searching);

		// ListView header status image
		this.listViewHeaderImage = (ImageView)listViewHeader.findViewById(R.id.imgMap);
		this.listViewHeaderImage.setBackgroundResource(R.drawable.ic_map);

		// Set refresh header listener
		this.listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(RefreshableListView listView) {
				if(!AppRequestBeingDoneSingleton.requestBeingDone) {
					// Log.d(MapplasActivity.this.getClass().getSimpleName(),
					// "REQUEST");
					loadLocalization();
				}
			}
		});
	}

	private void startScreenAnimation() {
		ProgressWheel progressWheel = (ProgressWheel)this.findViewById(R.id.pw_spinner);
		progressWheel.spin();

		// 124 are number of lines in text file
		Random random = new Random();
		int randomNum = random.nextInt(124);

		Resources res = getResources();
		InputStream inputStream = res.openRawResource(R.raw.citas);
		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);

		RobotoTextView text = (RobotoTextView)this.findViewById(R.id.tv_citas);
		String line;
		int i = 0;

		try {
			while ((line = buffreader.readLine()) != null) {
				if(i != randomNum) {
					i++;
				}
				else {
					text.setText(line);
					break;
				}
			}
		} catch (IOException e) {
		}
	}

	/**
	 * Load localization depending on Google Play Services is present on mobile
	 * or not
	 */
	private void loadLocalization() {

		if(this.servicesConnected()) {
			this.appsRequester.start();
		}
		else {
			this.aroundRequester.start();
		}

	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if(ConnectionResult.SUCCESS == resultCode) {
			return true;
		}
		// Google Play services was not available for some reason
		else {
			// // Get the error code
			// int errorCode = connectionResult.getErrorCode();
			// // Get the error dialog from Google Play services
			// Dialog errorDialog =
			// GooglePlayServicesUtil.getErrorDialog(errorCode, this,
			// CONNECTION_FAILURE_RESOLUTION_REQUEST);
			//
			// // If Google Play services can provide an error dialog
			// if(errorDialog != null) {
			// // Create a new DialogFragment for the error dialog
			// ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// // Set the dialog in the DialogFragment
			// errorFragment.setDialog(errorDialog);
			// // Show the error dialog in the DialogFragment
			// errorFragment.show(getSupportFragmentManager(),
			// "Geofence Detection");
			return false;
			// }
		}
	}

	private void checkNetworkStatus() {
		NetworkConnectionChecker networkConnectionChecker = new NetworkConnectionChecker();
		if((networkConnectionChecker.isWifiConnected(this) || networkConnectionChecker.isNetworkConnectionConnected(this)) && !networkConnectionChecker.isWifiEnabled(this)) {
			Toast.makeText(this, R.string.wifi_error_toast, Toast.LENGTH_LONG).show();
		}
	}
	
	/******************************
	 * 
	 * GEOFENCES
	 * 
	 ******************************/
	
	private void addGeoFences() {
		this.mGeofenceList = new ArrayList<Geofence>();

		GeoFenceRepository repo = RepositoryManager.geofences(this);

		this.geofence1 = new GeoFence("1", 42.3453453, -1.4353453, 45343, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER);
		this.geofence2 = new GeoFence("2", 43.26728, -1.975968, 543, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER);

		try {
			repo.createOrUpdateBatch(this.geofence1);
			repo.createOrUpdateBatch(this.geofence2);
			repo.createOrUpdateFlush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.mGeofenceList.add(this.geofence1.toGeofence());
		this.mGeofenceList.add(this.geofence2.toGeofence());
	}

	private PendingIntent getTransitionPendingIntent() {
		// Create an explicit Intent
		Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);

		/*
		 * Return the PendingIntent
		 */
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/**
	 * Start a request for geofence monitoring by calling
	 * LocationClient.connect().
	 */
	public void addGeofences() {
		// Start a request to add geofences
		requestType = REQUEST_TYPE.ADD;

		/*
		 * Test for Google Play services after setting the request type. If
		 * Google Play services isn't present, the proper request can be
		 * restarted.
		 */
		if(!servicesConnected()) {
			return;
		}
		/*
		 * Create a new location client object. Since the current activity class
		 * implements ConnectionCallbacks and OnConnectionFailedListener, pass
		 * the current activity object as the listener for both parameters
		 */
		locationClient = new LocationClient(this, this, this);

		// If a request is not already underway
		if(!requestInProgress) {
			// Indicate that a request is underway
			requestInProgress = true;
			// Request a connection from the client to Location Services
			locationClient.connect();
		}
		else {
			/*
			 * A request is already underway. You can handle this situation by
			 * disconnecting the client, re-setting the flag, and then re-trying
			 * the request.
			 */
		}
	}

	/**
	 * Start a request to remove geofences by calling LocationClient.connect()
	 */
	public void removeGeofences(PendingIntent requestIntent) {
		// Record the type of removal request
		requestType = REQUEST_TYPE.REMOVE_INTENT;
		/*
		 * Test for Google Play services after setting the request type. If
		 * Google Play services isn't present, the request can be restarted.
		 */
		if(!servicesConnected()) {
			return;
		}
		// Store the PendingIntent
		pendingIntent = requestIntent;
		/*
		 * Create a new location client object. Since the current activity class
		 * implements ConnectionCallbacks and OnConnectionFailedListener, pass
		 * the current activity object as the listener for both parameters
		 */
		locationClient = new LocationClient(this, this, this);
		// If a request is not already underway
		if(!requestInProgress) {
			// Indicate that a request is underway
			requestInProgress = true;
			// Request a connection from the client to Location Services
			locationClient.connect();
		}
		else {
			/*
			 * A request is already underway. You can handle this situation by
			 * disconnecting the client, re-setting the flag, and then re-trying
			 * the request.
			 */
		}
	}

	/*
	 * Provide the implementation of
	 * OnAddGeofencesResultListener.onAddGeofencesResult. Handle the result of
	 * adding the geofences
	 */
	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// If adding the geofences was successful
		if(LocationStatusCodes.SUCCESS == statusCode) {
			/*
			 * Handle successful addition of geofences here. You can send out a
			 * broadcast intent or update the UI. geofences into the Intent's
			 * extended data.
			 */
		}
		else {
			// If adding the geofences failed
			/*
			 * Report errors here. You can log the error using Log.e() or update
			 * the UI.
			 */
		}
		// Turn off the in progress flag and disconnect the client
		requestInProgress = false;
		locationClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Turn off the request flag
		requestInProgress = false;
		/*
		 * If the error has a resolution, start a Google Play services activity
		 * to resolve it.
		 */
		if(connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
			// If no resolution is available, display an error dialog
		}
		else {
			// Get the error code
			int errorCode = connectionResult.getErrorCode();
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			// If Google Play services can provide an error dialog
			if(errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				// ErrorDialogFragment errorFragment = new
				// ErrorDialogFragment();
				// // Set the dialog in the DialogFragment
				// errorFragment.setDialog(errorDialog);
				// // Show the error dialog in the DialogFragment
				// errorFragment.show(getSupportFragmentManager(),
				// "Geofence Detection");
			}
		}
	}

	/*
	 * Provide the implementation of ConnectionCallbacks.onConnected() Once the
	 * connection is available, send a request to add the Geofences
	 */
	@Override
	public void onConnected(Bundle arg0) {
		switch (requestType) {
			case ADD:
				// Get the PendingIntent for the request
				pendingIntent = getTransitionPendingIntent();
				// Send a request to add the current geofences
				locationClient.addGeofences(mGeofenceList, pendingIntent, this);

			case REMOVE_INTENT:
				locationClient.removeGeofences(pendingIntent, this);
				break;
		}
	}

	/*
	 * Implement ConnectionCallbacks.onDisconnected() Called by Location
	 * Services once the location client is disconnected.
	 */
	@Override
	public void onDisconnected() {
		// Turn off the request flag
		requestInProgress = false;
		// Destroy the current location client
		locationClient = null;
	}

	@Override
	public void onRemoveGeofencesByPendingIntentResult(int statusCode, PendingIntent arg1) {
		// If removing the geofences was successful
		if(statusCode == LocationStatusCodes.SUCCESS) {
			/*
			 * Handle successful removal of geofences here. You can send out a
			 * broadcast intent or update the UI. geofences into the Intent's
			 * extended data.
			 */
		}
		else {
			// If adding the geocodes failed
			/*
			 * Report errors here. You can log the error using Log.e() or update
			 * the UI.
			 */
		}
		/*
		 * Disconnect the location client regardless of the request status, and
		 * indicate that a request is no longer in progress
		 */
		requestInProgress = false;
		locationClient.disconnect();
	}

	@Override
	public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] arg1) {
		// If removing the geocodes was successful
		if(LocationStatusCodes.SUCCESS == statusCode) {
			/*
			 * Handle successful removal of geofences here. You can send out a
			 * broadcast intent or update the UI. geofences into the Intent's
			 * extended data.
			 */
		}
		else {
			// If removing the geofences failed
			/*
			 * Report errors here. You can log the error using Log.e() or update
			 * the UI.
			 */
		}
		// Indicate that a request is no longer in progress
		requestInProgress = false;
		// Disconnect the location client
		locationClient.disconnect();
	}

}
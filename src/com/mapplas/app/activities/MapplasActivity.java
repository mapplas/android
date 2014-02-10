package com.mapplas.app.activities;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.model.database.MySQLiteHelper;
import com.mapplas.utils.gcm.GcmRegistrationManager;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.location.location_manager.AroundRequesterLocationManager;
import com.mapplas.utils.location.location_manager.LocationRequesterLocationManagerFactory;
import com.mapplas.utils.location.play_services.AroundRequesterGooglePlayServices;
import com.mapplas.utils.network.NetworkConnectionChecker;
import com.mapplas.utils.network.async_tasks.AppGetterTask;
import com.mapplas.utils.network.async_tasks.UserIdentificationTask;
import com.mapplas.utils.searcher.SearchManager;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.static_intents.SuperModelSingleton;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.third_party.RefreshableListView.OnRefreshListener;
import com.mapplas.utils.visual.SplashScreenTextSelector;
import com.mapplas.utils.visual.custom_views.RobotoTextView;

public class MapplasActivity extends LanguageActivity {

	public static String PACKAGE_NAME = "";

	public final static boolean mDebug = false;

	private SuperModel model = new SuperModel();

	public ArrayList<ApplicationInfo> appsInstalledList = null;

	private RefreshableListView listView = null;

	private AppAdapter listViewAdapter = null;

	private TextView listViewHeaderStatusMessage = null;

	private ImageView listViewHeaderImage = null;

	private AroundRequesterGooglePlayServices appsRequester = null;

	private AroundRequesterLocationManager aroundRequester = null;

	private GcmRegistrationManager gcmManager;

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
		SuperModelSingleton.model = this.model;

		// Get user application list
		this.appsInstalledList = new ArrayList<ApplicationInfo>();

		// Load layout components
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		this.setClickListenersToButtons(normalTypeFace);

		this.initializeAutocompleteSearchView();

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
		// location.setLatitude(41.353673);
		// location.setLongitude(2.128786);
		//
		// this.model.setLocation(location);
		// new ReverseGeocodingTask(this, this.model,
		// this.listViewHeaderStatusMessage).execute(new Location(location));
		// new AppGetterTask(this, this.model, this.listViewAdapter,
		// this.listView, this.appsInstalledList, this, 0).execute(new
		// Location(location), true);
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
				MySQLiteHelper db = new MySQLiteHelper(MapplasActivity.this);
				db.insertOrUpdateUser(model.currentUser());

				MapplasActivity.this.startActivityForResult(intent, Constants.SYNESTH_USER_ID);
			}
		});

		// Search button
		Button searchButton = (Button)findViewById(R.id.btnSearch);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.layoutSearch).setVisibility(View.VISIBLE);
			}
		});

		// Search layout back button
		Button searchLayoutBack = (Button)findViewById(R.id.searchLayoutBtnBack);
		searchLayoutBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.layoutSearch).setVisibility(View.GONE);
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
					loadLocalization();
				}
			}
		});
	}

	private void startScreenAnimation() {
		RobotoTextView mainTextView = (RobotoTextView)this.findViewById(R.id.tv_citas);
		RobotoTextView authorTextView = (RobotoTextView)this.findViewById(R.id.tv_citas_author);

		SplashScreenTextSelector splashTextSelector = new SplashScreenTextSelector(mainTextView, authorTextView, this);
		splashTextSelector.setRandomText();
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

	private void initializeAutocompleteSearchView() {
		SearchManager searchManager = new SearchManager((AutoCompleteTextView)findViewById(R.id.autocompleteSearchView), this, this);
		searchManager.initializeSearcher();
	}
	
	public void requestAppsForEntity(int entity_id) {
		int requestNumber = 0;
		new AppGetterTask(this, this.model, this.listViewAdapter, this.listView, this.appsInstalledList, this, requestNumber, Constants.APP_REQUEST_TYPE_ENTITY_ID).execute(null, true, entity_id);
//		new ReverseGeocodingTask(this.context, this.model, this.listViewHeaderStatusMessage).execute(new Location(location));
	}
}
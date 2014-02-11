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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.mapplas.utils.visual.animation.NavigationBarButtonAnimation;
import com.mapplas.utils.visual.custom_views.RobotoButton;
import com.mapplas.utils.visual.custom_views.RobotoTextView;
import com.mapplas.utils.visual.custom_views.autocomplete.CustomAutoCompleteView;
import com.mapplas.utils.visual.helpers.AppGetterTaskViewsContainer;

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

	private RelativeLayout layoutSearch;

	private CustomAutoCompleteView autoComplete;

	private ProgressBar searchLayoutSpinner;

	private RobotoButton userProfileButton;

	private RobotoButton searchButton;

	private AppGetterTaskViewsContainer appGetterTaskViewsContainer;

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

		this.initializeLayoutComponents();
		this.initializeAppGetterTaskContainer();

		// Load layout components
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		this.setClickListenersToButtons(normalTypeFace);

		// Load list
		this.loadApplicationsListView(normalTypeFace);
		this.appGetterTaskViewsContainer.listView = this.listView;
		this.listViewAdapter = new AppAdapter(this, this.model, this.appsInstalledList, this, this.appGetterTaskViewsContainer);
		this.listView.setAdapter(this.listViewAdapter);
		this.appGetterTaskViewsContainer.listViewAdapter = this.listViewAdapter;

		this.initializeAutocompleteSearchView();

		// Load location requesters
		this.appsRequester = new AroundRequesterGooglePlayServices(this, listViewHeaderStatusMessage, listViewHeaderImage, this.model, this.appsInstalledList, this, this.appGetterTaskViewsContainer);

		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		this.aroundRequester = new AroundRequesterLocationManager(new LocationRequesterLocationManagerFactory(), locationManager, this, listViewHeaderStatusMessage, listViewHeaderImage, this.model, this.appsInstalledList, this, this.appGetterTaskViewsContainer);

		// Check network status
		this.checkNetworkStatus();

		// this.loadLocalization();
		// TODO: uncomment for emulator or mocked location use
//		Location location = new Location("");
//		location.setLatitude(41.353673);
//		location.setLongitude(2.128786);
//
//		this.model.setLocation(location);
//		new ReverseGeocodingTask(this, this.model, this.listViewHeaderStatusMessage).execute(new Location(location));
//		new AppGetterTask(this, this.model, this.appsInstalledList, this, 0, Constants.APP_REQUEST_TYPE_LOCATION, this.appGetterTaskViewsContainer).execute(location, true, -1);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK && layoutSearch.getVisibility() == View.VISIBLE) {
			layoutSearch.setVisibility(View.GONE);
			new NavigationBarButtonAnimation(userProfileButton, searchButton).startFadeInAnimation(this);

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * Private methods
	 * 
	 */
	private void initializeLayoutComponents() {
		this.layoutSearch = (RelativeLayout)findViewById(R.id.layoutSearch);
		this.searchLayoutSpinner = (ProgressBar)findViewById(R.id.search_layout_spinner);
		this.autoComplete = (CustomAutoCompleteView)findViewById(R.id.autocompleteSearchView);

		this.userProfileButton = (RobotoButton)findViewById(R.id.btnProfile);
		this.searchButton = (RobotoButton)findViewById(R.id.btnSearch);
	}

	private void initializeAppGetterTaskContainer() {
		RelativeLayout navigationBar = (RelativeLayout)findViewById(R.id.navigation_bar);
		RelativeLayout radarLayout = (RelativeLayout)findViewById(R.id.radar_layout);
		this.appGetterTaskViewsContainer = new AppGetterTaskViewsContainer(this.layoutSearch, this.searchLayoutSpinner, this.userProfileButton, this.searchButton, navigationBar, radarLayout);
	}

	private void setClickListenersToButtons(Typeface normalTypeFace) {
		// User profile button
		this.userProfileButton.setOnClickListener(new View.OnClickListener() {

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
		this.searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutSearch.setVisibility(View.VISIBLE);
				autoComplete.setVisibility(View.VISIBLE);
				searchLayoutSpinner.setVisibility(View.GONE);

				new NavigationBarButtonAnimation(userProfileButton, searchButton).startFadeOutAnimation(MapplasActivity.this);

				// Intercept back layout touch events
				layoutSearch.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return true;
					}
				});

				autoComplete.requestFocus();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(autoComplete, InputMethodManager.SHOW_IMPLICIT);
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
		SearchManager searchManager = new SearchManager(this.autoComplete, this, this, this.listView, this.searchLayoutSpinner);
		searchManager.initializeSearcher();
	}

	public void requestAppsForEntity(int entity_id, String city) {
		this.listViewHeaderStatusMessage.setText(city);
		int requestNumber = 0;
		this.model.initializeForNewAppRequest();

		new AppGetterTask(this, this.model, this.appsInstalledList, this, requestNumber, Constants.APP_REQUEST_TYPE_ENTITY_ID, this.appGetterTaskViewsContainer).execute(null, true, entity_id);
	}
}
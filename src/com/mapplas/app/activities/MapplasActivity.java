package com.mapplas.app.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
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
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.database.repositories.UserRepository;
import com.mapplas.utils.gcm.GcmRegistrationManager;
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

public class MapplasActivity extends LanguageActivity {

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
		// 124 are number of lines in text file
		Random random = new Random();
		int randomNum = random.nextInt(124);

		Resources res = getResources();
		InputStream inputStream = res.openRawResource(R.raw.citas);
		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);

		TextView text = (TextView)this.findViewById(R.id.tv_citas);
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

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// Log.e("LOCALIZATION", resultCode + "");

		if(resultCode == ConnectionResult.SUCCESS) {
			this.appsRequester.start();
		}
		else {
			this.aroundRequester.start();
		}

	}

	private void checkNetworkStatus() {
		NetworkConnectionChecker networkConnectionChecker = new NetworkConnectionChecker();
		if((networkConnectionChecker.isWifiConnected(this) || networkConnectionChecker.isNetworkConnectionConnected(this)) && !networkConnectionChecker.isWifiEnabled(this)) {
			Toast.makeText(this, R.string.wifi_error_toast, Toast.LENGTH_LONG).show();
		}
	}

}

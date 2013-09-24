package com.mapplas.app.activities;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.mapplas.com.R;

import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.database.repositories.UserRepository;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.location.AroundRequester;
import com.mapplas.utils.location.UserLocationRequesterFactory;
import com.mapplas.utils.network.NetworkConnectionChecker;
import com.mapplas.utils.network.requests.UserIdentificationRequester;
import com.mapplas.utils.static_intents.AppChangedSingleton;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.third_party.RefreshableListView.OnRefreshListener;

public class MapplasActivity extends LanguageActivity {

	public static String PACKAGE_NAME = "";

	/* Debug Values */
	public final static boolean mDebug = false;

	/* Properties */
	private SuperModel model = new SuperModel();

	private LocationManager locationManager = null;

	public ArrayList<ApplicationInfo> appsInstalledList = null;

	private RefreshableListView listView = null;

	private AppAdapter listViewAdapter = null;

	private TextView listViewHeaderStatusMessage = null;

	private ImageView listViewHeaderImage = null;

	private AroundRequester aroundRequester = null;

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

		// Load typefaces from MapplasApplication
		((MapplasApplication)this.getApplicationContext()).loadTypefaces();
		new LanguageSetter(this).setLanguageToApp(((MapplasApplication)this.getApplicationContext()).getLanguage());
		
		this.startRadarAnimation();

		// Identificamos contra el servidor
		try {
			Thread serverIdentificationThread = new Thread(new UserIdentificationRequester(this.model).getThread());
			serverIdentificationThread.run();
		} catch (Exception e) {
			this.model.setCurrentUser(null);
//			Log.d(this.getClass().getSimpleName(), "Login: " + e);
		}

		// Get user application list
		this.appsInstalledList = new ArrayList<ApplicationInfo>();

		// Load layout components
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		this.setClickListenersToButtons(normalTypeFace);

		// Load list
		this.loadApplicationsListView(normalTypeFace);
		
		// Load progress layout
		RelativeLayout progressLayout = (RelativeLayout)this.findViewById(R.id.progress_layout);
		boolean comesFromRadarLayout = true;

		// Load around requester
		this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		this.listViewAdapter = new AppAdapter(this, this.listView, this.model, this.appsInstalledList, this, progressLayout);
		this.listView.setAdapter(this.listViewAdapter);
		
		this.aroundRequester = new AroundRequester(new UserLocationRequesterFactory(), this.locationManager, this, this.listViewHeaderStatusMessage, this.listViewHeaderImage, this.model, this.listViewAdapter, this.listView, this.appsInstalledList, this, progressLayout, comesFromRadarLayout);

		// Check network status
		this.checkNetworkStatus();

		this.loadLocalization();
		// TODO: uncomment for emulator or mocked location use
//		 Location location = new Location("");
//		 location.setLatitude(40.431);
//		 location.setLongitude(-3.687);
//		 this.model.setLocation(location);
//		 new ReverseGeocodingTask(this, this.model,
//		 this.listViewHeaderStatusMessage).execute(new Location(location));
//		 new AppGetterTask(this, this.model, this.listViewAdapter, this.listView, this.appsInstalledList, this).execute(new Location(location), true);
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
//					Log.e(MapplasActivity.this.getClass().getSimpleName(), e.toString());
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
				loadLocalization();
			}
		});
	}

	/**
	 * Radar animation
	 */
	private void startRadarAnimation() {
		ImageView radar1 = (ImageView)this.findViewById(R.id.radar_1);
		((BitmapDrawable)radar1.getDrawable()).setAntiAlias(true);
		RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		this.setRotateAnimConstants(rotate);
		rotate.setDuration(1600);
		radar1.setDrawingCacheEnabled(false);
		radar1.startAnimation(rotate);

		ImageView radar2 = (ImageView)this.findViewById(R.id.radar_2);
		((BitmapDrawable)radar2.getDrawable()).setAntiAlias(true);
		RotateAnimation rotate2 = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		this.setRotateAnimConstants(rotate2);
		rotate2.setDuration(1800);
		radar2.setDrawingCacheEnabled(true);
		radar2.startAnimation(rotate2);

		ImageView radar3 = (ImageView)this.findViewById(R.id.radar_3);
		((BitmapDrawable)radar3.getDrawable()).setAntiAlias(true);
		RotateAnimation rotate3 = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		this.setRotateAnimConstants(rotate3);
		rotate3.setDuration(3400);
		radar3.setDrawingCacheEnabled(true);
		radar3.startAnimation(rotate3);

		ImageView radar4 = (ImageView)this.findViewById(R.id.radar_4);
		((BitmapDrawable)radar4.getDrawable()).setAntiAlias(true);
		RotateAnimation rotate4 = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		this.setRotateAnimConstants(rotate4);
		rotate4.setDuration(2600);
		radar4.setDrawingCacheEnabled(false);
		radar4.startAnimation(rotate4);

//		ImageView radar5 = (ImageView)this.findViewById(R.id.radar_5);
//		RotateAnimation rotate5 = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		this.setRotateAnimConstants(rotate5);
//		rotate5.setDuration(1200);
//		radar5.setDrawingCacheEnabled(true);
//		radar5.startAnimation(rotate5);

//		ImageView radar6 = (ImageView)this.findViewById(R.id.radar_6);
//		RotateAnimation rotate6 = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		this.setRotateAnimConstants(rotate6);
//		rotate6.setDuration(1200);
//		radar6.setDrawingCacheEnabled(true);
//		radar6.startAnimation(rotate6);
	}

	private void setRotateAnimConstants(RotateAnimation animation) {
		animation.setRepeatMode(Animation.RESTART);
		animation.setFillAfter(false);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setInterpolator(new LinearInterpolator());
	}

	/**
	 * Load localization
	 */
	private void loadLocalization() {
		this.aroundRequester.start();
	}

	private void checkNetworkStatus() {
		NetworkConnectionChecker networkConnectionChecker = new NetworkConnectionChecker();
		if((networkConnectionChecker.isWifiConnected(this) || networkConnectionChecker.isNetworkConnectionConnected(this)) && !networkConnectionChecker.isWifiEnabled(this)) {
			Toast.makeText(this, R.string.wifi_error_toast, Toast.LENGTH_LONG).show();
		}
	}

}

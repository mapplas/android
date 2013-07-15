package com.mapplas.app.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;
import com.mapplas.model.database.repositories.RepositoryManager;
import com.mapplas.model.database.repositories.UserRepository;
import com.mapplas.utils.infinite_scroll.InfiniteScrollManager;
import com.mapplas.utils.location.AroundRequester;
import com.mapplas.utils.location.UserLocationRequesterFactory;
import com.mapplas.utils.network.NetworkConnectionChecker;
import com.mapplas.utils.network.requests.UserIdentificationRequester;
import com.mapplas.utils.static_intents.AppChangedSingleton;

public class MapplasActivity extends Activity {

	public static String PACKAGE_NAME = "";

	/* Debug Values */
	public final static boolean mDebug = false;

	/* Properties */
	private SuperModel model = new SuperModel();

	private LocationManager locationManager = null;

	public List<ApplicationInfo> applicationList = null;

	private AwesomeListView listView = null;

	private AppAdapter listViewAdapter = null;

	private TextView listViewHeaderStatusMessage = null;

	private ImageView listViewHeaderImage = null;

	private AroundRequester aroundRequester = null;

	private Button notificationsButton;

	private boolean pressedNotificationScreen = false;

	private TextView latitudeTV;

	private TextView longitudeTV;
	
	private Handler latitudeTVHandler = new Handler();
	
	private Handler longitudeTVHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Load typefaces from MapplasApplication
		((MapplasApplication)this.getApplicationContext()).loadTypefaces();

		MapplasActivity.PACKAGE_NAME = this.getApplicationContext().getPackageName();

		// Get phone IMEI as identifier (problems with ANDROID_ID)
		TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		this.model.setCurrentIMEI(manager.getDeviceId());

		this.startRadarAnimation();
		this.startLatLongAnimation();

		// Identificamos contra el servidor
		try {
			Thread serverIdentificationThread = new Thread(new UserIdentificationRequester(this.model).getThread());
			serverIdentificationThread.run();
		} catch (Exception e) {
			this.model.setCurrentUser(null);
			Log.d(this.getClass().getSimpleName(), "Login: " + e);
		}

		// Get user application list
		this.applicationList = new ArrayList<ApplicationInfo>();

		// Load layout components
		Typeface normalTypeFace = ((MapplasApplication)this.getApplicationContext()).getTypeFace();
		this.setClickListenersToButtons(normalTypeFace);

		// Load list
		this.loadApplicationsListView(normalTypeFace);

		// Load around requester
		this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		ActivityManager activityManager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
		this.aroundRequester = new AroundRequester(new UserLocationRequesterFactory(), this.locationManager, AroundRequester.LOCATION_TIMEOUT_IN_MILLISECONDS, this, this.listViewHeaderStatusMessage, this.listViewHeaderImage, this.model, this.listView, this.listViewAdapter, this.applicationList, activityManager, this.notificationsButton);

		// Check network status
		this.checkNetworkStatus();

		this.loadLocalization();
		// TODO: uncomment for emulator use
		// Location location = new Location("");
		// location.setLatitude(43.291248);
		// location.setLongitude(-1.982539);
		// new AppGetterTask(this, this.model, this.listViewAdapter,
		// this.listView, this.applicationList, activityManager,
		// this.notificationsButton).execute(new Location(location));
		// new ReverseGeocodingTask(this, this.model,
		// this.listViewHeaderStatusMessage).execute(new Location(location));
	}

	@Override
	protected void onStart() {
		if(AppChangedSingleton.somethingChanged) {
			AppChangedSingleton.somethingChanged = false;
			if(AppChangedSingleton.changedList != null) {
				AppOrderedList changedList = AppChangedSingleton.changedList;
				this.model.setAppList(changedList);
				this.model.appList().sort();
				AppChangedSingleton.changedList = null;
			}

			this.listView.updateAdapter(this, this.model, new InfiniteScrollManager().getFirstXNumberOfApps(this.model));
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		if(this.pressedNotificationScreen) {
			// Remove notifications red background
			this.notificationsButton.setText("");
			this.notificationsButton.setBackgroundResource(R.drawable.menu_notifications_button);

			this.pressedNotificationScreen = false;
		}
		super.onStop();
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
					Log.e(MapplasActivity.this.getClass().getSimpleName(), e.toString());
				}

				MapplasActivity.this.startActivityForResult(intent, Constants.SYNESTH_USER_ID);
			}
		});
	}

	private void loadApplicationsListView(Typeface normalTypeface) {
		this.listView = (AwesomeListView)findViewById(R.id.lvLista);

		// Add header to list
		LinearLayout listViewHeader = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.ptr_header, null);
		RelativeLayout headerLayout = (RelativeLayout)listViewHeader.findViewById(R.id.llInnerPtr);
		TextView headerTV = (TextView)listViewHeader.findViewById(R.id.lblAction);
		TextView wifiDisabledTV = (TextView)listViewHeader.findViewById(R.id.lblWifiDisabledMessage);
		ImageView headerIV = (ImageView)listViewHeader.findViewById(R.id.ivImage);

		// ListView header status message
		this.listViewHeaderStatusMessage = (TextView)listViewHeader.findViewById(R.id.lblStatus);
		this.listViewHeaderStatusMessage.setTypeface(normalTypeface);
		this.listViewHeaderStatusMessage.setText(R.string.location_searching);

		// ListView header status image
		this.listViewHeaderImage = (ImageView)listViewHeader.findViewById(R.id.imgMap);
		this.listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

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
					loadLocalization();
				} catch (Exception e) {
					Log.i(this.getClass().getSimpleName(), e.toString());
				}
			}
		});

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
	 * Radar animation
	 */
	private void startRadarAnimation() {
		ImageView outerImage = (ImageView)this.findViewById(R.id.imgOuter);
		RotateAnimation rotate = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(1200);
		rotate.setRepeatMode(Animation.RESTART);
		rotate.setRepeatCount(Animation.INFINITE);
		rotate.setInterpolator(new LinearInterpolator());
		outerImage.startAnimation(rotate);

		ImageView northSouthImage = (ImageView)this.findViewById(R.id.imgMeasures);
		RotateAnimation rotate2 = new RotateAnimation(360, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate2.setDuration(1200);
		rotate2.setRepeatMode(Animation.RESTART);
		rotate2.setRepeatCount(Animation.INFINITE);
		rotate2.setInterpolator(new LinearInterpolator());
		northSouthImage.startAnimation(rotate2);

		ImageView trianglesImage = (ImageView)this.findViewById(R.id.imgTriangles);
		RotateAnimation rotate3 = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate3.setDuration(1200);
		rotate3.setRepeatMode(Animation.RESTART);
		rotate3.setRepeatCount(Animation.INFINITE);
		rotate3.setInterpolator(new LinearInterpolator());
		trianglesImage.startAnimation(rotate3);

		ImageView shadowImage = (ImageView)this.findViewById(R.id.imgShadow);
		RotateAnimation rotate4 = new RotateAnimation(360, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate4.setDuration(1200);
		rotate4.setRepeatMode(Animation.RESTART);
		rotate4.setRepeatCount(Animation.INFINITE);
		rotate4.setInterpolator(new LinearInterpolator());
		shadowImage.startAnimation(rotate4);
	}

	private void startLatLongAnimation() {
		this.latitudeTV = (TextView)this.findViewById(R.id.lblLat);
		this.latitudeTVHandler.post(this.latitudeTVRunnable);

		this.longitudeTV = (TextView)this.findViewById(R.id.lblLon);
		this.longitudeTVHandler.post(this.longitudeTVRunnable);
	}
	
	private Runnable latitudeTVRunnable = new Runnable() {
        public void run() {
			Random random = new Random();
        	latitudeTV.setText((random.nextInt(180 + 180) - 180) + "," + random.nextInt(99999));
            latitudeTVHandler.postDelayed(latitudeTVRunnable, 100);
        }
    };
    
    private Runnable longitudeTVRunnable = new Runnable() {
		@Override
		public void run() {
			Random random = new Random();
			longitudeTV.setText((random.nextInt(90 + 90) - 90) + "," + random.nextInt(99999));
			longitudeTVHandler.postDelayed(longitudeTVRunnable, 100);
		}
	};

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

	/**
	 * Getter and setters
	 */
	public AwesomeListView getListView() {
		return listView;
	}
}

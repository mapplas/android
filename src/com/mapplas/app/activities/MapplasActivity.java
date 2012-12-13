package com.mapplas.app.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
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
import com.mapplas.app.LocalizationAdapter;
import com.mapplas.app.MessageHandlerFactory;
import com.mapplas.app.async_tasks.AppGetterTask;
import com.mapplas.app.async_tasks.ReverseGeocodingTask;
import com.mapplas.app.threads.ServerIdentificationThread;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.model.User;

public class MapplasActivity extends Activity {

	/* Debug Values */
	public final static boolean mDebug = false;

	/* Properties */
	private SuperModel model = new SuperModel();
	
	private Handler messageHandler = null;

	private boolean isSplashActive = true;

	private LocationManager locationManager = null;

	private LocationListener locationListener = null;

	private Typeface typeface = null;

	private Typeface typefaceBold = null;

	private Typeface typefaceItalic = null;

	public List<ApplicationInfo> applicationList = null;

	private AwesomeListView listView = null;
	
	private LocalizationAdapter listViewAdapter = null;

	private static SharedPreferences sharedPreferences = null;

	private TextView listViewHeaderStatusMessage = null;

	private ImageView listViewHeaderImage = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Obtenemos el IMEI como identificador (ANDROID_ID da problemas)
		TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		this.model.currentIMEI = manager.getDeviceId();

		this.sharedPreferences = getApplicationContext().getSharedPreferences("synesth", Context.MODE_PRIVATE);
		this.isSplashActive = true;
		this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		// Identificamos contra el servidor
		try {
			Thread serverIdentificationThread = new Thread(new ServerIdentificationThread(this.model).getThread());
			serverIdentificationThread.run();
		} catch (Exception e) {
			this.model.currentUser = null;
			Log.d(this.getClass().getSimpleName(), "Login: " + e);
		}

		// Get user application list
		final PackageManager pm = getPackageManager();
		this.applicationList = pm.getInstalledApplications(PackageManager.GET_META_DATA);

		this.loadAppTypefaces();

		this.loadLayoutComponents();
		this.setClickListenersToButtons();

		this.loadApplicationsListView();

		this.messageHandler = new MessageHandlerFactory(this.listViewHeaderStatusMessage, this.isSplashActive, this.model, this.listViewAdapter, this.listView, this.applicationList).getHandler();

		this.loadLocalization();
	}

	/**
	 * 
	 * Private methods
	 * 
	 */

	/**
	 * Loads application's different typeface
	 */
	private void loadAppTypefaces() {
		this.typeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
		this.typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
		this.typefaceItalic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Italic.ttf");
	}

	/**
	 * Loads screen components
	 */
	private void loadLayoutComponents() {

		this.listView = (AwesomeListView)findViewById(R.id.lvLista);

		// ListView header status message
		this.listViewHeaderStatusMessage = (TextView)findViewById(R.id.lblStatus);
		listViewHeaderStatusMessage.setTypeface(this.typeface);
		listViewHeaderStatusMessage.setText(R.string.location_searching);

		// ListView header status image
		this.listViewHeaderImage = (ImageView)findViewById(R.id.imgMap);
		listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);
	}

	private void setClickListenersToButtons() {
		// User profile button
		Button userProfileButton = (Button)findViewById(R.id.btnProfile);
		userProfileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapplasActivity.this, UserForm.class);

				User usr = model.currentUser;

				if(usr != null) {
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTID_ID, usr.getId());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTNAME_ID, usr.getName());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTLASTNAME_ID, usr.getLastname());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTGENDER_ID, usr.getGender());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTBIRTHDATE_ID, usr.getBirthdate());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTLOGIN_ID, usr.getLogin());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTPASS_ID, usr.getPassword());
					intent.putExtra(Constants.SYNESTH_LOGIN_TXTEMAIL_ID, usr.getEmail());
				}

				MapplasActivity.this.startActivityForResult(intent, Constants.SYNESTH_USER_ID);
			}
		});

		// Notifications button
		Button notificationsButton = (Button)findViewById(R.id.btnNotifications);
		notificationsButton.setTypeface(this.typeface);
		notificationsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapplasActivity.this, AppNotifications.class);
				MapplasActivity.this.startActivity(intent);
			}
		});
	}

	private void loadApplicationsListView() {
		// Add header to list
		LinearLayout listViewHeader = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.ptr_header, null);
		RelativeLayout headerLayout = (RelativeLayout)listViewHeader.findViewById(R.id.llInnerPtr);
		TextView headerTV = (TextView)listViewHeader.findViewById(R.id.lblAction);
		ImageView headerIV = (ImageView)listViewHeader.findViewById(R.id.ivImage);

		this.listView.InsertHeader(listViewHeader, headerLayout, headerTV, headerIV, this.getResources().getDrawable(R.drawable.ic_pulltorefresh_arrow), this.getResources().getDrawable(R.drawable.ic_refresh_photo), this.getResources().getString(R.string.ptr_pull_to_refresh), this.getResources().getString(R.string.ptr_pull_to_refresh), this.getResources().getString(R.string.ptr_release_to_refresh), this.getResources().getString(R.string.ptr_refreshing));

		// Set release header listener
		this.listView.setOnReleasehHeaderListener(new AwesomeListView.OnRelease() {

			@Override
			public void onRelease() {
				try {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

				} catch (Exception e) {
					Log.i(this.getClass().getSimpleName(), "SynesthActivity.onCreate: " + e);
				}
			}
		});

		// Set adapter
		this.listViewAdapter = new LocalizationAdapter(this, R.layout.rowloc, model.localizations);
		this.listView.setAdapter(listViewAdapter);

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
						if(mDebug) {
							textoToast = "onStatusChanged: " + provider + ", " + status + ", " + extras;
							Toast.makeText(getBaseContext(), textoToast, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onProviderEnabled(String provider) {
						if(mDebug) {
							textoToast = "onProviderEnabled: " + provider;
							Toast.makeText(getBaseContext(), textoToast, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onProviderDisabled(String provider) {
						if(mDebug) {
							textoToast = "onProviderDisabled: " + provider;
							Toast.makeText(getBaseContext(), textoToast, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onLocationChanged(Location location) {

						listViewHeaderStatusMessage.setText(R.string.location_done);
						listViewHeaderImage.setBackgroundResource(R.drawable.icon_map);

						model.currentLocation = location.getLatitude() + "," + location.getLongitude();

						try {
							locationManager.removeUpdates(locationListener);

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


	//
	// public static Typeface getTypeFace() {
	// return MapplasActivity.mTypeface;
	// }
	//
	// public static SharedPreferences getAppPreferences() {
	// return MapplasActivity.mPreferences;
	// }
	//
	// public static LocalizationAdapter getLocalizationAdapter() {
	// return MapplasActivity.mListAdapter;
	// }
	//
	// public static DrawableBackgroundDownloader getDbd() {
	// return MapplasActivity.mDbd;
	// }
	//



	/* Methods */
	// public static Button GetButtonNotifications() {
	// return mButtonNotifications;
	// }
	//
	// public static SuperModel GetModel() {
	// return model;
	// }
	//

	//
	// public static Localization GetLocalizationById(long id) {
	// if(model != null) {
	// for(int i = 0; i < model.localizations.size(); i++) {
	// if(model.localizations.get(i).getId() == id) {
	// return model.localizations.get(i);
	// }
	// }
	// }
	//
	// return null;
	// }

	// private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	//
	// @Override
	// protected void onPostExecute(String[] result) {
	// // mListItems.addFirst("Added after refresh...");
	// // Call onRefreshComplete when the list has been refreshed.
	//
	// try {
	// mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
	// 0, 0, mLocationListener);
	//
	// } catch (Exception e1) {
	// Log.i(getClass().getSimpleName(), "GetDataTask.onPostExecute: " + e1);
	// }
	//
	// super.onPostExecute(result);
	// }
	//
	// @Override
	// protected String[] doInBackground(Void... arg0) {
	//
	// return null;
	// }
	// }

}

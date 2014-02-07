package com.mapplas.utils.network.async_tasks;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import app.mapplas.com.R;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.language.LanguageDialogCreator;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.network.NetworkConnectionChecker;
import com.mapplas.utils.network.connectors.AppGetterConnector;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.visual.custom_views.RobotoButton;
import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;

public class AppGetterTask extends AsyncTask<Object, Void, String> implements LanguageDialogInterface {

	private Context context;

	private SuperModel model;

	private AppAdapter listViewAdapter;

	private RefreshableListView listView;

	private ArrayList<ApplicationInfo> appsInstalledInfo;

	private static Semaphore semaphore = new Semaphore(1);

	private MapplasActivity mainActivity;

	private int retries;

	// Params
	private Location location;

	private boolean resetPagination;

	public AppGetterTask(Context context, SuperModel model, AppAdapter listViewAdapter, RefreshableListView listView, ArrayList<ApplicationInfo> applicationList, MapplasActivity mainActivity, int retries) {
		super();
		this.context = context;
		this.model = model;
		this.listViewAdapter = listViewAdapter;
		this.listView = listView;
		this.appsInstalledInfo = applicationList;
		this.mainActivity = mainActivity;
		this.retries = retries;
	}

	@Override
	protected String doInBackground(Object... params) {

		this.location = (Location)params[0];
		this.resetPagination = (Boolean)params[1];

		try {
			semaphore.acquire();
			if(AppRequestBeingDoneSingleton.requestBeingDone) {
				semaphore.release();
				return Constants.APP_OBTENTION_ERROR_GENERIC;
			}

			AppRequestBeingDoneSingleton.requestBeingDone = true;
			semaphore.release();
		} catch (Exception exc) {
			return Constants.APP_OBTENTION_ERROR_GENERIC;
		}

		String code = AppGetterConnector.request(this.location, this.model, this.resetPagination, this.context, new LanguageSetter(this.context).getLanguageConstantFromPhone());

		try {
			semaphore.acquire();
			AppRequestBeingDoneSingleton.requestBeingDone = false;
			semaphore.release();
		} catch (Exception exc) {
			return Constants.APP_OBTENTION_ERROR_GENERIC;
		}
		return code;
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);

		if(response.equals(Constants.APP_OBTENTION_OK)) {
			this.checkLanguage();
		}
		// Generic error or socket error
		else if(this.retries <= Constants.NUMBER_OF_REQUEST_RETRIES) {
			this.retries = this.retries + 1;
			new AppGetterTask(this.context, this.model, this.listViewAdapter, this.listView, this.appsInstalledInfo, this.mainActivity, this.retries).execute(this.location, this.resetPagination);
		}
		else {
			NetworkConnectionChecker networkConnChecker = new NetworkConnectionChecker();
			networkConnChecker.getNetworkErrorToast(this.context, R.string.connection_error).show();

			this.mainActivity.finish();
		}
	}

	private void afterLanguageCheck() {
		RelativeLayout navigationBar = (RelativeLayout)((MapplasActivity)this.context).findViewById(R.id.navigation_bar);
		navigationBar.setVisibility(View.VISIBLE);

		RelativeLayout radarLayout = (RelativeLayout)((MapplasActivity)this.context).findViewById(R.id.radar_layout);
		radarLayout.setVisibility(View.GONE);
		
		this.listView.setVisibility(View.VISIBLE);

		// Profile button animation
		RobotoButton profileNavBarButton = (RobotoButton)((MapplasActivity)this.context).findViewById(R.id.btnProfile);
		RobotoButton searchNavBarButton = (RobotoButton)((MapplasActivity)this.context).findViewById(R.id.btnSearch);
		if(profileNavBarButton.getVisibility() == View.GONE) {
			Animation myFadeInAnimation = AnimationUtils.loadAnimation(this.context, R.anim.alpha);

			profileNavBarButton.setVisibility(View.VISIBLE);
			profileNavBarButton.startAnimation(myFadeInAnimation);
			
			searchNavBarButton.setVisibility(View.VISIBLE);
			searchNavBarButton.startAnimation(myFadeInAnimation);
		}

		if(this.listViewAdapter != null) {

			// Get app list from telf.
			final PackageManager pm = this.context.getPackageManager();
			this.appsInstalledInfo = (ArrayList<ApplicationInfo>)pm.getInstalledApplications(PackageManager.GET_ACTIVITIES);

			for(int i = 0; i < this.model.appList().size(); i++) {
				ApplicationInfo ai = findApplicationInfo(this.model.appList().get(i).getId());
				if(ai != null) {
					this.model.appList().get(i).setInternalApplicationInfo(ai);
				}
			}
			this.listViewAdapter.SLEEP = false;
			this.listView.updateAdapter(this.context, this.model, this.appsInstalledInfo);
			this.listView.completeRefreshing();
		}
		
//		this.mainActivity.requestGeoFences();
	}

	private ApplicationInfo findApplicationInfo(String id) {
		ApplicationInfo ret = null;

		for(int i = 0; i < this.appsInstalledInfo.size(); i++) {
			ApplicationInfo ai = this.appsInstalledInfo.get(i);
			if(ai.packageName != null && id.contentEquals(ai.packageName)) {
				ret = this.appsInstalledInfo.get(i);
			}
		}
		return ret;
	}

	private void checkLanguage() {
		// First launch language dialog

		SharedPreferences sharedPrefs = this.context.getSharedPreferences(Constants.MAPPLAS_SHARED_PREFS, Context.MODE_PRIVATE);
		boolean firstboot = sharedPrefs.getBoolean(Constants.MAPPLAS_SHARED_PREFS_LANGUAGE_DIALOG_SHOWN, true);

		if(firstboot && this.model.isFromBasqueCountry()) {
			// Show language dialog
			sharedPrefs.edit().putBoolean(Constants.MAPPLAS_SHARED_PREFS_LANGUAGE_DIALOG_SHOWN, false).commit();
			new LanguageDialogCreator(this.context, this, this).createLanguageListDialog();
		}
		else {
			new LanguageSetter(this.mainActivity).setLanguageToApp(((MapplasApplication)this.context.getApplicationContext()).getLanguage());
			this.afterLanguageCheck();
		}
	}

	@Override
	public void onDialogEnglishLanguageClick() {
		((MapplasApplication)this.context.getApplicationContext()).setLanguage(Constants.ENGLISH);
		updateLanguage(((MapplasApplication)this.context.getApplicationContext()).getLanguage());
		this.afterLanguageCheck();
	}

	@Override
	public void onDialogSpanishLanguageClick() {
		((MapplasApplication)this.context.getApplicationContext()).setLanguage(Constants.SPANISH);
		updateLanguage(((MapplasApplication)this.context.getApplicationContext()).getLanguage());
		this.afterLanguageCheck();
	}

	@Override
	public void onDialogBasqueLanguageClick() {
		((MapplasApplication)this.context.getApplicationContext()).setLanguage(Constants.BASQUE);
		updateLanguage(((MapplasApplication)this.context.getApplicationContext()).getLanguage());
		this.afterLanguageCheck();
	}

	private void updateLanguage(String language) {
		new LanguageSetter(this.mainActivity).setLanguageToApp(language);
	}

}

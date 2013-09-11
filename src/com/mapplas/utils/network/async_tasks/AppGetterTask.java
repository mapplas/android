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
import android.widget.RelativeLayout;
import app.mapplas.com.R;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.Constants;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.language.LanguageDialogCreator;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.network.connectors.AppGetterConnector;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.third_party.RefreshableListView;
import com.mapplas.utils.visual.dialogs.LanguageDialogInterface;

public class AppGetterTask extends AsyncTask<Object, Void, Location> implements LanguageDialogInterface {

	private Context context;

	private SuperModel model;

	private AppAdapter listViewAdapter;

	private RefreshableListView listView;
	
	private ArrayList<ApplicationInfo> appsInstalledInfo;
	
	private static Semaphore semaphore = new Semaphore(1);

	private static boolean occupied = false;
		
	private MapplasActivity mainActivity;
		
	public AppGetterTask(Context context, SuperModel model, AppAdapter listViewAdapter, RefreshableListView listView, ArrayList<ApplicationInfo> applicationList, MapplasActivity mainActivity) {
		super();
		this.context = context;
		this.model = model;
		this.listViewAdapter = listViewAdapter;
		this.listView = listView;
		this.appsInstalledInfo = applicationList;
		this.mainActivity = mainActivity;
	}

	@Override
	protected Location doInBackground(Object... params) {
		// Set singleton to true
		AppRequestBeingDoneSingleton.requestBeingDone = true;
		
		try {
			semaphore.acquire();
			if(occupied) {
				semaphore.release();
				return null;
			}

			occupied = true;
			semaphore.release();
		} catch (Exception exc) {
//			Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
			return null;
		}

		Location location = (Location)params[0];

		try {
			AppGetterConnector.request(location, this.model, (Boolean)params[1], context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			semaphore.acquire();
			occupied = false;
			semaphore.release();
		} catch (Exception exc) {
//			Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
			return null;
		}

		return location;
	}

	@Override
	protected void onPostExecute(Location location) {
		super.onPostExecute(location);
		
		this.checkLanguage();
	}
	
	private void afterLanguageCheck() {
		RelativeLayout radarLayout = (RelativeLayout)((MapplasActivity)this.context).findViewById(R.id.radar_layout);
		radarLayout.setVisibility(View.GONE);
		this.listView.setVisibility(View.VISIBLE);

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
		
		// Set singleton to false
		AppRequestBeingDoneSingleton.requestBeingDone = false;
				
		// Send app info to server
//		new AppInfoSenderTask(this.applicationList, location, this.activityManager, this.model.currentUser()).execute();
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

package com.mapplas.utils.network.async_tasks;

import java.util.List;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import app.mapplas.com.R;

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.connectors.AppGetterConnector;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.third_party.RefreshableListView;

public class AppGetterTask extends AsyncTask<Object, Void, Location> {

	private Context context;

	private SuperModel model;

	private AppAdapter listViewAdapter;

	private RefreshableListView listView;
	
	private List<ApplicationInfo> appsInstalledInfo;
	
	private static Semaphore semaphore = new Semaphore(1);

	private static boolean occupied = false;
	
	public AppGetterTask(Context context, SuperModel model, AppAdapter listViewAdapter, RefreshableListView listView, List<ApplicationInfo> applicationList) {
		super();
		this.context = context;
		this.model = model;
		this.listViewAdapter = listViewAdapter;
		this.listView = listView;
		this.appsInstalledInfo = applicationList;
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
			Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
			return null;
		}

		Location location = (Location)params[0];

		try {
			AppGetterConnector.request(location, this.model, (Boolean)params[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			semaphore.acquire();
			occupied = false;
			semaphore.release();
		} catch (Exception exc) {
			Log.d(this.getClass().getSimpleName(), "doInBackground", exc);
			return null;
		}

		return location;
	}

	@Override
	protected void onPostExecute(Location location) {
		super.onPostExecute(location);

		RelativeLayout radarLayout = (RelativeLayout)((MapplasActivity)this.context).findViewById(R.id.radar_layout);
		radarLayout.setVisibility(View.GONE);
		this.listView.setVisibility(View.VISIBLE);

		if(this.listViewAdapter != null) {
			
			// Get app list from telf.
			final PackageManager pm = this.context.getPackageManager();
			this.appsInstalledInfo = pm.getInstalledApplications(PackageManager.GET_ACTIVITIES);

			for(int i = 0; i < this.model.appList().size(); i++) {
				ApplicationInfo ai = findApplicationInfo(this.model.appList().get(i).getId());
				if(ai != null) {
					this.model.appList().get(i).setInternalApplicationInfo(ai);
				}
			}

			// Order app list
			this.model.appList().sort();
			
			this.listViewAdapter.addNewApps();
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
}

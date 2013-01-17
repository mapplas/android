package com.mapplas.app.async_tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import app.mapplas.com.R;

import com.mapplas.app.AwesomeListView;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.adapters.app.AppAdapter;
import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.model.JsonParser;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.infinite_scroll.InfiniteScrollManager;
import com.mapplas.utils.static_intents.AppAdapterSingleton;

public class AppGetterTask extends AsyncTask<Location, Void, Location> {

	private Context context;

	private SuperModel model;

	private AppAdapter listViewAdapter;

	private AwesomeListView listView;

	private List<ApplicationInfo> applicationList;
	
	private ActivityManager activityManager;
	
	private Button notificationsButton;

	private static Semaphore semaphore = new Semaphore(1);

	private static boolean occupied = false;

	public AppGetterTask(Context context, SuperModel model, AppAdapter listViewAdapter, AwesomeListView listView, List<ApplicationInfo> applicationList, ActivityManager activityManager, Button notificationsButton) {
		super();
		this.context = context;
		this.model = model;
		this.listViewAdapter = listViewAdapter;
		this.listView = listView;
		this.applicationList = applicationList;
		this.activityManager = activityManager;
		this.notificationsButton = notificationsButton;
	}

	@Override
	protected Location doInBackground(Location... params) {
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

		Location location = params[0];

		String uid = "0";
		String serverResponse = "";

		if(this.model.currentUser() != null) {
			uid = String.valueOf(this.model.currentUser().getId());
		}

		SharedPreferences sharedPreferences = this.context.getSharedPreferences("synesth", Context.MODE_PRIVATE);
		String strLastNotifications = sharedPreferences.getString(Constants.SYNESTH_LAST_NOTIFICATIONS, "");

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "ipc_locations.php?l=" + location.getLatitude() + "," + location.getLongitude() + "&uid=" + uid + "&ln=" + strLastNotifications + "&v=" + Constants.SYNESTH_VERSION + "&p=" + location.getAccuracy());

		try {
			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());

				// Comprobamos que el parser funciona correctamente
				JsonParser jp = new JsonParser(this.context);
				jp.parseApps(serverResponse, this.model, false);
			}

		} catch (Exception exc) {
			Log.i(getClass().getSimpleName(), "ObtainLocalizationTask.doInBackGround: " + exc);
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

		// Get first X applications
		ArrayList<App> appList = new ArrayList<App>();
		int maxIndex = InfiniteScrollManager.NUMBER_OF_APPS;

		if(this.model.appList().size() < InfiniteScrollManager.NUMBER_OF_APPS) {
			maxIndex = this.model.appList().size();
		}
		for(int i = 0; i < maxIndex; i++) {
			appList.add(this.model.appList().get(i));
		}

		this.listViewAdapter = new AppAdapter(this.context, this.listView, this.model, appList);
		this.listView.setAdapter(this.listViewAdapter);
		AppAdapterSingleton.appAdapter = this.listViewAdapter;
		this.listView.setVisibility(View.VISIBLE);

		RelativeLayout radarLayout = (RelativeLayout)((MapplasActivity)this.context).findViewById(R.id.radar_layout);
		radarLayout.setVisibility(View.GONE);


		if(this.listViewAdapter != null) {
			
			// Get app list from telf.
			final PackageManager pm = this.context.getPackageManager();
			this.applicationList = pm.getInstalledApplications(PackageManager.GET_ACTIVITIES);

			for(int i = 0; i < maxIndex; i++) {
				ApplicationInfo ai = findApplicationInfo(this.model.appList().get(i).getAppName());
				if(ai != null) {
					this.model.appList().get(i).setInternalApplicationInfo(ai);
				}
			}

			this.listViewAdapter.notifyDataSetChanged();

			this.listView.finishRefresing();
		}
		
		// Insert notifications into database
		new NotificationDatabaseInserterTask(this.model, this.context, this.notificationsButton).execute();

		// Send app info to server
		new AppInfoSenderTask(this.applicationList, location, this.activityManager, this.model.currentUser()).execute();
	}

	private ApplicationInfo findApplicationInfo(String id) {
		ApplicationInfo ret = null;

		for(int i = 0; i < this.applicationList.size(); i++) {
			ApplicationInfo ai = this.applicationList.get(i);
			if(id.contentEquals(ai.packageName)) {
				ret = this.applicationList.get(i);
			}
		}
		return ret;
	}
}

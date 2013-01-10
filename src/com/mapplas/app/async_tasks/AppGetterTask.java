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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
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

public class AppGetterTask extends AsyncTask<Location, Void, Void> {

	private Context context;

	private SuperModel model;

	private AppAdapter listViewAdapter;

	private AwesomeListView listView;

	private List<ApplicationInfo> applicationList;

	private static Semaphore semaphore = new Semaphore(1);

	private static boolean occupied = false;

	public AppGetterTask(Context context, SuperModel model, AppAdapter listViewAdapter, AwesomeListView listView, List<ApplicationInfo> applicationList) {
		super();
		this.context = context;
		this.model = model;
		this.listViewAdapter = listViewAdapter;
		this.listView = listView;
		this.applicationList = applicationList;
	}

	@Override
	protected Void doInBackground(Location... params) {

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
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "ipc_locations.php?l=" + location.getLatitude() + "," + location.getLongitude() 
			+ "&uid=" + uid 
			+ "&ln=" + strLastNotifications 
			+ "&v=" + Constants.SYNESTH_VERSION
			+ "&p=" + location.getAccuracy());

		try {
			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());

				// Comprobamos que el parser funciona correctamente
				JsonParser jp = new JsonParser(this.context);
				jp.parseApps(serverResponse, this.model, false);
			}
			else {
				if(MapplasActivity.mDebug) {
					String textoToast = "ESTADO: " + rp.getStatusLine().getStatusCode();
					Toast.makeText(context, textoToast, Toast.LENGTH_LONG).show();
				}
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

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
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

		final RelativeLayout mainLayout = (RelativeLayout)((MapplasActivity)this.context).findViewById(R.id.layoutMain);
		final LinearLayout mainScreenContentLayout = (LinearLayout)((MapplasActivity)this.context).findViewById(R.id.lytContent);
		final LinearLayout splashLayout = (LinearLayout)((MapplasActivity)this.context).findViewById(R.id.id_splash);
		final Button notificationsButton = (Button)((MapplasActivity)this.context).findViewById(R.id.btnNotifications);

		if(MapplasActivity.isSplashActive) {
			final Animation fadeOutAnimation = new AlphaAnimation(1, 0);
			fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); // and
			// this
			fadeOutAnimation.setStartOffset(0);
			fadeOutAnimation.setDuration(500);

			final Animation fadeInAnimation = new AlphaAnimation(0, 1);
			fadeInAnimation.setInterpolator(new AccelerateInterpolator()); // and
			// this
			fadeInAnimation.setStartOffset(0);
			fadeInAnimation.setDuration(500);

			fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mainLayout.removeView(splashLayout);
					mainScreenContentLayout.startAnimation(fadeInAnimation);
				}
			});

			mainLayout.startAnimation(fadeOutAnimation);
			MapplasActivity.isSplashActive = false;
		}

		if(this.listViewAdapter != null) {

			for(int i = 0; i < maxIndex; i++) {
				ApplicationInfo ai = findApplicationInfo(this.model.appList().get(i).getAppName());
				if(ai != null) {
					this.model.appList().get(i).setInternalApplicationInfo(ai);
				}
			}

			if(this.model.notificationList().size() > 0) {
				notificationsButton.setText(this.model.notificationList().size() + "");
				notificationsButton.setBackgroundResource(R.drawable.menu_notifications_number_button);
			}
			else {
				notificationsButton.setText("");
				notificationsButton.setBackgroundResource(R.drawable.menu_notifications_button);
			}

			this.listViewAdapter.notifyDataSetChanged();

			this.listView.finishRefresing();
		}
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

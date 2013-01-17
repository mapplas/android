package com.mapplas.app.async_tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.mapplas.model.Constants;
import com.mapplas.model.User;

public class AppInfoSenderTask extends AsyncTask<Void, Void, Void> {

	private List<ApplicationInfo> applicationList;

	private Location location;
	
	private ActivityManager activityManager;
	
	private User currentUser;

	public AppInfoSenderTask(List<ApplicationInfo> applicationList, Location location, ActivityManager activityManager, User currentUser) {
		this.applicationList = applicationList;
		this.location = location;
		this.activityManager = activityManager;
		this.currentUser = currentUser;
	}

	@Override
	protected Void doInBackground(Void... params) {
		ArrayList<String> installedAppsPackage = new ArrayList<String>();
		for(ApplicationInfo currentApp : this.applicationList) {
			if(!this.isSystemPackage(currentApp)) {
				installedAppsPackage.add(currentApp.packageName);
			}
		}
		
        List<RecentTaskInfo> task = this.activityManager.getRecentTasks(5,0);
        String lastUsedApps = task.get(0).baseIntent.getPackage();

		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://" + Constants.SYNESTH_SERVER + ":" + Constants.SYNESTH_SERVER_PORT + Constants.SYNESTH_SERVER_PATH + "ipc_userInfo.php");
	
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("loc", this.location.getLatitude() + "," + this.location.getLongitude()));
		nameValuePairs.add(new BasicNameValuePair("apps", installedAppsPackage.toString()));
		nameValuePairs.add(new BasicNameValuePair("last", lastUsedApps));
		nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(this.currentUser.getId())));

		String serverResponse = "";
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse rp = hc.execute(post);

			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				serverResponse = EntityUtils.toString(rp.getEntity());
			}
			else {
				Log.e(this.getClass().getSimpleName(), serverResponse);
			}
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), e.toString());
		}
		return null;
	}

	private boolean isSystemPackage(ApplicationInfo appInfo) {
		return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
	}

}

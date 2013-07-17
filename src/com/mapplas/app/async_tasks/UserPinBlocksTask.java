package com.mapplas.app.async_tasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mapplas.app.adapters.user.UserAppAdapter;
import com.mapplas.model.User;
import com.mapplas.utils.network.connectors.UserPinBlocksConnector;
import com.mapplas.utils.network.mappers.JsonToBlockedAppsMapper;
import com.mapplas.utils.network.mappers.JsonToPinnedAppsMapper;

public class UserPinBlocksTask extends AsyncTask<Void, Void, String> {

	private ListView listView;

	private Context context;

	private int textViewResourceId;

	private LinearLayout refreshListBackgroundFooter;
	
	private User user;
		
	public UserPinBlocksTask(User user, ListView listView, Context context, int textViewResourceId, LinearLayout refreshListBackgroundFooter) {
		super();
		this.user = user;
		this.listView = listView;
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.refreshListBackgroundFooter = refreshListBackgroundFooter;
	}

	@Override
	protected String doInBackground(Void... params) {
		String response = "";
		try {
			response = UserPinBlocksConnector.request(String.valueOf(this.user.getId()));
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "Get pin and blocks", e);
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		try {
			JSONObject jsonResult = new JSONObject(result);
			
			this.user.setPinnedApps(new JsonToPinnedAppsMapper().map(jsonResult.getJSONArray("pinned")));
			this.user.setBlockedApps(new JsonToBlockedAppsMapper().map(jsonResult.getJSONArray("blocked")));
			
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "error parsing", e);
		}

		this.listView.removeFooterView(this.refreshListBackgroundFooter);

		UserAppAdapter appAdapter = new UserAppAdapter(this.context, this.textViewResourceId, this.user.pinnedApps(), UserAppAdapter.PINUP, this.user, true);
		this.listView.setAdapter(appAdapter);
	}
	
}
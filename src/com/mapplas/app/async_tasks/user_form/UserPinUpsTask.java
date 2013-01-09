package com.mapplas.app.async_tasks.user_form;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mapplas.app.adapters.user.UserAppAdapter;
import com.mapplas.model.JsonParser;
import com.mapplas.model.User;
import com.mapplas.utils.NetRequests;

public class UserPinUpsTask extends AsyncTask<Void, Void, String> {

	private JsonParser parser;

	private ListView listView;

	private Context context;

	private int textViewResourceId;

	private LinearLayout refreshListBackgroundFooter;
	
	private User user;
	
	private String currentLocation;
	
	public UserPinUpsTask(User user, String currentLocation, JsonParser parser, ListView listView, Context context, int textViewResourceId, LinearLayout refreshListBackgroundFooter) {
		super();
		this.user = user;
		this.currentLocation = currentLocation;
		this.parser = parser;
		this.listView = listView;
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.refreshListBackgroundFooter = refreshListBackgroundFooter;
	}

	@Override
	protected String doInBackground(Void... params) {
		String response = "";
		try {
			response = NetRequests.UserPinUpsRequest(String.valueOf(this.user.getId()));
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "Get PinUps", e);
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		// Parse result and insert into user
		this.user.setPinnedApps(this.parser.parseApps(result));

		this.listView.removeFooterView(this.refreshListBackgroundFooter);

		UserAppAdapter appAdapter = new UserAppAdapter(this.context, this.textViewResourceId, this.user.pinnedApps(), UserAppAdapter.PINUP, this.user, this.currentLocation, true);
		this.listView.setAdapter(appAdapter);
	}
	
	
}

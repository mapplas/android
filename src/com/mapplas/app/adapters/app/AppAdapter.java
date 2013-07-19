package com.mapplas.app.adapters.app;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import app.mapplas.com.R;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.async_tasks.AppGetterTask;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.third_party.RefreshableListView;

public class AppAdapter extends EndlessAdapter {
				
	private Context context;
	
	private SuperModel model;
	
	private RefreshableListView list;
		
	private ArrayList<ApplicationInfo> applicationList;
	
//	private String TAG = "AppAdapter";
	
	public boolean SLEEP = false;
		
	public AppAdapter(Context context, RefreshableListView list, SuperModel model, ArrayList<ApplicationInfo> applicationList) {
		super(new AppArrayAdapter(context, R.layout.rowloc, android.R.id.text1, model.appList().getAppList(), list, model));

		this.context = context;
		this.model = model;
		this.list = list;
		this.applicationList = applicationList;
	}

	/**
	 * 
	 * Endless adapter
	 * 
	 */

	@Override
	protected View getPendingView(ViewGroup parent) {
		View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row, null);
		this.startProgressAnimation(row);
		return (row);
	}

	private void startProgressAnimation(View view) {
		ImageView loadingImage = (ImageView)view.findViewById(R.id.throbber);
		if(loadingImage != null) {
			RotateAnimation rotate = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setDuration(800);
			rotate.setRepeatMode(Animation.RESTART);
			rotate.setRepeatCount(Animation.INFINITE);
			rotate.setInterpolator(new LinearInterpolator());
			loadingImage.startAnimation(rotate);
		}
	}

	@Override
	protected void appendCachedData() {
		this.notifyDataSetChanged();
	}

	@Override
	protected boolean cacheInBackground() throws Exception {
		if (this.model.moreData() && !AppRequestBeingDoneSingleton.requestBeingDone) {
			this.SLEEP = true;
			new AppGetterTask(this.context, this.model, this, this.list, this.applicationList).execute(this.model.getLocation(), false);
			// WAIT UNTIL APP GETTER TASK FINISHES
			while(this.SLEEP) {
				Thread.sleep(200);
			}
		}
		return this.model.moreData();
	}

	@SuppressWarnings("unchecked")
	public void clear() {
		ArrayAdapter<App> adapter = (ArrayAdapter<App>)getWrappedAdapter();
		adapter.clear();
	}

	@SuppressWarnings("unchecked")
	public void add(App newApp) {
		ArrayAdapter<App> adapter = (ArrayAdapter<App>)getWrappedAdapter();
		adapter.add(newApp);
	}

	@SuppressWarnings("unchecked")
	public void remove(App appToRemove) {
		ArrayAdapter<App> adapter = (ArrayAdapter<App>)getWrappedAdapter();
		adapter.remove(appToRemove);
	}

}

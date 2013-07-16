package com.mapplas.app.adapters.app;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
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
import com.mapplas.app.async_tasks.AppGetterTask;
import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.third_party.RefreshableListView;

public class AppAdapter extends EndlessAdapter {
				
	private Context context;
	
	private SuperModel model;
	
	private RefreshableListView list;
		
	private List<ApplicationInfo> applicationList;
		
	public AppAdapter(Context context, RefreshableListView list, SuperModel model, List<ApplicationInfo> applicationList) {
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
		Log.d("TAG", "getPendingView");
		View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row, null);
		this.startProgressAnimation(row);
		return (row);
	}

	private void startProgressAnimation(View view) {
		Log.d("TAG", "startProgressAnimation");

		ImageView loadingImage = (ImageView)view.findViewById(R.id.throbber);
		if(loadingImage != null) {
			RotateAnimation rotate = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setDuration(1200);
			rotate.setRepeatMode(Animation.RESTART);
			rotate.setRepeatCount(Animation.INFINITE);
			rotate.setInterpolator(new LinearInterpolator());
			loadingImage.startAnimation(rotate);
		}
	}

	@Override
	protected void appendCachedData() {
		Log.d("TAG", "appendCachedData");

		this.notifyDataSetChanged();
	}

	@Override
	protected boolean cacheInBackground() throws Exception {
		Log.d("TAG", "cacheInBackground");

		if (this.model.moreData() && !AppRequestBeingDoneSingleton.requestBeingDone) {
			Log.d("TAG", "REQUEST");

			new AppGetterTask(this.context, this.model, this, this.list, this.applicationList).execute(this.model.getLocation(), false);
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
	
	public void addNewApps() {
		this.clear();
		Log.d("TAG", "addNewApps");

		for (int i=0; i<this.model.appList().size(); i++) {
			this.add(this.model.appList().get(i));
		}
	}

}

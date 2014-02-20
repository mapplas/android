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

import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.model.App;
import com.mapplas.model.SuperModel;
import com.mapplas.utils.network.async_tasks.AppGetterTask;
import com.mapplas.utils.searcher.SearchManager;
import com.mapplas.utils.static_intents.AppRequestBeingDoneSingleton;
import com.mapplas.utils.third_party.EndlessAdapter;
import com.mapplas.utils.visual.helpers.AppGetterTaskViewsContainer;

public class AppAdapter extends EndlessAdapter {

	private Context context;

	private SuperModel model;

	private ArrayList<ApplicationInfo> applicationList;

	private MapplasActivity mainActivity;

	private AppGetterTaskViewsContainer appGetterTaskViewsContainer;

	public boolean SLEEP = false;

	public AppAdapter(Context context, SuperModel model, ArrayList<ApplicationInfo> applicationList, MapplasActivity mainActivity, AppGetterTaskViewsContainer appGetterTaskViewsContainer) {
		super(new AppArrayAdapter(context, R.layout.rowloc, android.R.id.text1, model.appList().getAppList(), appGetterTaskViewsContainer.listView, model));

		this.context = context;
		this.model = model;
		this.applicationList = applicationList;
		this.mainActivity = mainActivity;
		this.appGetterTaskViewsContainer = appGetterTaskViewsContainer;
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
		return row;
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

		if(this.model.moreData() && !AppRequestBeingDoneSingleton.requestBeingDone) {
			this.SLEEP = true;
			boolean restart_pagination = false;
			int requestNumber = 0;

			new AppGetterTask(this.context, this.model, this.applicationList, this.mainActivity, requestNumber, SearchManager.APP_REQUEST_TYPE_BEING_DONE, appGetterTaskViewsContainer).execute(this.model.getLocation(), restart_pagination, SearchManager.APP_REQUEST_ENTITY_BEING_DONE);
			// WAIT UNTIL APP GETTER TASK FINISHES
			while (this.SLEEP) {
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

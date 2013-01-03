package com.mapplas.app.adapters.app;

import java.util.ArrayList;

import android.content.Context;
import android.os.SystemClock;
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
import com.mapplas.app.AwesomeListView;
import com.mapplas.model.App;
import com.mapplas.model.User;
import com.mapplas.utils.infinite_scroll.InfiniteScrollManager;

public class AppAdapter extends EndlessAdapter {
	
	private static int SLEEP_MILISECONDS = 2000;

	private ArrayList<App> modelData;

	private InfiniteScrollManager scrollManager;

	private int loadedListCount = 1;

	public AppAdapter(Context context, AwesomeListView list, int layout, int textViewResourceId, ArrayList<App> modelItems, ArrayList<App> appList, String currentLocation, String currentDescriptiveGeoLoc, User currentUser) {
		super(new AppArrayAdapter(context, layout, textViewResourceId, appList, list, currentLocation, currentDescriptiveGeoLoc, currentUser));

		this.modelData = modelItems;
		this.scrollManager = new InfiniteScrollManager(this.modelData);
	}

	/**
	 * 
	 * Endless adapter
	 * 
	 */

	@Override
	protected View getPendingView(ViewGroup parent) {
		View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row, null);
		startProgressAnimation(row);
		return (row);
	}

	void startProgressAnimation(View view) {
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
		if(getWrappedAdapter().getCount() < this.modelData.size()) {
			@SuppressWarnings("unchecked")
			ArrayAdapter<App> adapter = (ArrayAdapter<App>)getWrappedAdapter();
			
//			int count = this.loadedListCount;
//			int maxCount = this.scrollManager.getMaxCount();
//			int resto = this.scrollManager.getRest();

			if(this.loadedListCount == this.scrollManager.getMaxCount() - 1 && !this.scrollManager.isRestZero()) {
				int rest = this.scrollManager.getRest();
				for(int i = InfiniteScrollManager.NUMBER_OF_APPS * this.loadedListCount; i <= (InfiniteScrollManager.NUMBER_OF_APPS * this.loadedListCount) + rest - 1; i++) {
					adapter.add(this.modelData.get(i));
				}
			}
			else {
				for(int i = InfiniteScrollManager.NUMBER_OF_APPS * this.loadedListCount; i <= (InfiniteScrollManager.NUMBER_OF_APPS * this.loadedListCount) + (InfiniteScrollManager.NUMBER_OF_APPS - 1); i++) {
					adapter.add(this.modelData.get(i));
				}
			}
			this.loadedListCount++;
		}
	}

	@Override
	protected boolean cacheInBackground() throws Exception {
		SystemClock.sleep(AppAdapter.SLEEP_MILISECONDS); // pretend to do work
		return getWrappedAdapter().getCount() < this.modelData.size();
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

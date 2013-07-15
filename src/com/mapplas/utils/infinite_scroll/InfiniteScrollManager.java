package com.mapplas.utils.infinite_scroll;

import java.util.ArrayList;

import com.mapplas.model.App;
import com.mapplas.model.SuperModel;

public class InfiniteScrollManager {

	public static int NUMBER_OF_APPS = 20;

	private ArrayList<App> apps;

	public InfiniteScrollManager(ArrayList<App> apps) {
		this.apps = apps;
	}
	
	public InfiniteScrollManager() {
	}

	public int getMaxCount() {
		if(this.apps.size() % InfiniteScrollManager.NUMBER_OF_APPS == 0) {
			return this.apps.size() / InfiniteScrollManager.NUMBER_OF_APPS;
		}
		else {
			return (this.apps.size() / InfiniteScrollManager.NUMBER_OF_APPS) + 1;
		}
	}

	public boolean isRestZero() {
		if(this.apps.size() % InfiniteScrollManager.NUMBER_OF_APPS == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public int getRest() {
		return this.apps.size() % InfiniteScrollManager.NUMBER_OF_APPS;
	}
	
	public ArrayList<App> getFirstXNumberOfApps(SuperModel model) {
		// Get first X applications
		ArrayList<App> appList = new ArrayList<App>();
		int maxIndex = InfiniteScrollManager.NUMBER_OF_APPS;
		
		if(model.appList().size() < InfiniteScrollManager.NUMBER_OF_APPS) {
			maxIndex = model.appList().size();
		}
		for(int i = 0; i < maxIndex; i++) {
			appList.add(model.appList().get(i));
		}
		
		return appList;
	}

}

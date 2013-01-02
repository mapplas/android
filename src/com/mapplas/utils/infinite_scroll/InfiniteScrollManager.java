package com.mapplas.utils.infinite_scroll;

import java.util.ArrayList;

import com.mapplas.model.App;

public class InfiniteScrollManager {

	public static int NUMBER_OF_APPS = 10;

	private ArrayList<App> apps;

	public InfiniteScrollManager(ArrayList<App> apps) {
		this.apps = apps;
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

}

package com.mapplas.utils.infinite_scroll;

import java.util.ArrayList;

import com.mapplas.model.App;

public class InfiniteScrollManager {

	public static int NUMBER_OF_APPS = 10;

	private ArrayList<App> apps;

	public InfiniteScrollManager(ArrayList<App> apps) {
		this.apps = apps;
	}

	public ArrayList<App> getMore(int count) {
		ArrayList<App> appsToReturn = new ArrayList<App>();
		int pos = 0;

		while (pos <= (count * InfiniteScrollManager.NUMBER_OF_APPS) - 1) {
			if(count * InfiniteScrollManager.NUMBER_OF_APPS > this.apps.size()) {
				// TODO: THINK!!
			}
			else {
				appsToReturn.add(this.apps.get(pos));
			}
			pos++;
		}

		return appsToReturn;
	}

	public void getSobras(int count) {
		int module = this.apps.size() % InfiniteScrollManager.NUMBER_OF_APPS;
		if((count * InfiniteScrollManager.NUMBER_OF_APPS) + module > this.apps.size()) {

		}

	}

	public int getMaxCount() {
		if(this.apps.size() % InfiniteScrollManager.NUMBER_OF_APPS == 0) {
			return this.apps.size() / InfiniteScrollManager.NUMBER_OF_APPS;
		}
		else {
			return (this.apps.size() / InfiniteScrollManager.NUMBER_OF_APPS) + 1;
		}
	}

}

package com.mapplas.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class AppOrderedList implements Comparator<App>, Parcelable {

	private ArrayList<App> appList;

	public AppOrderedList() {
		this.appList = new ArrayList<App>();
	}

	public ArrayList<App> getAppList() {
		return this.appList;
	}

	public void setAppList(ArrayList<App> list) {
		this.appList = list;
	}
	
	public void reset() {
		this.appList.clear();
	}
	
	public void sort() {
		Collections.sort(this.appList, this);
	}

	@Override
	public int compare(App app1, App app2) {
		if(app1.isAuxPin() && !app2.isAuxPin()) {
			return -1;
		}
		else if(!app1.isAuxPin() && app2.isAuxPin()) {
			return 1;
//		}
//		else if(app1.isAuxPin() && app2.isAuxPin()) {
			// Check distance
//			if(distancia(app1.getLatitude(), app1.getLongitude(), lat2, lon2) > distancia(app2.getLatitude(), app2.getLongitude(), lat2, lon2)) {
//				return -1;
//			}
//			else {
//				return 1;
//			}
		}
		else
			return 0;
	}

	protected double distancia(long lat1, long lon1, long lat2, long lon2) {
		long theta = lon1 - lon2;
		double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);

		double miles = dist * 60 * 1.1515;

		return miles * 1.609344;
	}

	/**
	 * 
	 * Parcelable
	 * 
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this.appList);
	}

	public AppOrderedList(Parcel parcel) {
		parcel.writeTypedList(this.appList);
	}

	public static final Parcelable.Creator<AppOrderedList> CREATOR = new Parcelable.Creator<AppOrderedList>() {

		@Override
		public AppOrderedList createFromParcel(Parcel source) {
			return new AppOrderedList(source);
		}

		@Override
		public AppOrderedList[] newArray(int size) {
			return new AppOrderedList[size];
		}
	};

}

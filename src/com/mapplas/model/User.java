package com.mapplas.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	private int id = 0;

	private String imei = "";

	private ArrayList<App> pinnedApps = new ArrayList<App>();

	private ArrayList<App> blockedApps = new ArrayList<App>();

	// Users table name
	public static final String TABLE_USERS = "user";

	// Books Table Columns names
	public static final String KEY_ID = "id";

	public static final String KEY_IMEI = "imei";

	public static final String KEY_PINNEDAPPS = "pinnedApps";

	public static final String KEY_BLOKEDAPPS = "blockedApps";

	public static final String[] COLUMNS = { KEY_ID, KEY_IMEI, KEY_BLOKEDAPPS, KEY_BLOKEDAPPS };

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public ArrayList<App> pinnedApps() {
		return pinnedApps;
	}

	public void setPinnedApps(ArrayList<App> pinnedApps) {
		this.pinnedApps = pinnedApps;
	}

	public ArrayList<App> blockedApps() {
		return blockedApps;
	}

	public void setBlockedApps(ArrayList<App> blockedApps) {
		this.blockedApps = blockedApps;
	}

	/**
	 * Parcelable methods
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.imei);
		dest.writeTypedList(this.pinnedApps);
		dest.writeTypedList(this.blockedApps);
	}

	public User(Parcel parcel) {
		this.id = parcel.readInt();
		this.imei = parcel.readString();
		parcel.readTypedList(this.pinnedApps, App.CREATOR);
		parcel.readTypedList(this.blockedApps, App.CREATOR);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

}

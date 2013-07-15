package com.mapplas.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User implements Parcelable, Unit {

	public static final String TABLE_NAME = "users";

	@DatabaseField(id = true)
	private int id = 0;

	@DatabaseField
	private String tel = "";

	@DatabaseField
	private String imei = "";

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<App> pinnedApps = new ArrayList<App>();

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<App> blockedApps = new ArrayList<App>();

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTelf() {
		return this.tel;
	}

	public void setTelf(String telf) {
		this.tel = telf;
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
		dest.writeString(this.tel);
		dest.writeString(this.imei);
		dest.writeTypedList(this.pinnedApps);
		dest.writeTypedList(this.blockedApps);
	}

	public User(Parcel parcel) {
		this.id = parcel.readInt();
		this.tel = parcel.readString();
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

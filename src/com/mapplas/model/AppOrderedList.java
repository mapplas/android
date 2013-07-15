package com.mapplas.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "orderedList")
public class AppOrderedList implements Parcelable, Unit {
	
	public static final String TABLE_NAME = "orderedList";

	@DatabaseField (generatedId=true)
	private int autoincrementId;
	
	@DatabaseField (dataType = DataType.SERIALIZABLE)
	private ArrayList<App> appList = new ArrayList<App>();

	@DatabaseField
	private String currentLocation = "";

	public AppOrderedList() {
	}

	public ArrayList<App> getAppList() {
		return this.appList;
	}

	public void setAppList(ArrayList<App> list) {
		this.appList = list;
	}
	
	public void add(App app) {
		this.appList.add(app);
//		Collections.sort(this.appList, this);
	}
	
	public void reset() {
		this.appList.clear();
	}
	
	public int size() {
		return this.appList.size();
	}
	
	public App get(int index) {
		return this.appList.get(index);
	}
	
	public void setCurrentLocation(String location) {
		this.currentLocation = location;
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
		dest.writeString(this.currentLocation);
		dest.writeInt(this.autoincrementId);
	}

	public AppOrderedList(Parcel parcel) {
		parcel.readTypedList(this.appList, App.CREATOR);
		this.currentLocation = parcel.readString();
		this.autoincrementId = parcel.readInt();
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

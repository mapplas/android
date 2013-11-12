package com.mapplas.model;

import java.util.ArrayList;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class SuperModel implements Parcelable {

	private String currentLocation = "";

	private Location location;

	private User currentUser = null;

	private String currentRadius = "0.1";

	private String currentIMEI = "";

	private String currentDescriptiveGeoLoc = "";

	private AppOrderedList appList = new AppOrderedList();

	private boolean operationError = false;

	private String errorText = "";

	private ArrayList<String> notificationRawList = new ArrayList<String>();

	private boolean moreData = true;
	
	private boolean fromBasqueCountry = false;

	public SuperModel() {
	}

	/**
	 * Getter and setters
	 * 
	 */
	public String currentRadius() {
		return currentRadius;
	}

	public void setCurrentRadius(String currentRadius) {
		this.currentRadius = currentRadius;
	}

	public String currentDescriptiveGeoLoc() {
		return currentDescriptiveGeoLoc;
	}

	public void setCurrentDescriptiveGeoLoc(String currentDescriptiveGeoLoc) {
		this.currentDescriptiveGeoLoc = currentDescriptiveGeoLoc;
	}

	public AppOrderedList appList() {
		return this.appList;
	}

	public void setAppList(AppOrderedList appList) {
		this.appList = appList;
	}

	public boolean operationError() {
		return operationError;
	}

	public void setOperationError(boolean operationError) {
		this.operationError = operationError;
	}

	public String errorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String currentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String string) {
		this.currentLocation = string;
	}

	public User currentUser() {
		return currentUser;
	}

	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	public String currentIMEI() {
		return currentIMEI;
	}

	public void setCurrentIMEI(String imei) {
		this.currentIMEI = imei;
	}

	public ArrayList<String> notificationRawList() {
		return this.notificationRawList;
	}

	public void setNotificationRawList(ArrayList<String> rawList) {
		this.notificationRawList = rawList;
	}

	public void setLocation(Location loc) {
		this.location = loc;
	}

	public Location getLocation() {
		return this.location;
	}

	/**
	 * 
	 */

	public void resetError() {
		this.operationError = false;
		this.errorText = "";
	}

	public void resetLocalizations() {
		this.appList.reset();
	}

	public void resetModel() {
		this.resetLocalizations();
	}

	public boolean moreData() {
		return moreData;
	}

	public void setMoreData(boolean moreData) {
		this.moreData = moreData;
	}

	public boolean isFromBasqueCountry() {
		return fromBasqueCountry;
	}

	public void setFromBasqueCountry(boolean fromBasqueCountry) {
		this.fromBasqueCountry = fromBasqueCountry;
	}
	
	public void initializeForNewAppRequest() {
		this.setMoreData(true);
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
		dest.writeString(this.currentLocation);
		dest.writeParcelable(this.currentUser, flags);
		dest.writeString(this.currentRadius);
		dest.writeString(this.currentIMEI);
		dest.writeString(this.currentDescriptiveGeoLoc);
		dest.writeParcelable(this.appList, flags);
		dest.writeByte((byte)(this.operationError ? 1 : 0));
		dest.writeString(this.errorText);
		dest.writeSerializable(this.notificationRawList);
		dest.writeParcelable(this.location, flags);
		dest.writeByte((byte)(this.moreData ? 1 : 0));
		dest.writeByte((byte)(this.fromBasqueCountry ? 1 : 0));
	}

	public SuperModel(Parcel parcel) {
		this.currentLocation = parcel.readString();
		this.currentUser = parcel.readParcelable(User.class.getClassLoader());
		this.currentRadius = parcel.readString();
		this.currentIMEI = parcel.readString();
		this.currentDescriptiveGeoLoc = parcel.readString();
		this.appList = parcel.readParcelable(AppOrderedList.class.getClassLoader());
		parcel.readParcelable(AppOrderedList.class.getClassLoader());
		this.operationError = parcel.readByte() == 1;
		this.errorText = parcel.readString();
		parcel.readParcelable(Location.class.getClassLoader());
		this.moreData = parcel.readByte() == 1;
		this.fromBasqueCountry = parcel.readByte() == 1;
	}

	public static final Parcelable.Creator<SuperModel> CREATOR = new Parcelable.Creator<SuperModel>() {

		@Override
		public SuperModel createFromParcel(Parcel source) {
			return new SuperModel(source);
		}

		@Override
		public SuperModel[] newArray(int size) {
			return new SuperModel[size];
		}
	};

	public void updateAppList(AppOrderedList newList) {
		this.appList.update(newList.getAppList());
	}
}

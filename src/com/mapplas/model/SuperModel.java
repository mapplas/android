package com.mapplas.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapplas.model.notifications.NotificationList;

public class SuperModel implements Parcelable {

	private String currentLocation = "";

	private User currentUser = null;

	private String currentRadius = "0.1";

	private String currentIMEI = "";

	private String currentDescriptiveGeoLoc = "";

	private ArrayList<App> appList;

	private NotificationList notificationList;

	private boolean operationError = false;

	private String errorText = "";

	public SuperModel() {
		this.appList = new ArrayList<App>();
		this.notificationList = new NotificationList();
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

	public ArrayList<App> appList() {
		return appList;
	}

	public void setAppList(ArrayList<App> appList) {
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

	public NotificationList notificationList() {
		return notificationList;
	}

	public void setNotificationList(NotificationList list) {
		this.notificationList = list;
	}

	/**
	 * 
	 */

	public void resetError() {
		this.operationError = false;
		this.errorText = "";
	}

	public void resetLocalizations() {
		this.appList = new ArrayList<App>();
	}

	public void resetNotifications() {
		this.notificationList = new NotificationList();
	}

	public void resetModel() {
		this.resetLocalizations();
		this.resetNotifications();
	}

	public App getAppWithIdInList(int position) {
		for(App currentApp : this.appList) {
			if(currentApp.getId() == position) {
				return currentApp;
			}
		}
		return null;
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
		dest.writeTypedList(this.appList);
		dest.writeParcelable(this.notificationList, flags);
		dest.writeByte((byte)(this.operationError ? 1 : 0));
		dest.writeString(this.errorText);
	}

	public SuperModel(Parcel parcel) {
		this.currentLocation = parcel.readString();
		this.currentUser = parcel.readParcelable(User.class.getClassLoader());
		this.currentRadius = parcel.readString();
		this.currentIMEI = parcel.readString();
		this.currentDescriptiveGeoLoc = parcel.readString();
		parcel.writeTypedList(this.appList);
		parcel.writeTypedList(this.notificationList);
		this.operationError = parcel.readByte() == 1;
		this.errorText = parcel.readString();
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

}

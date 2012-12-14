package com.mapplas.model;

import java.util.ArrayList;

public class SuperModel {

	public String currentLocation = "";

	public User currentUser = null;

	public String currentRadius = "0.1";

	public String currentIMEI = "";

	public String currentDescriptiveGeoLoc = "";

	public ArrayList<App> appList;

	public ArrayList<AppNotification> notificationList;

	public boolean operationError = false;

	public String errorText = "";

	public SuperModel() {
		appList = new ArrayList<App>();
		notificationList = new ArrayList<AppNotification>();
	}

	public void ResetError() {
		this.operationError = false;
		this.errorText = "";
	}

	public void ResetLocalizations() {
		this.appList = new ArrayList<App>();
	}

	public void ResetNotifications() {
		this.notificationList = new ArrayList<AppNotification>();
	}

	public void ResetModel() {
		this.ResetLocalizations();
		this.ResetNotifications();
	}

}

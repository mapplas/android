package com.mapplas.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SuperModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String currentLocation = "";

	private User currentUser = null;

	private String currentRadius = "0.1";

	private String currentIMEI = "";

	private String currentDescriptiveGeoLoc = "";

	private ArrayList<App> appList;

	private ArrayList<Notification> notificationList;

	private boolean operationError = false;

	private String errorText = "";

	public SuperModel() {
		this.appList = new ArrayList<App>();
		this.notificationList = new ArrayList<Notification>();
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

	public ArrayList<Notification> notificationList() {
		return notificationList;
	}

	public void setNotificationList(ArrayList<Notification> list) {
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
		this.notificationList = new ArrayList<Notification>();
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

}

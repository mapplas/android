package com.synesth.model;

import java.util.ArrayList;


public class SuperModel {
	
	public String currentLocation = "";
	public User currentUser = null;
	public String currentRadius = "0.1";
	public String currentIMEI = "";
	public String currentDescriptiveGeoLoc = "";
	
	public ArrayList<Localization> localizations;
	public ArrayList<AppNotification> notifications;
	
	public boolean operationError = false;
	public String errorText = "";
	
	public SuperModel()
	{
		localizations = new ArrayList<Localization>();
		notifications = new ArrayList<AppNotification>();
	}
	
	public void ResetError()
	{
		this.operationError = false;
		this.errorText = "";
	}
	
	public void ResetLocalizations()
	{
		this.localizations = new ArrayList<Localization>();
	}
	
	public void ResetNotifications()
	{
		this.notifications = new ArrayList<AppNotification>();
	}
	
	public void ResetModel()
	{
		this.ResetLocalizations();
		this.ResetNotifications();
	}
	
}

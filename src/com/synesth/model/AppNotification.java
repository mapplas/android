package com.synesth.model;

import com.synesth.app.SynesthActivity;
import com.synesth.utils.NetRequests;

public class AppNotification {
	// ---------------------------------------------------------------------------
	// Properties
	private int id = 0;
	
	private int idCompany = 0;
	private int idLocalization = 0;
	
	private String name = "?";
	private String description = "?";
	private String date = "?";
	private String hour = "?";
	
	private Localization auxLocalization = null;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdLocalization() {
		return idLocalization;
	}
	public void setIdLocalization(int idLocalization) {
		this.idLocalization = idLocalization;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public boolean FillLocalData()
	{
		this.auxLocalization = SynesthActivity.GetLocalizationById(this.idLocalization);
		
		if(this.auxLocalization != null)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean FillRemoteData()
	{
		try
		{
			String serverResponse = NetRequests.LocationIdRequest(SynesthActivity.GetModel().currentLocation, SynesthActivity.GetModel().currentUser.getId() + "", this.idLocalization + "");
		
			JsonParser jp = new JsonParser();
			this.auxLocalization = jp.ParseLocalization(serverResponse);
		}catch (Exception exc)
		{
			return false;
		}
		
		if(this.auxLocalization != null)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean FillData()
	{
		if(this.FillLocalData())
		{
			return true;
		}else
		{
			if(this.FillRemoteData())
			{
				return true;
			}
		}
		
		return false;
	}
	public int getIdCompany() {
		return idCompany;
	}
	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}
	public Localization getAuxLocalization() {
		return auxLocalization;
	}
	public void setAuxLocalization(Localization auxLocalization) {
		this.auxLocalization = auxLocalization;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	
	// ---------------------------------------------------------------------------
}

package com.mapplas.model.notifications;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mapplas.model.App;
import com.mapplas.model.Unit;

@DatabaseTable(tableName = "notifications")
public class Notification implements Serializable, Unit {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "notifications";

	// ---------------------------------------------------------------------------
	// Properties
	@DatabaseField(id = true)
	private int id = 0;

	@DatabaseField
	private int idCompany = 0;

	@DatabaseField
	private int idApp = 0;

	@DatabaseField
	private String name = "";

	@DatabaseField
	private String description = "";

	@DatabaseField
	private String date = "";

	@DatabaseField
	private String hour = "";

	private App auxApp = null;

	@DatabaseField
	private int seen = 0;

	@DatabaseField
	private int shown = 0;

	@DatabaseField
	private long arrivalTimestamp = 0;

	@DatabaseField
	private String currentLocation = "";
	
	// Needed by ormlite
	public Notification() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdLocalization() {
		return idApp;
	}

	public void setIdLocalization(int idLocalization) {
		this.idApp = idLocalization;
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

	// public boolean FillLocalData() {
	// this.auxLocalization =
	// MapplasActivity.GetLocalizationById(this.idLocalization);
	//
	// if(this.auxLocalization != null) {
	// return true;
	// }
	//
	// return false;
	// }
	//
	// public boolean FillRemoteData() {
	// try {
	// String serverResponse =
	// NetRequests.LocationIdRequest(MapplasActivity.GetModel().currentLocation,
	// MapplasActivity.GetModel().currentUser.getId() + "", this.idLocalization
	// + "");
	//
	// JsonParser jp = new JsonParser();
	// this.auxLocalization = jp.ParseLocalization(serverResponse);
	// } catch (Exception exc) {
	// return false;
	// }
	//
	// if(this.auxLocalization != null) {
	// return true;
	// }
	//
	// return false;
	// }
	//
	// public boolean FillData() {
	// if(this.FillLocalData()) {
	// return true;
	// }
	// else {
	// if(this.FillRemoteData()) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	public int getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}

	public App getAuxApp() {
		return auxApp;
	}

	public void setAuxApp(App auxApp) {
		this.auxApp = auxApp;
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

	public int seen() {
		return seen;
	}

	public void setSeen(int seen) {
		this.seen = seen;
	}

	public int shown() {
		return shown;
	}

	public void setShown(int shown) {
		this.shown = shown;
	}

	public long arrivalTimestamp() {
		return arrivalTimestamp;
	}

	public void setArrivalTimestamp(long arrivalTimestamp) {
		this.arrivalTimestamp = arrivalTimestamp;
	}

	public String currentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	// ---------------------------------------------------------------------------
}

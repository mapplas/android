package com.mapplas.model.notifications;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mapplas.model.App;
import com.mapplas.model.Unit;

@DatabaseTable(tableName = "notifications")
public class Notification implements Parcelable, Unit {

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

	@DatabaseField
	private long dateInMs = 0;

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

	public long dateInMiliseconds() {
		return this.dateInMs;
	}

	public void setDateInMiliseconds(long ms) {
		this.dateInMs = ms;
	}

	// ---------------------------------------------------------------------------

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
		dest.writeInt(this.idCompany);
		dest.writeInt(this.idApp);
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeString(this.date);
		dest.writeString(this.hour);
		dest.writeParcelable(this.auxApp, flags);
		dest.writeInt(this.seen);
		dest.writeInt(this.shown);
		dest.writeLong(this.arrivalTimestamp);
		dest.writeString(this.currentLocation);
		dest.writeLong(this.dateInMs);
	}

	public Notification(Parcel parcel) {
		this.id = parcel.readInt();
		this.idCompany = parcel.readInt();
		this.idApp = parcel.readInt();
		this.name = parcel.readString();
		this.description = parcel.readString();
		this.date = parcel.readString();
		this.hour = parcel.readString();
		this.auxApp = parcel.readParcelable(null);
		this.seen = parcel.readInt();
		this.shown = parcel.readInt();
		this.arrivalTimestamp = parcel.readLong();
		this.currentLocation = parcel.readString();
		this.dateInMs = parcel.readLong();
	}

	public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {

		@Override
		public Notification createFromParcel(Parcel source) {
			return new Notification(source);
		}

		@Override
		public Notification[] newArray(int size) {
			return new Notification[size];
		}
	};
}

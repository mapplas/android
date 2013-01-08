package com.mapplas.model;

import java.util.ArrayList;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class App implements Parcelable {

	// ---------------------------------------------------------------------------
	// Constants
	// Type Constants
	public static final String SYNESTH_TYPE_OFFER = "offer";

	public static final String SYNESTH_TYPE_URL = "url";

	public static final String SYNESTH_TYPE_APPLICATION = "application";

	public static final String SYNESTH_TYPE_USERALARM = "useralarm";

	public static final String SYNESTH_TYPE_USERURL = "userurl";

	// App Type Constants
	public static final String SYNESTH_APPTYPE_IOS = "ios";

	public static final String SYNESTH_APPTYPE_ANDROID = "android";

	public static final String SYNESTH_APPTYPE_BLACKBERRY = "blackberry";

	public static final String SYNESTH_APPTYPE_HTML5 = "html5";

	// ---------------------------------------------------------------------------

	// ---------------------------------------------------------------------------
	// Properties
	private int id = 0;

	private String name = "?";

	private double latitude = 0.0f;

	private double longitude = 0.0f;

	private String type = "?";

	private int appId = 0;

	private String appName = "?";

	private String appLogo = "?";

	private String appLogoMini = "?";

	private String appUrl = "?";

	private String appDescription = "?";

	private String Phone = "";

	private float AppPrice = 0.0f;

	private boolean auxFavourite = false;

	private boolean auxPin = false;
	
	private boolean auxBlocked = false;

	private float auxRate = 0.0f;

	private String auxComment = "";

	private float auxTotalRate = 0.0f;

	private int auxTotalPins = 0;

	private int auxTotalComments = 0;

	private ArrayList<Comment> auxComments = new ArrayList<Comment>();

	private ArrayList<Photo> auxPhotos = new ArrayList<Photo>();

	private ApplicationInfo internalApplicationInfo = null;

	private double pinnedLatitude = 0.0f;

	private double pinnedLongitude = 0.0f;

	private String setPinnedGeocodedLocation = "";

	public App() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppLogo() {
		return appLogo;
	}

	public void setAppLogo(String appLogo) {
		this.appLogo = appLogo;
	}

	public String getAppLogoMini() {
		return appLogoMini;
	}

	public void setAppLogoMini(String appLogoMini) {
		this.appLogoMini = appLogoMini;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public ApplicationInfo getInternalApplicationInfo() {
		return internalApplicationInfo;
	}

	public void setInternalApplicationInfo(ApplicationInfo internalApplicationInfo) {
		this.internalApplicationInfo = internalApplicationInfo;
	}

	public boolean isAuxFavourite() {
		return auxFavourite;
	}

	public void setAuxFavourite(boolean auxFavourite) {
		this.auxFavourite = auxFavourite;
	}

	public float getAuxRate() {
		return auxRate;
	}

	public void setAuxRate(float auxRate) {
		this.auxRate = auxRate;
	}

	public ArrayList<Comment> getAuxComments() {
		return auxComments;
	}

	public void setAuxComments(ArrayList<Comment> auxComments) {
		this.auxComments = auxComments;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public float getAppPrice() {
		return AppPrice;
	}

	public void setAppPrice(float appPrice) {
		AppPrice = appPrice;
	}

	public float getAuxTotalRate() {
		return auxTotalRate;
	}

	public void setAuxTotalRate(float auxTotalRate) {
		this.auxTotalRate = auxTotalRate;
	}

	public String getAuxComment() {
		return auxComment;
	}

	public void setAuxComment(String auxComment) {
		this.auxComment = auxComment;
	}

	public ArrayList<Photo> getAuxPhotos() {
		return auxPhotos;
	}

	public void setAuxPhotos(ArrayList<Photo> auxPhotos) {
		this.auxPhotos = auxPhotos;
	}

	public boolean isAuxPin() {
		return this.auxPin;
	}

	public void setAuxPin(boolean auxPin) {
		this.auxPin = auxPin;
	}
	
	public boolean isAuxBlocked() {
		return this.auxBlocked;
	}
	
	public void setAuxBlocked(boolean auxBlocked) {
		this.auxBlocked = auxBlocked;
	}

	public int getAuxTotalPins() {
		return auxTotalPins;
	}

	public void setAuxTotalPins(int auxTotalPins) {
		this.auxTotalPins = auxTotalPins;
	}

	public int getAuxTotalComments() {
		return auxTotalComments;
	}

	public void setAuxTotalComments(int auxTotalComments) {
		this.auxTotalComments = auxTotalComments;
	}

	public double getPinnedLatitude() {
		return this.pinnedLatitude;
	}

	public void setPinnedLatitude(double lat) {
		this.pinnedLatitude = lat;
	}

	public double getPinnedLongitude() {
		return this.pinnedLongitude;
	}

	public void setPinnedLongitude(double lon) {
		this.pinnedLongitude = lon;
	}

	public String getPinnedGeocodedLocation() {
		return this.setPinnedGeocodedLocation;
	}

	public void setPinnedGeocodedLocation(String loc) {
		this.setPinnedGeocodedLocation = loc;
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
		dest.writeString(this.name);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeString(this.type);
		dest.writeInt(this.appId);
		dest.writeString(this.appName);
		dest.writeString(this.appLogo);
		dest.writeString(this.appLogoMini);
		dest.writeString(this.appUrl);
		dest.writeString(this.appDescription);
		dest.writeString(this.Phone);
		dest.writeFloat(this.AppPrice);
		dest.writeByte((byte)(this.auxFavourite ? 1 : 0));
		dest.writeByte((byte)(this.auxPin ? 1 : 0));
		dest.writeByte((byte)(this.auxBlocked ? 1 : 0));
		dest.writeFloat(this.auxRate);
		dest.writeString(this.auxComment);
		dest.writeFloat(this.auxTotalRate);
		dest.writeInt(this.auxTotalPins);
		dest.writeInt(this.auxTotalComments);
		dest.writeTypedList(this.auxComments);
		dest.writeTypedList(this.auxPhotos);
		dest.writeParcelable(this.internalApplicationInfo, flags);
		dest.writeDouble(this.pinnedLatitude);
		dest.writeDouble(this.pinnedLongitude);
		dest.writeString(this.setPinnedGeocodedLocation);
	}

	public App(Parcel parcel) {
		this.id = parcel.readInt();
		this.name = parcel.readString();
		this.latitude = parcel.readLong();
		this.longitude = parcel.readLong();
		this.type = parcel.readString();
		this.appId = parcel.readInt();
		this.appName = parcel.readString();
		this.appLogo = parcel.readString();
		this.appLogoMini = parcel.readString();
		this.appUrl = parcel.readString();
		this.appDescription = parcel.readString();
		this.Phone = parcel.readString();
		this.AppPrice = parcel.readFloat();
		this.auxFavourite = parcel.readByte() == 1;
		this.auxPin = parcel.readByte() == 1;
		this.auxBlocked = parcel.readByte() == 1;
		this.auxRate = parcel.readFloat();
		this.auxComment = parcel.readString();
		this.auxTotalRate = parcel.readFloat();
		this.auxTotalPins = parcel.readInt();
		this.auxTotalComments = parcel.readInt();
		parcel.readTypedList(this.auxComments, Comment.CREATOR);
		parcel.readTypedList(this.auxPhotos, Photo.CREATOR);
		this.internalApplicationInfo = parcel.readParcelable(null);
		this.pinnedLatitude = parcel.readDouble();
		this.pinnedLongitude = parcel.readDouble();
		this.setPinnedGeocodedLocation = parcel.readString();
	}

	public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() {

		@Override
		public App createFromParcel(Parcel source) {
			return new App(source);
		}

		@Override
		public App[] newArray(int size) {
			return new App[size];
		}
	};

}

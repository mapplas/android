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

//	private String address = "?";

//	private String zipCode = "?";
//
//	private String state = "?";
//
//	private String city = "?";
//
//	private String country = "?";

	private String type = "?";

	private int idCompany = 0;

	private int offerId = 0;

	private String offerName = "?";

	private String offerLogo = "?";

	private String offerLogoMini = "?";

	private String offerURL = "?";

	private String offerText = "?";

	private int urlId = 0;

	private String urlName = "?";

	private String urlLogo = "?";

	private String urlLogoMini = "?";

	private String urlValue = "?";

	private String urlText = "?";

	private int appId = 0;

	private String appName = "?";

	private String appLogo = "?";

	private String appLogoMini = "?";

	private String appUrl = "?";

	private String appDescription = "?";

	private String appType = "?";

	private int userAlarmId = 0;

	private String userAlarmName = "?";

	private int userUrlId = 0;

	private String userUrlValue = "?";

	private String userUrlDescription = "?";

	private String userUrlTags = "?";

	private String userUrlComment = "?";

	private String userUrlPhoto = "?";

	private int idUser = 0;

	private double radius = 0.0f;

	private String Phone = "";

//	private String Wifi = "";
//
//	private String Bluetooth = "";
//
//	private String Location = "";

	private float AppPrice = 0.0f;

//	private int auxPlus = 0;
//
//	private int auxMinus = 0;

	private boolean auxFavourite = false;

	private boolean auxPin = false;

	private float auxRate = 0.0f;

	private String auxComment = "";

	private float auxTotalRate = 0.0f;

	private int auxTotalPins = 0;

	private int auxTotalComments = 0;

	private ArrayList<Comment> auxComments = new ArrayList<Comment>();

	private ArrayList<Photo> auxPhotos = new ArrayList<Photo>();

//	private boolean internalLogoLoaded = false;
//
//	private Bitmap internalLogo = null;

	private ApplicationInfo internalApplicationInfo = null;

	// ---------------------------------------------------------------------------

	// ---------------------------------------------------------------------------
	// Methods
	
	public App() {
		
	}
	
	public String toString() {
		String ret = "";

		if(this.type.equalsIgnoreCase(App.SYNESTH_TYPE_APPLICATION)) {
			ret = this.id + ") " + this.name + " [APPLICATION " + this.appId + "]: " + this.appName + "\n";
			ret += "     IDCompany: " + this.idCompany + "\n";
			ret += "     Logo: " + this.appLogo + "\n";
			ret += "     Logo Mini: " + this.appLogoMini + "\n";
			ret += "     Type: " + this.appType + "\n";
			ret += "     URL: " + this.appUrl + "\n";
			ret += "     Description: " + this.appDescription + "\n";
		}
		else if(this.type.equalsIgnoreCase(App.SYNESTH_TYPE_OFFER)) {
			ret = this.id + ") " + this.name + " [OFFER " + this.offerId + "]: " + this.offerName + "\n";
			ret += "     IDCompany: " + this.idCompany + "\n";
			ret += "     Logo: " + this.offerLogo + "\n";
			ret += "     Logo Mini: " + this.offerLogoMini + "\n";
			ret += "     URL: " + this.offerURL + "\n";
			ret += "     Text: " + this.offerText + "\n";
		}
		else if(this.type.equalsIgnoreCase(App.SYNESTH_TYPE_URL)) {
			ret = this.id + ") " + this.name + " [URL " + this.urlId + "]: " + this.urlName + "\n";
			ret += "     IDCompany: " + this.idCompany + "\n";
			ret += "     Logo: " + this.urlLogo + "\n";
			ret += "     Logo Mini: " + this.urlLogoMini + "\n";
			ret += "     URL: " + this.urlValue + "\n";
			ret += "     Text: " + this.urlText + "\n";
		}
		else if(this.type.equalsIgnoreCase(App.SYNESTH_TYPE_USERALARM)) {
			ret = this.id + ") " + this.name + " [USERALARM " + this.userAlarmId + "]: " + this.userAlarmName + "\n";
			ret += "     IDUser: " + this.idUser + "\n";
		}
		else if(this.type.equalsIgnoreCase(App.SYNESTH_TYPE_USERURL)) {
			ret = this.id + ") " + this.name + " [USERURL " + this.userUrlId + "]: " + this.userUrlComment + "\n";
			ret += "     IDUser: " + this.idUser + "\n";
			ret += "     Tags: " + this.userUrlTags + "\n";
			ret += "     Photo: " + this.userUrlPhoto + "\n";
			ret += "     URL: " + this.userUrlValue + "\n";
			ret += "     Description: " + this.userUrlDescription + "\n";
		}
		else {
			ret = this.id + ") " + this.name + " [UNKNOWN (" + this.type + ")]: " + super.toString() + "\n";
		}

		ret += "     Radius: " + this.radius + "\n";
		ret += "     Location: " + this.latitude + "," + this.longitude + "\n";

		return ret;
	}

	// ---------------------------------------------------------------------------

	// ---------------------------------------------------------------------------
	// Getters & Setters

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

//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getZipCode() {
//		return zipCode;
//	}
//
//	public void setZipCode(String zipCode) {
//		this.zipCode = zipCode;
//	}

//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}

//	public String getCity() {
//		return city;
//	}
//
//	public void setCity(String city) {
//		this.city = city;
//	}

//	public String getCountry() {
//		return country;
//	}
//
//	public void setCountry(String country) {
//		this.country = country;
//	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	public int getIdCompany() {
//		return idCompany;
//	}
//
//	public void setIdCompany(int idCompany) {
//		this.idCompany = idCompany;
//	}

//	public int getOfferId() {
//		return offerId;
//	}
//
//	public void setOfferId(int offerId) {
//		this.offerId = offerId;
//	}

//	public String getOfferName() {
//		return offerName;
//	}
//
//	public void setOfferName(String offerName) {
//		this.offerName = offerName;
//	}

//	public String getOfferLogo() {
//		return offerLogo;
//	}
//
//	public void setOfferLogo(String offerLogo) {
//		this.offerLogo = offerLogo;
//	}

//	public String getOfferLogoMini() {
//		return offerLogoMini;
//	}
//
//	public void setOfferLogoMini(String offerLogoMini) {
//		this.offerLogoMini = offerLogoMini;
//	}

//	public String getOfferURL() {
//		return offerURL;
//	}
//
//	public void setOfferURL(String offerURL) {
//		this.offerURL = offerURL;
//	}

//	public String getOfferText() {
//		return offerText;
//	}
//
//	public void setOfferText(String offerText) {
//		this.offerText = offerText;
//	}

//	public int getUrlId() {
//		return urlId;
//	}
//
//	public void setUrlId(int urlId) {
//		this.urlId = urlId;
//	}
//
//	public String getUrlName() {
//		return urlName;
//	}
//
//	public void setUrlName(String urlName) {
//		this.urlName = urlName;
//	}
//
//	public String getUrlLogo() {
//		return urlLogo;
//	}
//
//	public void setUrlLogo(String urlLogo) {
//		this.urlLogo = urlLogo;
//	}
//
//	public String getUrlLogoMini() {
//		return urlLogoMini;
//	}
//
//	public void setUrlLogoMini(String urlLogoMini) {
//		this.urlLogoMini = urlLogoMini;
//	}
//
//	public String getUrlValue() {
//		return urlValue;
//	}
//
//	public void setUrlValue(String urlValue) {
//		this.urlValue = urlValue;
//	}
//
//	public String getUrlText() {
//		return urlText;
//	}
//
//	public void setUrlText(String urlText) {
//		this.urlText = urlText;
//	}

//	public int getAppId() {
//		return appId;
//	}
//
//	public void setAppId(int appId) {
//		this.appId = appId;
//	}

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

//	public String getAppType() {
//		return appType;
//	}
//
//	public void setAppType(String appType) {
//		this.appType = appType;
//	}
//
//	public int getUserAlarmId() {
//		return userAlarmId;
//	}
//
//	public void setUserAlarmId(int userAlarmId) {
//		this.userAlarmId = userAlarmId;
//	}
//
//	public String getUserAlarmName() {
//		return userAlarmName;
//	}
//
//	public void setUserAlarmName(String userAlarmName) {
//		this.userAlarmName = userAlarmName;
//	}
//
//	public int getUserUrlId() {
//		return userUrlId;
//	}
//
//	public void setUserUrlId(int userUrlId) {
//		this.userUrlId = userUrlId;
//	}
//
//	public String getUserUrlValue() {
//		return userUrlValue;
//	}
//
//	public void setUserUrlValue(String userUrlValue) {
//		this.userUrlValue = userUrlValue;
//	}
//
//	public String getUserUrlDescription() {
//		return userUrlDescription;
//	}
//
//	public void setUserUrlDescription(String userUrlDescription) {
//		this.userUrlDescription = userUrlDescription;
//	}
//
//	public String getUserUrlTags() {
//		return userUrlTags;
//	}
//
//	public void setUserUrlTags(String userUrlTags) {
//		this.userUrlTags = userUrlTags;
//	}
//
//	public String getUserUrlComment() {
//		return userUrlComment;
//	}
//
//	public void setUserUrlComment(String userUrlComment) {
//		this.userUrlComment = userUrlComment;
//	}
//
//	public String getUserUrlPhoto() {
//		return userUrlPhoto;
//	}
//
//	public void setUserUrlPhoto(String userUrlPhoto) {
//		this.userUrlPhoto = userUrlPhoto;
//	}
//
//	public int getIdUser() {
//		return idUser;
//	}
//
//	public void setIdUser(int idUser) {
//		this.idUser = idUser;
//	}
//
//	public double getRadius() {
//		return radius;
//	}
//
//	public void setRadius(double radius) {
//		this.radius = radius;
//	}

	// ---------------------------------------------------------------------------

//	public boolean isInternalLogoLoaded() {
//		return internalLogoLoaded;
//	}
//
//	public void setInternalLogoLoaded(boolean internalLogoLoaded) {
//		this.internalLogoLoaded = internalLogoLoaded;
//	}
//
//	public Bitmap getInternalLogo() {
//		return internalLogo;
//	}
//
//	public void setInternalLogo(Bitmap internalLogo) {
//		this.internalLogo = internalLogo;
//	}
//
//	public int getAuxPlus() {
//		return auxPlus;
//	}
//
//	public void setAuxPlus(int auxPlus) {
//		this.auxPlus = auxPlus;
//	}
//
//	public int getAuxMinus() {
//		return auxMinus;
//	}
//
//	public void setAuxMinus(int auxMinus) {
//		this.auxMinus = auxMinus;
//	}

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

//	public String getWifi() {
//		return Wifi;
//	}
//
//	public void setWifi(String wifi) {
//		Wifi = wifi;
//	}

//	public String getBluetooth() {
//		return Bluetooth;
//	}
//
//	public void setBluetooth(String bluetooth) {
//		Bluetooth = bluetooth;
//	}

//	public String getLocation() {
//		return Location;
//	}
//
//	public void setLocation(String location) {
//		Location = location;
//	}

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
		return auxPin;
	}

	public void setAuxPin(boolean auxPin) {
		this.auxPin = auxPin;
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
//		dest.writeString(this.address);
//		dest.writeString(this.zipCode);
//		dest.writeString(this.state);
//		dest.writeString(this.city);
//		dest.writeString(this.country);
		dest.writeString(this.type);
//		dest.writeInt(this.idCompany);
//		dest.writeInt(this.offerId);
//		dest.writeString(this.offerName);
//		dest.writeString(this.offerLogo);
//		dest.writeString(this.offerLogoMini);
//		dest.writeString(this.offerURL);
//		dest.writeString(this.offerText);
//		dest.writeInt(this.urlId);
//		dest.writeString(this.urlName);
//		dest.writeString(this.urlLogo);
//		dest.writeString(this.urlLogoMini);
//		dest.writeString(this.urlValue);
//		dest.writeString(this.urlText);
		dest.writeInt(this.appId);
		dest.writeString(this.appName);
		dest.writeString(this.appLogo);
		dest.writeString(this.appLogoMini);
		dest.writeString(this.appUrl);
		dest.writeString(this.appDescription);
//		dest.writeString(this.appType);
//		dest.writeInt(this.userAlarmId);
//		dest.writeString(this.userAlarmName);
//		dest.writeInt(this.userUrlId);
//		dest.writeString(this.userUrlValue);
//		dest.writeString(this.userUrlDescription);
//		dest.writeString(this.userUrlTags);
//		dest.writeString(this.userUrlComment);
//		dest.writeString(this.userUrlPhoto);
//		dest.writeInt(this.idUser);
//		dest.writeDouble(this.radius);
		dest.writeString(this.Phone);
//		dest.writeString(this.Wifi);
//		dest.writeString(this.Bluetooth);
//		dest.writeString(this.Location);
		dest.writeFloat(this.AppPrice);
//		dest.writeInt(this.auxPlus);
//		dest.writeInt(this.auxMinus);
		dest.writeByte((byte)(this.auxFavourite ? 1 : 0));
		dest.writeByte((byte)(this.auxPin ? 1 : 0));
		dest.writeFloat(this.auxRate);
		dest.writeString(this.auxComment);
		dest.writeFloat(this.auxTotalRate);
		dest.writeInt(this.auxTotalPins);
		dest.writeInt(this.auxTotalComments);
		dest.writeTypedList(this.auxComments);
		dest.writeTypedList(this.auxPhotos);
//		dest.writeByte((byte)(this.internalLogoLoaded ? 1 : 0));
//		dest.writeParcelable(this.internalLogo, 0);
		dest.writeParcelable(this.internalApplicationInfo, flags);
	}

	public App(Parcel parcel) {
		this.id = parcel.readInt();
		this.name = parcel.readString();
		this.latitude = parcel.readLong();
		this.longitude = parcel.readLong();
//		this.address = parcel.readString();
//		this.zipCode = parcel.readString();
//		this.state = parcel.readString();
//		this.city = parcel.readString();
//		this.country = parcel.readString();
		this.type = parcel.readString();
//		this.idCompany = parcel.readInt();
//		this.offerId = parcel.readInt();
//		this.offerName = parcel.readString();
//		this.offerLogo = parcel.readString();
//		this.offerLogoMini = parcel.readString();
//		this.offerURL = parcel.readString();
//		this.offerText = parcel.readString();
//		this.urlId = parcel.readInt();
//		this.urlName = parcel.readString();
//		this.urlLogo = parcel.readString();
//		this.urlLogoMini = parcel.readString();
//		this.urlValue = parcel.readString();
//		this.urlText = parcel.readString();
		this.appId = parcel.readInt();
		this.appName = parcel.readString();
		this.appLogo = parcel.readString();
		this.appLogoMini = parcel.readString();
		this.appUrl = parcel.readString();
		this.appDescription = parcel.readString();
//		this.appType = parcel.readString();
//		this.userAlarmId = parcel.readInt();
//		this.userAlarmName = parcel.readString();
//		this.userUrlId = parcel.readInt();
//		this.userUrlValue = parcel.readString();
//		this.userUrlDescription = parcel.readString();
//		this.userUrlTags = parcel.readString();
//		this.userUrlComment = parcel.readString();
//		this.userUrlPhoto = parcel.readString();
//		this.idUser = parcel.readParcelable(null);
//		this.radius = parcel.readDouble();
		this.Phone = parcel.readString();
//		this.Wifi = parcel.readString();
//		this.Bluetooth = parcel.readString();
//		this.Location = parcel.readString();
		this.AppPrice = parcel.readFloat();
//		this.auxPlus = parcel.readInt();
//		this.auxMinus = parcel.readInt();
		this.auxFavourite = parcel.readByte() == 1;
		this.auxPin = parcel.readByte() == 1;
		this.auxRate = parcel.readFloat();
		this.auxComment = parcel.readString();
		this.auxTotalRate = parcel.readFloat();
		this.auxTotalPins = parcel.readInt();
		this.auxTotalComments = parcel.readInt();
		parcel.readTypedList(this.auxComments, Comment.CREATOR);
		parcel.readTypedList(this.auxPhotos, Photo.CREATOR);
//		this.internalLogoLoaded = parcel.readByte() == 1;
//		this.internalLogo = parcel.readParcelable(null);
		this.internalApplicationInfo = parcel.readParcelable(null);
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

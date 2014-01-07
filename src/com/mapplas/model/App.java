package com.mapplas.model;

import java.util.ArrayList;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class App implements Parcelable {

	// ---------------------------------------------------------------------------

	// ---------------------------------------------------------------------------
	// Properties
	private String id;

	private String name = "";

	private String shortDescription = "";

	private String description;

	private String logo;
	
	private String icon_color_1 = "";
	
	private String icon_color_2 = "";
	
	private String icon_color_3 = "";
	
	private String icon_color_4 = "";

	private String price;

	private float rating;

	private int auxPin;

	private String address;

	private ArrayList<String> auxPhotos = new ArrayList<String>();

	private ApplicationInfo internalApplicationInfo = null;

	private String appDeveloperEmail = "";

	private String appDeveloperWeb = "";

	private ArrayList<MoreFromDeveloperApp> moreFromDev = new ArrayList<MoreFromDeveloperApp>();
	
	private int moreFromDeveloperCount;
	
	private String developerName;

	private String type = Constants.MAPPLAS_APPLICATION_TYPE_ANDROID_APPLICATION;

	public App() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppShortDescription() {
		return this.shortDescription;
	}

	public void setAppShortDescription(String appShortDescription) {
		this.shortDescription = appShortDescription;
	}

	public String getAppDescription() {
		return this.description;
	}

	public void setAppDescription(String appDescription) {
		this.description = appDescription;
	}

	public String getAppLogo() {
		return this.logo;
	}

	public void setAppLogo(String appLogo) {
		this.logo = appLogo;
	}

	public String getAppPrice() {
		return price;
	}

	public void setAppPrice(String appPrice) {
		price = appPrice;
	}

	public ArrayList<String> getAuxPhotos() {
		return auxPhotos;
	}

	public void setAuxPhotos(ArrayList<String> auxPhotos) {
		this.auxPhotos = auxPhotos;
	}

	public int isAuxPin() {
		return this.auxPin;
	}

	public void setAuxPin(int auxPin) {
		this.auxPin = auxPin;
	}

	// public boolean isAuxBlocked() {
	// return this.auxBlocked;
	// }
	//
	// public void setAuxBlocked(boolean auxBlocked) {
	// this.auxBlocked = auxBlocked;
	// }
	//
	// public int getAuxTotalPins() {
	// return auxTotalPins;
	// }
	//
	// public void setAuxTotalPins(int auxTotalPins) {
	// this.auxTotalPins = auxTotalPins;
	// }

	public ApplicationInfo getInternalApplicationInfo() {
		return internalApplicationInfo;
	}

	public void setInternalApplicationInfo(ApplicationInfo internalApplicationInfo) {
		this.internalApplicationInfo = internalApplicationInfo;
	}

	public String getAppType() {
		return this.type;
	}

	public void setAppType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAdress(String addr) {
		this.address = addr;
	}

	public String appDeveloperEmail() {
		return appDeveloperEmail;
	}

	public void setAppDeveloperEmail(String appDeveloperEmail) {
		this.appDeveloperEmail = appDeveloperEmail;
	}

	public String appDeveloperWeb() {
		return appDeveloperWeb;
	}

	public void setAppDeveloperWeb(String appDeveloperWeb) {
		this.appDeveloperWeb = appDeveloperWeb;
	}

	public float rating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = Float.valueOf(rating);
	}

	public ArrayList<MoreFromDeveloperApp> moreFromDev() {
		return moreFromDev;
	}

	public void setMoreFromDev(ArrayList<MoreFromDeveloperApp> moreFromDev) {
		this.moreFromDev = moreFromDev;
	}

	public int moreFromDeveloperCount() {
		return moreFromDeveloperCount;
	}

	public void setMoreFromDeveloperCount(int moreFromDeveloperCount) {
		this.moreFromDeveloperCount = moreFromDeveloperCount;
	}
	
	public String developerName() {
		return this.developerName;
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	
	public String getIcon_color_1() {
		return icon_color_1;
	}

	public void setIcon_color_1(String icon_color_1) {
		this.icon_color_1 = icon_color_1;
	}

	public String getIcon_color_2() {
		return icon_color_2;
	}

	public void setIcon_color_2(String icon_color_2) {
		this.icon_color_2 = icon_color_2;
	}

	public String getIcon_color_3() {
		return icon_color_3;
	}

	public void setIcon_color_3(String icon_color_3) {
		this.icon_color_3 = icon_color_3;
	}

	public String getIcon_color_4() {
		return icon_color_4;
	}

	public void setIcon_color_4(String icon_color_4) {
		this.icon_color_4 = icon_color_4;
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
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeString(this.logo);
		dest.writeString(this.price);
		// dest.writeByte((byte)(this.auxFavourite ? 1 : 0));
		dest.writeInt(this.auxPin);
		dest.writeSerializable(this.auxPhotos);
		dest.writeParcelable(this.internalApplicationInfo, flags);
		dest.writeString(this.type);
		dest.writeString(this.address);
		dest.writeString(this.appDeveloperEmail);
		dest.writeString(this.appDeveloperWeb);
		dest.writeFloat(this.rating);
		dest.writeString(this.shortDescription);
		dest.writeTypedList(this.moreFromDev);
		dest.writeInt(this.moreFromDeveloperCount);
		dest.writeString(this.developerName);
		dest.writeString(this.icon_color_1);
		dest.writeString(this.icon_color_2);
		dest.writeString(this.icon_color_3);
		dest.writeString(this.icon_color_4);
	}

	@SuppressWarnings("unchecked")
	public App(Parcel parcel) {
		this.id = parcel.readString();
		this.name = parcel.readString();
		this.description = parcel.readString();
		this.logo = parcel.readString();
		this.price = parcel.readString();
		// this.auxFavourite = parcel.readByte() == 1;
		this.auxPin = parcel.readInt();
		this.auxPhotos = (ArrayList<String>)parcel.readSerializable();
		this.internalApplicationInfo = parcel.readParcelable(null);
		this.type = parcel.readString();
		this.address = parcel.readString();
		this.appDeveloperEmail = parcel.readString();
		this.appDeveloperWeb = parcel.readString();
		this.rating = parcel.readFloat();
		this.shortDescription = parcel.readString();
		parcel.readTypedList(this.moreFromDev, MoreFromDeveloperApp.CREATOR);
		this.moreFromDeveloperCount = parcel.readInt();
		this.developerName = parcel.readString();
		this.icon_color_1 = parcel.readString();
		this.icon_color_2 = parcel.readString();
		this.icon_color_3 = parcel.readString();
		this.icon_color_4 = parcel.readString();
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

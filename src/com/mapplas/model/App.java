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

	private String name;

	private String shortDescription;

	private String description;

	private String logo;

	private String price;

	// private String version;
	//
	// private String versionUpdated;
	//
	// private String size;
	//
	// private float rating;
	//
	// private int votes;
	//
	// private String privacy;
	//
	private int auxPin;

	//
	// private int auxTotalPins = 0;
	//
	// private String auxPinnedGeocodedLocation;
	//
	// private boolean auxBlocked = false;
	//
	// private float auxRate = 0.0f;

	private ArrayList<Photo> auxPhotos = new ArrayList<Photo>();

	// private String video;
	//
	// private String banner;
	//
	private ApplicationInfo internalApplicationInfo = null;

	//
	// private String contentRating;
	//
	// private String operatingSystem;

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

	public ArrayList<Photo> getAuxPhotos() {
		return auxPhotos;
	}

	public void setAuxPhotos(ArrayList<Photo> auxPhotos) {
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
		// dest.writeByte((byte)(this.auxBlocked ? 1 : 0));
		dest.writeTypedList(this.auxPhotos);
		dest.writeParcelable(this.internalApplicationInfo, flags);
		dest.writeString(this.type);
	}

	public App(Parcel parcel) {
		this.id = parcel.readString();
		this.name = parcel.readString();
		this.description = parcel.readString();
		this.logo = parcel.readString();
		this.price = parcel.readString();
		// this.auxFavourite = parcel.readByte() == 1;
		this.auxPin = parcel.readInt();
		// this.auxBlocked = parcel.readByte() == 1;
		// this.auxRate = parcel.readFloat();
		// this.auxComment = parcel.readString();
		// this.auxTotalRate = parcel.readFloat();
		// this.auxTotalPins = parcel.readInt();
		parcel.readTypedList(this.auxPhotos, Photo.CREATOR);
		this.internalApplicationInfo = parcel.readParcelable(null);
		this.type = parcel.readString();
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

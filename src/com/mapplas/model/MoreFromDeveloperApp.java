package com.mapplas.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MoreFromDeveloperApp implements Parcelable {

	private String id;

	private String name;
	
	private String logo;
	
	private String shortDescription;
	
	private String price;

	public MoreFromDeveloperApp() {
	}

	public String id() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String name() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String logo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String shortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public String price() {
		return this.price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.logo);
		dest.writeString(this.shortDescription);
		dest.writeString(this.price);
	}

	public MoreFromDeveloperApp(Parcel parcel) {
		this.id = parcel.readString();
		this.name = parcel.readString();
		this.logo = parcel.readString();
		this.shortDescription = parcel.readString();
		this.price = parcel.readString();
	}

	public static final Parcelable.Creator<MoreFromDeveloperApp> CREATOR = new Parcelable.Creator<MoreFromDeveloperApp>() {

		@Override
		public MoreFromDeveloperApp createFromParcel(Parcel source) {
			return new MoreFromDeveloperApp(source);
		}

		@Override
		public MoreFromDeveloperApp[] newArray(int size) {
			return new MoreFromDeveloperApp[size];
		}
	};

}

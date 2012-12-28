package com.mapplas.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	private int id = 0;

	private String name = "";

	private String lastname = "";

	private String gender = "";

	private String birthdate = "";

	private String login = "";

	private String password = "";

	private String email = "";

	private String imei = "";

	private ArrayList<App> pinnedApps = new ArrayList<App>();

	private ArrayList<App> likedApps = new ArrayList<App>();

	private ArrayList<App> blockedApps = new ArrayList<App>();

	public User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public ArrayList<App> pinnedApps() {
		return pinnedApps;
	}

	public void setPinnedApps(ArrayList<App> pinnedApps) {
		this.pinnedApps = pinnedApps;
	}

	public ArrayList<App> likedApps() {
		return likedApps;
	}

	public void setLikedApps(ArrayList<App> likedApps) {
		this.likedApps = likedApps;
	}

	public ArrayList<App> blockedApps() {
		return blockedApps;
	}

	public void setBlockedApps(ArrayList<App> blockedApps) {
		this.blockedApps = blockedApps;
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
		dest.writeString(this.lastname);
		dest.writeString(this.gender);
		dest.writeString(this.birthdate);
		dest.writeString(this.login);
		dest.writeString(this.password);
		dest.writeString(this.email);
		dest.writeString(this.imei);
		dest.writeTypedList(this.pinnedApps);
		dest.writeTypedList(this.likedApps);
		dest.writeTypedList(this.blockedApps);
	}

	public User(Parcel parcel) {
		this.id = parcel.readInt();
		this.name = parcel.readString();
		this.lastname = parcel.readString();
		this.gender = parcel.readString();
		this.birthdate = parcel.readString();
		this.login = parcel.readString();
		this.password = parcel.readString();
		this.email = parcel.readString();
		this.imei = parcel.readString();
		parcel.writeTypedList(this.pinnedApps);
		parcel.writeTypedList(this.likedApps);
		parcel.writeTypedList(this.blockedApps);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

}

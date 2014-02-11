package com.mapplas.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.Geofence;

public class GeoFence implements Parcelable {

	public static final String TABLE_NAME = "geoFences";

	private int id;

	private double latitude;

	private double longitude;

	private float radius;

	private long expiration;

	private int transition;
	

//	public enum TransitionType {
//		ENTER("ENTER", 1), QUIT("QUIT", 2);
//
//		private String stringValue;
//
//		private int intValue;
//
//		private TransitionType(String toString, int value) {
//			stringValue = toString;
//			intValue = value;
//		}
//
//		@Override
//		public String toString() {
//			return stringValue;
//		}
//	}

	public GeoFence() {
	}

	public GeoFence(int id, double latitude, double longitude, float radius, long expiration, int transition) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.expiration = expiration;
		this.transition = transition;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String lat) {
		this.latitude = Double.parseDouble(lat);
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String lon) {
		this.longitude = Double.parseDouble(lon);
	}

	public float getRadius() {
		return this.radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public long getExpirationDuration() {
		return this.expiration;
	}
	
	public void setExpirationDuration(int expiration) {
		this.expiration = expiration;
	}

	public int getTransitionType() {
		return this.transition;
	}

	public void setTransitionType(int tran_type) {
		this.transition = tran_type;
	}

	/**
	 * Creates a Location Services Geofence object from a Geofence.
	 * 
	 * @return A Geofence object
	 */
	public Geofence toGeofence() {
		return new Geofence.Builder().setRequestId(String.valueOf(getId())).setTransitionTypes(this.transition).setCircularRegion(getLatitude(), getLongitude(), getRadius()).setExpirationDuration(this.expiration).build();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeFloat(this.radius);
		dest.writeLong(this.expiration);
		dest.writeInt(this.transition);
	}

	public GeoFence(Parcel parcel) {
		this.id = parcel.readInt();
		this.latitude = parcel.readDouble();
		this.longitude = parcel.readDouble();
		this.radius = parcel.readFloat();
		this.expiration = parcel.readLong();
		this.transition = parcel.readInt();
	}

	public static final Parcelable.Creator<GeoFence> CREATOR = new Parcelable.Creator<GeoFence>() {

		@Override
		public GeoFence createFromParcel(Parcel source) {
			return new GeoFence(source);
		}

		@Override
		public GeoFence[] newArray(int size) {
			return new GeoFence[size];
		}
	};

}

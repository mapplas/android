package com.mapplas.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "geoFences")
public class GeoFence implements Parcelable, Unit {

	public static final String TABLE_NAME = "geoFences";

	@DatabaseField(id = true)
	private String id;

	@DatabaseField
	private double latitude;

	@DatabaseField
	private double longitude;

	@DatabaseField()
	private float radius;

	@DatabaseField()
	private long expiration;

	@DatabaseField()
	private int transition;

	public GeoFence() {
	}
	
	public GeoFence(String id, double latitude, double longitude, float radius, long expiration, int transition) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.expiration = expiration;
		this.transition = transition;
	}

	public String getId() {
		return this.id;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public float getRadius() {
		return this.radius;
	}

	public long getExpirationDuration() {
		return this.expiration;
	}

	public int getTransitionType() {
		return this.transition;
	}

	/**
	 * Creates a Location Services Geofence object from a Geofence.
	 * 
	 * @return A Geofence object
	 */
	public Geofence toGeofence() {
		return new Geofence.Builder().setRequestId(getId()).setTransitionTypes(this.transition).setCircularRegion(getLatitude(), getLongitude(), getRadius()).setExpirationDuration(this.expiration).build();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeFloat(this.radius);
		dest.writeLong(this.expiration);
		dest.writeInt(this.transition);
	}

	public GeoFence(Parcel parcel) {
		this.id = parcel.readString();
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

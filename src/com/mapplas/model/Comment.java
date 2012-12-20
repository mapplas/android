package com.mapplas.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

	// ---------------------------------------------------------------------------
	// Properties
	private int id = 0;

	private int idUser = 0;

	private int idLocalization = 0;

	private float rate = 0;

	private String date = "?";

	private String hour = "?";

	private String comment = "?";

	public Comment() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdLocalization() {
		return idLocalization;
	}

	public void setIdLocalization(int idLocalization) {
		this.idLocalization = idLocalization;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
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
		dest.writeInt(this.idUser);
		dest.writeInt(this.idLocalization);
		dest.writeFloat(this.rate);
		dest.writeString(this.date);
		dest.writeString(this.hour);
		dest.writeString(this.comment);
	}

	public Comment(Parcel parcel) {
		this.id = parcel.readInt();
		this.idUser = parcel.readInt();
		this.idLocalization = parcel.readInt();
		this.rate = parcel.readFloat();
		this.date = parcel.readString();
		this.hour = parcel.readString();
		this.comment = parcel.readString();
	}

	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {

		@Override
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
		}

		@Override
		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};
}

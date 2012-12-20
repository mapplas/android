package com.mapplas.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

	// ---------------------------------------------------------------------------
	// Properties
	private int id = 0;

	private int idUser = 0;

	private int idLocalization = 0;

	private String date = "?";

	private String comment = "?";

	private String photo = "?";

	private String hour = "?";

	public Photo() {

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

	// public boolean FillLocalData()
	// {
	// this.auxLocalization =
	// MapplasActivity.GetLocalizationById(this.idLocalization);
	//
	// if(this.auxLocalization != null)
	// {
	// return true;
	// }
	//
	// return false;
	// }
	//
	// public boolean FillRemoteData()
	// {
	// try
	// {
	// String serverResponse =
	// NetRequests.LocationIdRequest(MapplasActivity.GetModel().currentLocation,
	// MapplasActivity.GetModel().currentUser.getId() + "", this.idLocalization
	// + "");
	//
	// JsonParser jp = new JsonParser();
	// this.auxLocalization = jp.ParseLocalization(serverResponse);
	// }catch (Exception exc)
	// {
	// return false;
	// }
	//
	// if(this.auxLocalization != null)
	// {
	// return true;
	// }
	//
	// return false;
	// }
	//
	// public boolean FillData()
	// {
	// if(this.FillLocalData())
	// {
	// return true;
	// }else
	// {
	// if(this.FillRemoteData())
	// {
	// return true;
	// }
	// }
	//
	// return false;
	// }

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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
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
		dest.writeInt(this.idUser);
		dest.writeInt(this.idLocalization);
		dest.writeString(this.date);
		dest.writeString(this.comment);
		dest.writeString(this.photo);
		dest.writeString(this.hour);
	}

	public Photo(Parcel parcel) {
		this.id = parcel.readInt();
		this.idUser = parcel.readInt();
		this.idLocalization = parcel.readInt();
		this.date = parcel.readString();
		this.comment = parcel.readString();
		this.photo = parcel.readString();
		this.hour = parcel.readString();
	}

	public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {

		@Override
		public Photo createFromParcel(Parcel source) {
			return new Photo(source);
		}

		@Override
		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};
}

package com.mapplas.model;


public class Photo {

	// ---------------------------------------------------------------------------
	// Properties
	private int id = 0;

	private int idUser = 0;

	private int idLocalization = 0;

	private String date = "?";

	private String comment = "?";

	private String photo = "?";

	private String hour = "?";

	private App auxLocalization = null;

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

	public App getAuxLocalization() {
		return auxLocalization;
	}

	public void setAuxLocalization(App auxLocalization) {
		this.auxLocalization = auxLocalization;
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
}

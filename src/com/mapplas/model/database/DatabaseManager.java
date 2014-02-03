package com.mapplas.model.database;

import android.content.Context;

public class DatabaseManager {

	private static UserDatabase userDatabase = null;

//	private static GeoFenceDatabase geoFenceDatabase = null;

	static public UserDatabase user(Context context) {
		if(DatabaseManager.userDatabase == null) {
			DatabaseManager.userDatabase = new UserDatabase(context);
		}

		return DatabaseManager.userDatabase;
	}

//	static public GeoFenceDatabase geoFence(Context context) {
//		if(DatabaseManager.geoFenceDatabase == null) {
//			DatabaseManager.geoFenceDatabase = new GeoFenceDatabase(context);
//		}
//
//		return DatabaseManager.geoFenceDatabase;
//	}

	static public void close() {
		if(DatabaseManager.userDatabase != null && DatabaseManager.userDatabase.isOpen()) {
			DatabaseManager.userDatabase.close();
		}

//		if(DatabaseManager.geoFenceDatabase != null && DatabaseManager.geoFenceDatabase.isOpen()) {
//			DatabaseManager.geoFenceDatabase.close();
//		}

		DatabaseManager.userDatabase = null;
//		DatabaseManager.geoFenceDatabase = null;

		System.gc();
	}
}

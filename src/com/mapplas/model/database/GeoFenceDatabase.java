//package com.mapplas.model.database;
//
//import java.sql.SQLException;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;
//import com.mapplas.model.GeoFence;
//
//public class GeoFenceDatabase extends OrmLiteSqliteOpenHelper {
//
//	public static String DATABASE_NAME = "GeoFences.db";
//
//	private static int DATABASE_VERSION = 1;
//
//	private Dao<GeoFence, Integer> geoFenceDao = null;
//
//	public GeoFenceDatabase(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//		try {
//			TableUtils.createTable(connectionSource, GeoFence.class);
//		} catch (SQLException e) {
//			// Log.e(this.getClass().getName(), "Can't create database " +
//			// database, e);
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
//		// Nothing to do
//	}
//
//	public Dao<GeoFence, Integer> getGeoFenceDao() throws SQLException {
//		if(this.geoFenceDao == null) {
//			this.geoFenceDao = this.getDao(GeoFence.class);
//		}
//		return this.geoFenceDao;
//	}
//
//	@Override
//	public void close() {
//		super.close();
//		this.geoFenceDao = null;
//	}
//
//}

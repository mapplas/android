package com.mapplas.model.database.repositories;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.mapplas.model.GeoFence;
import com.mapplas.model.Unit;
import com.mapplas.model.database.repositories.base.Repository;

public class GeoFenceRepository extends Repository {

	public GeoFenceRepository(Dao<GeoFence, Integer> dao, String tableName) {
		super(dao, tableName);
	}

	@SuppressWarnings("unchecked")
	public GeoFence get(String id) {
		QueryBuilder<GeoFence, Integer> queryBuilder = this.getDao().queryBuilder();

		List<GeoFence> fence = null;

		try {
			queryBuilder.where().eq("id", id);
			PreparedQuery<GeoFence> preparedQuery = queryBuilder.prepare();
			fence = this.getDao().query(preparedQuery);
		} catch (SQLException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
			return null;
		}

		if(fence.size() > 0) {
			return fence.get(0);
		}

		return null;
	}
	
	public void delete(String id) {
		Unit unit = this.get(id);
		GeoFence fence = (GeoFence)unit;
		if(fence != null) {
			this.delete(fence);
		}
	}

}

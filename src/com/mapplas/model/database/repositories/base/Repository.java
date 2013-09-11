package com.mapplas.model.database.repositories.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.mapplas.model.Unit;

public class Repository extends SqlRepository {

	@SuppressWarnings("rawtypes")
	private Dao dao = null;

	private String tableName;

	private ArrayList<Unit> createOrUpdateQueue = null;

	private final int createOrUpdateQueueMaxSize = 50;

	@SuppressWarnings("rawtypes")
	public Repository(Dao dao, String tableName) {
		this.dao = dao;
		this.tableName = tableName;

		this.createOrUpdateQueue = new ArrayList<Unit>();
	}

	public void empty() {
		try {
			this.getDao().executeRaw("DELETE FROM " + this.getTableName());
		} catch (SQLException e) {
//			Log.e(this.getClass().getName(), e.getMessage(), e);
			throw new RuntimeException();
		}
	}

	@SuppressWarnings("unchecked")
	public int create(Unit unit) throws SQLException {
		int result = -1;
		try {
			result = this.getDao().create(unit);
		} catch (IllegalStateException e) {
//			Log.d(this.getClass().getSimpleName(), e.getMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public CreateOrUpdateStatus createOrUpdate(Unit unit) throws SQLException {
		CreateOrUpdateStatus status = null;
		try {
			status = this.getDao().createOrUpdate(unit);
		} catch (IllegalStateException e) {
//			Log.d(this.getClass().getSimpleName(), e.getMessage());
		}
		return status;
	}

	public void createOrUpdateBatch(Unit unit) throws Exception {
		this.createOrUpdateQueue.add(unit);
		if(this.createOrUpdateQueue.size() >= this.createOrUpdateQueueMaxSize) {
			this.createOrUpdateFlush();
		}
	}

	@SuppressWarnings("unchecked")
	public void createOrUpdateFlush() throws Exception {
		if(this.createOrUpdateQueue.size() > 0) {
			this.getDao().callBatchTasks(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					for(Unit current : createOrUpdateQueue) {
						getDao().createOrUpdate(current);
					}
					return null;
				}
			});
			this.createOrUpdateQueue.clear();
		}
	}

	@SuppressWarnings("unchecked")
	public int update(Unit unit) throws SQLException {
		int result = -1;
		try {
			result = this.getDao().update(unit);
		} catch (IllegalStateException e) {
//			Log.d(this.getClass().getSimpleName(), e.getMessage());
		}
		return result;
	}

	protected String getTableName() {
		return this.tableName;
	}

	@SuppressWarnings("rawtypes")
	protected Dao getDao() {
		return this.dao;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Unit findFromKey(HashMap<String, Integer> keys) {
		QueryBuilder<Unit, Integer> queryBuilder = (this.getDao()).queryBuilder();

		Unit unit = null;
		try {
			Iterator<String> iterator = keys.keySet().iterator();

			while (iterator.hasNext()) {
				String key = iterator.next();
				queryBuilder.where().eq(key, keys.get(key));
			}

			PreparedQuery<Unit> preparedQuery = queryBuilder.prepare();
			unit = (Unit)(this.getDao().queryForFirst(preparedQuery));
		} catch (SQLException e) {
//			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
			return null;
		} catch (IllegalStateException e) {
//			Log.d(this.getClass().getSimpleName(), e.getMessage());
			return null;
		}

		return unit;
	}

	@SuppressWarnings("unchecked")
	public Unit findFromId(int id) {
		try {
			return (Unit)this.getDao().queryForId(id);
		} catch (SQLException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void delete(Unit unit) {
		try {
			this.getDao().delete(unit);
		} catch (SQLException e) {
//			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
			throw new RuntimeException();
		}
	}

}

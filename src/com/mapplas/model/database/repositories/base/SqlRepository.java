package com.mapplas.model.database.repositories.base;

import java.util.HashMap;

import com.j256.ormlite.dao.Dao;
import com.mapplas.model.Unit;


public abstract class SqlRepository {
	@SuppressWarnings("rawtypes")
	protected abstract Dao getDao();

	protected abstract Unit findFromKey(HashMap<String, Integer> keys);
}

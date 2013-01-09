package com.mapplas.model.database.repositories;

import com.j256.ormlite.dao.Dao;
import com.mapplas.model.AppOrderedList;
import com.mapplas.model.database.repositories.base.Repository;


public class AppListRepository extends Repository {

	public AppListRepository(Dao<AppOrderedList, Integer> dao, String tableName) {
		super(dao, tableName);
	}

}

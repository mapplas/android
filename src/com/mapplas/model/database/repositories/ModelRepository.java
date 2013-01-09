package com.mapplas.model.database.repositories;

import com.j256.ormlite.dao.Dao;
import com.mapplas.model.SuperModel;
import com.mapplas.model.database.repositories.base.Repository;


public class ModelRepository extends Repository {

	public ModelRepository(Dao<SuperModel, Integer> dao, String tableName) {
		super(dao, tableName);
	}

}

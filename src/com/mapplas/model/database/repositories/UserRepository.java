package com.mapplas.model.database.repositories;

import com.j256.ormlite.dao.Dao;
import com.mapplas.model.User;
import com.mapplas.model.database.repositories.base.Repository;


public class UserRepository extends Repository {

	public UserRepository(Dao<User, Integer> dao, String tableName) {
		super(dao, tableName);
	}
}

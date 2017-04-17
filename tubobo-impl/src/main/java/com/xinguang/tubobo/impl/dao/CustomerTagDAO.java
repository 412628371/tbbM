package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.CustomerTagEntity;

@Repository
public class CustomerTagDAO extends BasicDAO<CustomerTagEntity, String>{

	@Autowired
	public CustomerTagDAO(Datastore ds) {
		super(ds);
	}

}

package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.EmployeeStatisticsEntity;

@Repository
public class EmployeeStatisticsDAO extends BasicDAO<EmployeeStatisticsEntity, String> {

    @Autowired
    protected EmployeeStatisticsDAO(Datastore ds) {
        super(ds);
    }
	
}

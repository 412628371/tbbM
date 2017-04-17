package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.LoginLogEntity;

@Repository
public class LoginLogDAO extends BasicDAO<LoginLogEntity, String> {

    @Autowired
    protected LoginLogDAO(Datastore ds) {
        super(ds);
    }

}

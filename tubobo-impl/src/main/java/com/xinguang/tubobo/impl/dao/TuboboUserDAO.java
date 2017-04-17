package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.xinguang.tubobo.impl.entity.TuboboUserEntity;

@Repository
public class TuboboUserDAO extends BasicDAO<TuboboUserEntity, String> {

    @Autowired
    protected TuboboUserDAO(Datastore ds) {
        super(ds);
    }

}

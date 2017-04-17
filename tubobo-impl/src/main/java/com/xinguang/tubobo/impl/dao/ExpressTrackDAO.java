package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.ExpressTrackEntity;

@Repository
public class ExpressTrackDAO extends BasicDAO<ExpressTrackEntity, String> {

    @Autowired
    protected ExpressTrackDAO(Datastore ds) {
        super(ds);
    }

}

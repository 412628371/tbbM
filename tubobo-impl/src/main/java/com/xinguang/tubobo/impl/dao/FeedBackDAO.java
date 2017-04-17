package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.FeedBackEntity;

@Repository
public class FeedBackDAO extends BasicDAO<FeedBackEntity, String> {

    @Autowired
    protected FeedBackDAO(Datastore ds) {
        super(ds);
    }
   
}

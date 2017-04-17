package com.xinguang.tubobo.impl.alisms.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.alisms.entity.AppEntity;


@Repository
public class AppDao extends BasicDAO<AppEntity, String> {

    @Autowired
    protected AppDao(Datastore ds) {
        super(ds);
    }

}

package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.WayBillREntity;

@Repository
public class WayBillRDAO extends BasicDAO<WayBillREntity, String> {

    @Autowired
    protected WayBillRDAO(Datastore ds) {
        super(ds);
    }

}

package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.AddressInfoEntity;

@Repository
public class AddressInfoDAO extends BasicDAO<AddressInfoEntity, String> {

    @Autowired
    protected AddressInfoDAO(Datastore ds) {
        super(ds);
    }

}

package com.xinguang.tubobo.impl.alisms.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.alisms.entity.AliSmsRecordEntity;


@Repository
public class AliSmsRecordDao extends BasicDAO<AliSmsRecordEntity, String> {

    @Autowired
    protected AliSmsRecordDao(Datastore ds) {
        super(ds);
    }

}

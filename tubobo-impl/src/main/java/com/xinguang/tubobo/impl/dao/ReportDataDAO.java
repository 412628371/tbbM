package com.xinguang.tubobo.impl.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.entity.ReportDataEntity;

@Repository
public class ReportDataDAO extends BasicDAO<ReportDataEntity, String> {

    @Autowired
    protected ReportDataDAO(Datastore ds) {
        super(ds);
    }
}

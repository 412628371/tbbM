package com.xinguang.tubobo.impl.alisms.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinguang.tubobo.impl.alisms.entity.AliSmsTemplateEntity;

@Repository
public class AliSmsTemplateDao extends BasicDAO<AliSmsTemplateEntity, String> {

    @Autowired
    protected AliSmsTemplateDao(Datastore ds) {
        super(ds);
    }
    
}

package com.hzmux.hzcms.common.mongodb;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

public class MongoCredentialFactoryBean extends AbstractFactoryBean<MongoCredential> {
    private String user;
    private String dbName;
    private String pwd;
    @Override
    public Class<?> getObjectType() {
        return MongoClient.class;
    }

    @Override
    protected MongoCredential createInstance() throws Exception {
        MongoCredential credential = MongoCredential.createCredential(user, dbName, pwd.toCharArray());
        return credential;
    }

    public String getUser() {
        return user;
    }


    public String getPwd() {
        return pwd;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

}

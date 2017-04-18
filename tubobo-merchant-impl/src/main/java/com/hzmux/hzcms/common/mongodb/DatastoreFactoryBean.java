package com.hzmux.hzcms.common.mongodb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClient;

public class DatastoreFactoryBean extends AbstractFactoryBean<Datastore> {

    private Morphia morphia;
    private MongoClient mongoClient;
    private String dbName;
    private boolean ensureIndexes = false;
    private boolean ensureCaps = false;
    
    @Override
    public Class<?> getObjectType() {
        return Datastore.class;
    }

    @Override
    protected Datastore createInstance() throws Exception {
        Datastore ds = morphia.createDatastore(mongoClient, dbName);
        if(ensureIndexes) {
            ds.ensureIndexes();
        }
        if(ensureCaps) {
            ds.ensureCaps();
        }
        return ds;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public String getDbName() {
        return dbName;
    }

    public boolean isEnsureIndexes() {
        return ensureIndexes;
    }

    public boolean isEnsureCaps() {
        return ensureCaps;
    }

    public void setMorphia(Morphia morphia) {
        this.morphia = morphia;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setEnsureIndexes(boolean ensureIndexes) {
        this.ensureIndexes = ensureIndexes;
    }

    public void setEnsureCaps(boolean ensureCaps) {
        this.ensureCaps = ensureCaps;
    }

}

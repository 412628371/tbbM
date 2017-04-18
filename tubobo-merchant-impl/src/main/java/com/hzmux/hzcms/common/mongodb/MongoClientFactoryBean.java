package com.hzmux.hzcms.common.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class MongoClientFactoryBean extends AbstractFactoryBean<MongoClient> {
    private String[] serverStrings;
    private List<MongoCredential> mongoCredentials;
    private MongoClientOptions mongoOptions;
    private boolean readSecondary = false;
    private WriteConcern writeConcern = WriteConcern.SAFE;
    @Override
    public Class<?> getObjectType() {
        // TODO Auto-generated method stub
        return MongoClient.class;
    }

    @Override
    protected MongoClient createInstance() throws Exception {
        MongoClient client = initMongoClient();
        if(readSecondary) {
            client.setReadPreference(ReadPreference.secondaryPreferred());
        }
        client.setWriteConcern(writeConcern);
        return client;
        
    }

    private MongoClient initMongoClient() throws Exception {
        MongoClient client = null;
        List<ServerAddress> serverList = getServerList();
        
        List<MongoCredential> credentialsList = mongoCredentials==null?new ArrayList<MongoCredential>():mongoCredentials;
        
        if(serverList.size()==0) {
            client = new MongoClient();
        } else if(serverList.size()==1) {
            if(null!=mongoOptions) {
                client = new MongoClient(serverList.get(0),credentialsList, mongoOptions);
            } else {
                client = new MongoClient(serverList.get(0), credentialsList);
            }
        } else {
            if(null!=mongoOptions) {
                client = new MongoClient(serverList,credentialsList, mongoOptions);
            } else {
                client = new MongoClient(serverList,credentialsList);
            }
        }
        return client;
    }

    private List<ServerAddress> getServerList() throws Exception {
        List<ServerAddress> list = new ArrayList<>();
        try{
            for (String serverStr : serverStrings) {
                String[] temp = serverStr.split(":");
                String host = temp[0];
                if(temp.length>2) {
                    throw new IllegalArgumentException("Invalid server address string: " + serverStr);
                } else if(temp.length==2) {
                    list.add(new ServerAddress(host, Integer.valueOf(temp[1])));
                } else {
                    list.add(new ServerAddress(host));
                }
            }
        } catch(Exception e) {
            throw new Exception("Error while converting serverString to ServerAddressList", e);
        }
        return list;
    }

    public String[] getServerStrings() {
        return serverStrings;
    }

    public void setServerStrings(String[] serverStrings) {
        this.serverStrings = serverStrings;
    }

    public List<MongoCredential> getMongoCredentials() {
        return mongoCredentials;
    }

    public void setMongoCredentials(List<MongoCredential> mongoCredentials) {
        this.mongoCredentials = mongoCredentials;
    }

}

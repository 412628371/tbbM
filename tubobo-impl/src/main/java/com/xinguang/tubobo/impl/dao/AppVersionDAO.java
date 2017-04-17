package com.xinguang.tubobo.impl.dao;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.AppVersionEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;


@Repository
public class AppVersionDAO extends BasicDAO<AppVersionEntity, String> {

    @Autowired
    protected AppVersionDAO(Datastore ds) {
        super(ds);
    }
    
    public AppVersionEntity findByNameAndType(String appName,String appType){
    	Query<AppVersionEntity> q = createQuery();
		q.field("appName").equal(appName);
		q.field("appType").equal(appType);
		q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
		return findOne(q);
    }

    public List<AppVersionEntity> findAllVersion(){
    	Query<AppVersionEntity> q = createQuery();
    	return q.asList();
    }
    
    public void updateVersion(AppVersionEntity entity) {
    	UpdateOperations<AppVersionEntity> ops = createUpdateOperations();
        ops.set("version", entity.getVersion());
        ops.set("minVersion", entity.getMinVersion());
        ops.set("url", entity.getUrl());
        if (StringUtils.isNotBlank(entity.getDescription())) {
        	ops.set("description", entity.getDescription());
		}
        update(createQuery()
        		.field("id").equal(entity.getId())
        		.field("delFlag").equal(EnumDelFlag.NORMAL.getValue()), ops);
    }

    public void disableOldVersion(String appName,String appType) {
    	UpdateOperations<AppVersionEntity> ops = createUpdateOperations();
    	ops.set("delFlag", EnumDelFlag.DELETE.getValue());
    	update(createQuery()
    			.field("appName").equal(appName)
    			.field("appType").equal(appType)
    			.field("delFlag").equal(EnumDelFlag.NORMAL.getValue()), ops);
    }
    
    public Page<AppVersionEntity> find(Page<AppVersionEntity> page, AppVersionEntity entity) {
        Query<AppVersionEntity> q = createQuery();

        if(StringUtils.isNotBlank(entity.getAppName())) {
            q.field("appName").equal(entity.getAppName());
        }
        if(StringUtils.isNotBlank(entity.getAppType())) {
        	q.field("appType").equal(entity.getAppType());
        }
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
}

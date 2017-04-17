package com.xinguang.tubobo.impl.alisms.service;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.impl.alisms.dao.AppDao;
import com.xinguang.tubobo.impl.alisms.entity.AppEntity;


@Service
public class AppService {

    @Autowired
    private AppDao appDao;
    
    public int saveOrUpdate(String appId,String appName){
    	AppEntity entity = get(appId);
    	if (entity == null) {
    		entity = new AppEntity();
    		entity.setAppId(appId);
    		entity.setAppName(appName);
    		appDao.save(entity);
    		return 1;
		}else {
			Query<AppEntity> query = appDao.createQuery();
			query.field("appId").equal(appId);

			UpdateOperations<AppEntity> ops = appDao.createUpdateOperations();
	        ops.set("appName", appName);
	        
	        return appDao.update(query, ops).getUpdatedCount();
		}
    }
    
    public AppEntity get(String id){
    	if (StringUtils.isBlank(id)) {
			return null;
		}
		return appDao.get(id);
    }

    public void delete(String appId){
    	appDao.deleteById(appId);
    }
    
    public Page<AppEntity> find(int pageNo,int pageSize, AppEntity entity) {
        Query<AppEntity> query = appDao.createQuery();
        query.order("-createDate");
        long count = query.countAll();
        query.offset((pageNo-1)*pageSize).limit(pageSize);
        return new Page<AppEntity>(pageNo, pageSize, count, query.asList());
    }

}

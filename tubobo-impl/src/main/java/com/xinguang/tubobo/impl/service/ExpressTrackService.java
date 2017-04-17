package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.List;

import com.hzmux.hzcms.common.utils.StringUtils;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.ExpressTrackEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.impl.dao.ExpressTrackDAO;

@Service
public class ExpressTrackService {
	
    @Autowired
    private ExpressTrackDAO expressTrackDAO;

    public void save(String dataId,String desc,Date date,StoreEntity store,EmployeeEntity operator){
    	ExpressTrackEntity entity = new ExpressTrackEntity();
    	entity.setId(IdGen.uuid());
    	entity.setDataId(dataId);
    	entity.setDesc(desc);
    	entity.setTime(date);
    	entity.setStore(store);
    	entity.setOperator(operator);
    	expressTrackDAO.save(entity);
    }
    
    public List<ExpressTrackEntity> findByDataId(String dataId){
    	if (StringUtils.isBlank(dataId)){
    		return null;
		}
    	Query<ExpressTrackEntity> q = expressTrackDAO.createQuery();
		q.field("dataId").equal(dataId);
		q.order("-time");
		return q.asList();
    }

}

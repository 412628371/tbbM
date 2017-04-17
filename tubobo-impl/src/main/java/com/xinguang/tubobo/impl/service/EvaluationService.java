package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.regex.Pattern;

import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.EvaluationEntity;
import com.xinguang.tubobo.impl.dao.EvaluationDAO;

@Service
public class EvaluationService extends BaseService{
	
    @Autowired
    private EvaluationDAO evaluationDAO;
    
    public EvaluationEntity save(String type, String dataId,String waybillNo, String userId, String name, String phone,
    		String evaluationStar, String evaluationTag, String evaluationComment){
    	EvaluationEntity entity = new EvaluationEntity();
    	entity.setId(IdGen.uuid());
    	entity.setType(type);
    	entity.setDataId(dataId);
    	entity.setWaybillNo(waybillNo);
    	entity.setUserId(userId);
    	entity.setName(name);
    	entity.setPhone(phone);
    	entity.setEvaluationStar(evaluationStar);
    	entity.setEvaluationTag(evaluationTag);
    	entity.setEvaluationComment(evaluationComment);
    	entity.setEvaluationTime(new Date());
    	evaluationDAO.save(entity);
    	return entity;
    }
    
    public EvaluationEntity get(String id){
    	return evaluationDAO.get(id);
    }
    
    public Page<EvaluationEntity> find(Page<EvaluationEntity> page, EvaluationEntity entity) {
    	Query<EvaluationEntity> q = evaluationDAO.createQuery();
    	if(StringUtils.isNotBlank(entity.getType())) {
    		q.field("type").equal(entity.getType());
    	}
        if(StringUtils.isNotBlank(entity.getWaybillNo())) {
            q.field("waybillNo").equal(entity.getWaybillNo());
        }
        if(StringUtils.isNotBlank(entity.getPhone())) {
        	q.field("phone").contains(Pattern.quote(entity.getPhone()));
        }
        if(StringUtils.isNotBlank(entity.getEvaluationStar())) {
        	q.field("evaluationStar").equal(entity.getEvaluationStar());
        }
        if(null != entity.getCreateDate()) {
            q.field("evaluationTime").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("evaluationTime").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        q.order("-evaluationTime");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
}
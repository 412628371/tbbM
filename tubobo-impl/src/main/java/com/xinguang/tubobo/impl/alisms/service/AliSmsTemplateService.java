package com.xinguang.tubobo.impl.alisms.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.impl.alisms.dao.AliSmsTemplateDao;
import com.xinguang.tubobo.impl.alisms.entity.AliSmsTemplateEntity;

@Service
public class AliSmsTemplateService {

    @Autowired
    private AliSmsTemplateDao aliSmsTemplateDao;
    
    public int saveOrUpdate(AliSmsTemplateEntity entity){
    	if (StringUtils.isBlank(entity.getAliSmsId()) || StringUtils.isBlank(entity.getAliSignName())
    			|| StringUtils.isBlank(entity.getSmsType()) || StringUtils.isBlank(entity.getName())) {
    		return 0;
		}
    	AliSmsTemplateEntity exist = get(entity.getId());
    	if (exist == null) {
    		entity.setId(IdGen.uuid());
    		entity.setCreateDate(new Date());
    		aliSmsTemplateDao.save(entity);
    		return 1;
		}else {
			Query<AliSmsTemplateEntity> query = aliSmsTemplateDao.createQuery();
			query.field("id").equal(entity.getId());

			UpdateOperations<AliSmsTemplateEntity> ops = aliSmsTemplateDao.createUpdateOperations();
			ops.set("aliSmsId", entity.getAliSmsId());
			ops.set("aliSignName", entity.getAliSignName());
			ops.set("smsType", entity.getSmsType());
			ops.set("name", entity.getName());
			ops.set("content", entity.getContent());
			ops.set("updateDate", new Date());
			
			return aliSmsTemplateDao.update(query, ops).getUpdatedCount();
		}
		
    }
    
    public AliSmsTemplateEntity get(String id){
    	if (StringUtils.isBlank(id)) {
    		return null;
    	}
    	return aliSmsTemplateDao.get(id);
    }
    
    public List<AliSmsTemplateEntity> findBySmsType(String smsType){
    	Query<AliSmsTemplateEntity> query = aliSmsTemplateDao.createQuery();
		query.field("smsType").equal(smsType);
    	query.order("-updateDate");
    	return query.asList();
    }

    public AliSmsTemplateEntity findOneBySmsType(String smsType){
    	Query<AliSmsTemplateEntity> query = aliSmsTemplateDao.createQuery();
    	query.field("smsType").equal(smsType);
    	query.order("-updateDate");
    	List<AliSmsTemplateEntity> list = query.asList();
    	if (list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
    }

    public Page<AliSmsTemplateEntity> find(int pageNo,int pageSize, String smsType) {
        Query<AliSmsTemplateEntity> query = aliSmsTemplateDao.createQuery();
        if(StringUtils.isNotBlank(smsType)) {
        	query.field("smsType").equal(smsType);
        }
        query.order("-updateDate");
        long count = query.countAll();
        query.offset((pageNo-1)*pageSize).limit(pageSize);
        return new Page<AliSmsTemplateEntity>(pageNo, pageSize, count, query.asList());
    }
    
    public void delete(String templateId){
    	aliSmsTemplateDao.deleteById(templateId);
    }
}

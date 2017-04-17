package com.xinguang.tubobo.impl.dao;

import java.util.Date;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.ValidateCodeEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;

@Repository
public class ValidateCodeDAO extends BasicDAO<ValidateCodeEntity, String> {

    @Autowired
    protected ValidateCodeDAO(Datastore ds) {
        super(ds);
    }

    public int disableOldCodes(String phone,String type) {
    	UpdateOperations<ValidateCodeEntity> ops = createUpdateOperations();
        ops.set("delFlag", EnumDelFlag.DELETE.getValue());
        ops.set("updateDate", new Date());
        return update(createQuery()
        		.field("phone").equal(phone)
        		.field("type").equal(type)
        		.field("delFlag").equal(EnumDelFlag.NORMAL.getValue()), ops).getUpdatedCount();
    }
    
    public ValidateCodeEntity findUnValidateCodeByPhoneAndType(String phone,String type) {
    	Query<ValidateCodeEntity> q = createQuery();
    	q.field("phone").equal(phone);
    	q.field("type").equal(type);
    	q.field("result").equal(ValidateCodeEntity.RESULT_INIT);
    	q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
    	return findOne(q);
    }
    
    public int updateResultStatus(String id,String result) {
    	UpdateOperations<ValidateCodeEntity> ops = createUpdateOperations();
        ops.set("result", result);
        ops.set("updateDate", new Date());
        return update(createQuery()
        		.field("id").equal(id)
        		.field("delFlag").equal(EnumDelFlag.NORMAL.getValue()), ops).getUpdatedCount();
    }
    
    public Page<ValidateCodeEntity> find(Page<ValidateCodeEntity> page, ValidateCodeEntity code) {
        Query<ValidateCodeEntity> q = createQuery();
        if(StringUtils.isNotBlank(code.getPhone())) {
            q.field("phone").equal(code.getPhone());
        }
        if(StringUtils.isNotBlank(code.getType())) {
        	q.field("type").equal(code.getType());
        }
        q.order("-createDate");
        long count = q.countAll();
        page.setCount(count);
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        QueryResults<ValidateCodeEntity> result = find(q);
        return new Page<>(page.getPageNo(), page.getPageSize(), count, result.asList());
    }
}

package com.xinguang.tubobo.impl.dao;

import java.util.regex.Pattern;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;

@Repository
public class EmployeeDAO extends BasicDAO<EmployeeEntity, String> {

    @Autowired
    protected EmployeeDAO(Datastore ds) {
        super(ds);
    }

    public Page<EmployeeEntity> find(Page<EmployeeEntity> page,
            EmployeeEntity entity) {
        Query<EmployeeEntity> q = createQuery();
        
        if(StringUtils.isNotBlank(entity.getName())) {
            q.field("name").contains(Pattern.quote(entity.getName()));
        }
        if(StringUtils.isNotBlank(entity.getPhone())) {
            q.field("phone").contains(Pattern.quote(entity.getPhone()));
        }
        
        if(StringUtils.isNotBlank(entity.getEmployeeType())) {
            q.field("employeeType").equal(entity.getEmployeeType());
        }
        if(null != entity.getBelongStore() && StringUtils.isNotBlank(entity.getBelongStore().getStoreId())) {
            q.field("belongStore").equal(entity.getBelongStore());
        }
        if(null != entity.getSignStore() && StringUtils.isNotBlank(entity.getSignStore().getStoreId())) {
            q.field("signStore").equal(entity.getSignStore());
        }
        if(StringUtils.isNotBlank(entity.getDelFlag()) && entity.getDelFlag().equals(EnumDelFlag.DELETE.getValue())) {
            q.field("delFlag").equal(EnumDelFlag.DELETE.getValue());
        } else if (StringUtils.isNotBlank(entity.getDelFlag()) && entity.getDelFlag().equals(EnumDelFlag.NORMAL.getValue())){
            q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        }
        
        q.order("delFlag, -createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
	public int updatePassword(String id, String password) {
		UpdateOperations<EmployeeEntity> ops = createUpdateOperations();
        ops.set("password", password);
        return update(createQuery()
        		.field("id").equal(id)
        		.field("delFlag").equal(EnumDelFlag.NORMAL.getValue()), ops).getUpdatedCount();
	}
}

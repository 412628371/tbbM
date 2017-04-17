package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.CustomerTagEntity;
import com.xinguang.tubobo.impl.entity.EmployeeEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.impl.dao.CustomerTagDAO;

@Service
public class CustomerTagService {

	@Autowired
	private CustomerTagDAO customerTagDAO;
	
    public void save(StoreEntity store, EmployeeEntity employee, String waybillNo, String phone, String tags, String comment){
		CustomerTagEntity entity = new CustomerTagEntity();
		entity.setId(IdGen.uuid());
		entity.setCreateDate(new Date());
		entity.setWaybillNo(waybillNo);
		entity.setPhone(phone);
		entity.setTags(tags);
		entity.setComment(comment);
		entity.setStore(store);
		entity.setOperator(employee);
		customerTagDAO.save(entity);
	}
	
	public List<CustomerTagEntity> findByPhone(String phone){
    	Query<CustomerTagEntity> q = customerTagDAO.createQuery();
    	q.field("phone").equal(phone);
    	return q.asList();
    }

	public List<CustomerTagEntity> findByWaybillNo(String waybillNo){
    	Query<CustomerTagEntity> q = customerTagDAO.createQuery();
    	q.field("waybillNo").equal(waybillNo);
    	return q.asList();
    }
	
    public Page<CustomerTagEntity> find(Page<CustomerTagEntity> page, CustomerTagEntity entity) {
    	Query<CustomerTagEntity> q = customerTagDAO.createQuery();
    	if(null != entity.getStore() && StringUtils.isNotBlank(entity.getStore().getStoreId())) {
            q.field("store").equal(entity.getStore());
        }
        if(StringUtils.isNotBlank(entity.getWaybillNo())) {
            q.field("waybillNo").equal(entity.getWaybillNo());
        }
        if(StringUtils.isNotBlank(entity.getPhone())) {
        	q.field("phone").contains(Pattern.quote(entity.getPhone()));
        }
        if(null != entity.getCreateDate()) {
            q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
        }
        if(null != entity.getUpdateDate()) {
        	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
}

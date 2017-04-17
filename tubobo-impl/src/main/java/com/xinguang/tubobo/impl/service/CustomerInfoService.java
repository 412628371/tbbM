package com.xinguang.tubobo.impl.service;

import java.util.Date;
import java.util.regex.Pattern;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.entity.CustomerInfoEntity;
import com.xinguang.tubobo.impl.entity.StoreEntity;
import com.xinguang.tubobo.api.enums.EnumDelFlag;
import com.xinguang.tubobo.impl.dao.CustomerInfoDAO;

@Service
public class CustomerInfoService {
	
    @Autowired
    private CustomerInfoDAO customerInfoDAO;

    public void save(CustomerInfoEntity entity,String createBy){
		entity.setId(IdGen.uuid());
		entity.setCreateBy(createBy);
		entity.setCreateDate(new Date());
		entity.setDelFlag(EnumDelFlag.NORMAL.getValue());
		customerInfoDAO.save(entity);
    }
    
    public int update(CustomerInfoEntity entity,String updateBy){
		UpdateOperations<CustomerInfoEntity> ops = customerInfoDAO.createUpdateOperations();
		
		ops.set("name", entity.getName());
		ops.set("operatorId", entity.getOperatorId());
		ops.set("operatorName", entity.getOperatorName());
		ops.set("provinceCode", entity.getProvinceCode());
		ops.set("cityCode", entity.getCityCode());
		ops.set("areaCode", entity.getAreaCode());
		ops.set("provinceName", entity.getProvinceName());
		ops.set("cityName", entity.getCityName());
		ops.set("areaName", entity.getAreaName());
		ops.set("detailAddress", entity.getDetailAddress());
		ops.set("updateBy", updateBy);
		ops.set("updateDate", new Date());
		
		return customerInfoDAO.update(customerInfoDAO.createQuery()
				.field("id").equal(entity.getId())
				.field("phone").equal(entity.getPhone())
				.field("store").equal(entity.getStore())
				, ops).getUpdatedCount();
    }
    
    public Page<CustomerInfoEntity> find(Page<CustomerInfoEntity> page, CustomerInfoEntity entity) {
        Query<CustomerInfoEntity> q = customerInfoDAO.createQuery();
        if(null != entity) {
            if(StringUtils.isNotBlank(entity.getPhone())) {
                q.field("phone").contains(Pattern.quote(entity.getPhone()));
            }
            if(StringUtils.isNotBlank(entity.getName())) {
            	q.field("name").contains(Pattern.quote(entity.getName()));
            }
            if(null != entity.getStore() && StringUtils.isNotBlank(entity.getStore().getStoreId())) {
                q.field("store").equal(entity.getStore());
            }
            if(StringUtils.isNotBlank(entity.getOperatorId())) {
            	q.field("operatorId").equal(entity.getOperatorId());
            }
            if(null != entity.getCreateDate()) {
                q.field("createDate").greaterThanOrEq(DateUtils.getDateStart(entity.getCreateDate()));
            }
            if(null != entity.getUpdateDate()) {
            	q.field("createDate").lessThanOrEq(DateUtils.getDateEnd(entity.getUpdateDate()));
            }
        }
        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
        q.order("-createDate");
        page.setCount(q.countAll());
        q.offset(page.getFirstResult()).limit(page.getPageSize());
        page.setList(q.asList());
        return page;
    }
    
    public CustomerInfoEntity get(String id){
    	return customerInfoDAO.get(id);
    }

    public CustomerInfoEntity findByStoreAndPhone(StoreEntity store,String phone){
    	Query<CustomerInfoEntity> q = customerInfoDAO.createQuery();
        q.field("store").equal(store);
        q.field("phone").equal(phone);
        q.field("delFlag").equal(EnumDelFlag.NORMAL.getValue());
    	return customerInfoDAO.findOne(q);
    }
    
    public int deleteById(String id,String updateBy){
    	UpdateOperations<CustomerInfoEntity> ops = customerInfoDAO.createUpdateOperations();
        ops.set("delFlag", EnumDelFlag.DELETE.getValue());
        ops.set("updateBy", updateBy);
        ops.set("updateDate", new Date());
        return customerInfoDAO.update(customerInfoDAO.createQuery()
        		.field("id").equal(id), ops).getUpdatedCount();
    }
}

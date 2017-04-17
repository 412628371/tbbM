/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.rider.service;

import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.api.enums.EnumMsgReceiverSetting;
import com.xinguang.tubobo.impl.rider.dao.RiderSettingsDao;
import com.xinguang.tubobo.impl.rider.entity.RiderSettingsEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RiderSettingsService extends BaseService {

	@Autowired
	private RiderSettingsDao riderSettingsDao;

	public RiderSettingsEntity get(String id){
		return riderSettingsDao.get(id);
	}

	public RiderSettingsEntity findByUserId(String userId){
		if (StringUtils.isBlank(userId)) return null;
		String sqlString = "select * from tubobo_rider_settings where user_id = :p1 and del_flag = '0' ";
		List<RiderSettingsEntity> list = riderSettingsDao.findBySql(sqlString, new Parameter(userId), RiderSettingsEntity.class);
		if (list != null && list.size() > 0){
			return list.get(0);
		}else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public RiderSettingsEntity saveOrUpdate(RiderSettingsEntity entity){
		if (StringUtils.isBlank(entity.getId())){
			entity.setId(IdGen.uuid());
			entity.setCreateDate(new Date());
			entity.setDelFlag(RiderSettingsEntity.DEL_FLAG_NORMAL);
		}else {
			entity.setUpdateDate(new Date());
		}
		if (StringUtils.isBlank(entity.getOnDuty())){
			entity.setOnDuty(EnumMsgReceiverSetting.ACCEPT.getValue());
		}
		riderSettingsDao.save(entity);
		return entity;
	}

	@Transactional(readOnly = false)
	public int updateuOnDuty(String userId,String onDuty) {
		String sqlString = "update tubobo_rider_settings set on_duty = :p1, update_date = :p2 where user_id = :p3 and del_flag = '0' ";
		return riderSettingsDao.updateBySql(sqlString, new Parameter(onDuty,new Date(),userId));
	}
}

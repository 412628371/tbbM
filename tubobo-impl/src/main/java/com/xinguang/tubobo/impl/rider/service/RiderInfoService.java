/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.rider.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.api.enums.EnumAuthentication;
import com.xinguang.tubobo.api.enums.EnumMsgReceiverSetting;
import com.xinguang.tubobo.impl.rider.dao.RiderInfoDao;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import com.xinguang.tubobo.impl.rider.entity.RiderSettingsEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RiderInfoService extends BaseService {

	@Autowired
	private RiderInfoDao riderInfoDao;
	@Autowired
	private RiderSettingsService riderSettingsService;

	public RiderInfoEntity findByUserId(String userId){
		if (StringUtils.isBlank(userId)) return null;
		String sqlString = "select * from tubobo_rider_info where del_flag = '0' and user_id = :p1 ";
		List<RiderInfoEntity> list = riderInfoDao.findBySql(sqlString, new Parameter(userId), RiderInfoEntity.class);
		if (list != null && list.size() > 0){
			return list.get(0);
		}else {
			return null;
		}
	}

	/**
	 * 申请骑士
	 */
	@Transactional(readOnly = false)
	public RiderInfoEntity riderApply(String userId,RiderInfoEntity rider) {
		Date now = new Date();
		rider.setId(IdGen.uuid());
		rider.setDelFlag(RiderInfoEntity.DEL_FLAG_NORMAL);
		rider.setCreateDate(now);
		rider.setUserId(userId);

		rider.setRiderStatus(EnumAuthentication.APPLY.getValue());
		rider.setApplyDate(now);

		riderInfoDao.save(rider);

		//保存推送设置
		RiderSettingsEntity settings = new RiderSettingsEntity();
		settings.setUserId(userId);
		settings.setOnDuty(EnumMsgReceiverSetting.ACCEPT.getValue());
		riderSettingsService.saveOrUpdate(settings);

		return rider;
	}

	/**
	 * 更新骑士认证信息
	 */
	@Transactional(readOnly = false)
	public int riderApplyInfoUpdate(String userId,String realName,String phone,String idCardNo,String idCardImageFront,String idCardImageBack) {
		String sqlString = "update tubobo_rider_info set rider_status = :p1, update_date = :p2, real_name = :p3, phone = :p4, id_card_no = :p5, " +
				"id_card_image_front = :p6, id_card_image_back = :p7 where user_id = :p8 and del_flag = '0' ";
		return riderInfoDao.updateBySql(sqlString, new Parameter(EnumAuthentication.APPLY.getValue(),new Date(),realName,phone,idCardNo,idCardImageFront,idCardImageBack,userId));
	}

	/**
	 * 审核骑士状态
	 */
	@Transactional(readOnly = false)
	public int riderStatusVerify(String userId,String riderStatus,String updateBy) {
		if (EnumAuthentication.SUCCESS.getValue().equals(riderStatus)){
			String sqlString = "update tubobo_rider_info set rider_status = :p1, verify_date = :p2, update_by = :p3 where user_id = :p4 and del_flag = '0' ";
			return riderInfoDao.updateBySql(sqlString, new Parameter(riderStatus,new Date(),updateBy,userId));
		} else {
			String sqlString = "update tubobo_rider_info set rider_status = :p1, update_date = :p2, update_by = :p3 where user_id = :p4 and del_flag = '0' ";
			return riderInfoDao.updateBySql(sqlString, new Parameter(riderStatus,new Date(),updateBy,userId));
		}
	}

	/**
	 * 更新头像
	 */
	@Transactional(readOnly = false)
	public int updateHeadImage(String userId,String picUrl) {
		String sqlString = "update tubobo_rider_info set head_image = :p1, update_date = :p2 where user_id = :p3 and del_flag = '0' ";
		return riderInfoDao.updateBySql(sqlString, new Parameter(picUrl,new Date(),userId));
	}

	/**
	 * 骑士信息分页查询
	 */
	public Page<RiderInfoEntity> findRiderInfoPage(int pageNo, int pageSize, RiderInfoEntity entity){
		Parameter parameter = new Parameter();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from tubobo_rider_info where del_flag = '0' ");
		if (StringUtils.isNotBlank(entity.getRiderStatus())){
			sb.append("and rider_status = :rider_status ");
			parameter.put("rider_status", entity.getRiderStatus());
		}
		if (StringUtils.isNotBlank(entity.getPhone())){
			sb.append("and phone like :phone ");
			parameter.put("phone", "%"+entity.getPhone()+"%");
		}
		if (StringUtils.isNotBlank(entity.getRealName())){
			sb.append("and real_name like :real_name ");
			parameter.put("real_name", "%"+entity.getRealName()+"%");
		}
		if (null != entity.getCreateDate()){
			sb.append("and apply_date >= :create_date ");
			parameter.put("create_date", DateUtils.getDateStart(entity.getCreateDate()));
		}
		if (null != entity.getUpdateDate()){
			sb.append("and apply_date <= :update_date ");
			parameter.put("update_date", DateUtils.getDateEnd(entity.getUpdateDate()));
		}
		sb.append(" order by create_date desc ");
		return riderInfoDao.findPage(sb.toString(), parameter, RiderInfoEntity.class,pageNo,pageSize);
	}
}

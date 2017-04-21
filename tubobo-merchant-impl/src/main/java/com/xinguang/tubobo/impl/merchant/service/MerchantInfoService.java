/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.service.BaseService;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.dao.MerchantPushSettingsDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.dao.MerchantInfoDao;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MerchantInfoService extends BaseService {

	@Autowired
	private MerchantInfoDao merchantInfoDao;

	@Autowired
	MerchantPushSettingsDao merchantPushSettingsDao;

	@Cacheable(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	public MerchantInfoEntity findByUserId(String userId){
		if (StringUtils.isBlank(userId)) return null;
		String sqlString = "select * from tubobo_merchant_info where del_flag = '0' and user_id = :p1 ";
		List<MerchantInfoEntity> list = merchantInfoDao.findBySql(sqlString, new Parameter(userId), MerchantInfoEntity.class);
		if (list != null && list.size() > 0){
			return list.get(0);
		}else {
			return null;
		}
	}

	/**
	 * 申请商家
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public boolean merchantApply(String userId, MerchantInfoEntity entity) {
		entity.setUserId(userId);
		entity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
		entity.setCreateDate(new Date());
		entity.setUserId(userId);
		entity.setApplyDate(new Date());
		entity.setMerchantStatus(EnumAuthentication.APPLY.getValue());
		merchantInfoDao.save(entity);
		MerchantSettingsEntity settingsEntity = new MerchantSettingsEntity();
		settingsEntity.setUserId(userId);
		merchantPushSettingsDao.save(settingsEntity);
		return true;
	}

	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#entity.getUserId()")
	@Transactional(readOnly = false)
	public boolean merchantUpdate(MerchantInfoEntity entity){
		entity.setMerchantStatus(EnumAuthentication.APPLY.getValue());
		entity.setUpdateDate(new Date());
		merchantInfoDao.save(entity);
		return true;
	}

	/**
	 * 审核商家状态
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public int merchantStatusVerify(String userId,String merchantStatus,String updateBy) {
		int result;
		if (EnumAuthentication.SUCCESS.getValue().equals(merchantStatus)){
			String sqlString = "update tubobo_merchant_info set merchant_status = :p1, verify_date = :p2, update_by = :p3 where user_id = :p4 and del_flag = '0' ";
			result = merchantInfoDao.updateBySql(sqlString, new Parameter(merchantStatus,new Date(),updateBy,userId));
		} else {
			String sqlString = "update tubobo_merchant_info set merchant_status = :p1, update_date = :p2, update_by = :p3 where user_id = :p4 and del_flag = '0' ";
			 result = merchantInfoDao.updateBySql(sqlString, new Parameter(merchantStatus,new Date(),updateBy,userId));
		}
		merchantInfoDao.getSession().clear();
		return result;
	}

	/**
	 * 更新头像
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public int updateHeadImage(String userId,String picUrl) {

		String sqlString = "update tubobo_merchant_info set avatar_url = :p1, update_date = :p2 where user_id = :p3 and del_flag = '0' ";
		int result = merchantInfoDao.updateBySql(sqlString, new Parameter(picUrl,new Date(),userId));
		merchantInfoDao.getSession().flush();
		return result;
	}

	/**
	 * 商家信息分页查询
	 */
	public Page<MerchantInfoEntity> findMerchantInfoPage(int pageNo, int pageSize, MerchantInfoEntity entity){
		Parameter parameter = new Parameter();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from tubobo_merchant_info where del_flag = '0' ");
		if (StringUtils.isNotBlank(entity.getMerchantStatus())){
			sb.append("and merchant_status = :merchant_status ");
			parameter.put("merchant_status", entity.getMerchantStatus());
		}
		if (StringUtils.isNotBlank(entity.getPhone())){
			sb.append("and phone like :phone ");
			parameter.put("phone", "%"+entity.getPhone()+"%");
		}
		if (StringUtils.isNotBlank(entity.getMerchantName())){
			sb.append("and merchant_name like :merchant_name ");
			parameter.put("merchant_name", "%"+entity.getMerchantName()+"%");
		}
		if (StringUtils.isNotBlank(entity.getIdCardNo())){
			sb.append("and id_card_no = :id_card_no ");
			parameter.put("id_card_no", entity.getIdCardNo());
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
		return merchantInfoDao.findPage(sb.toString(), parameter, MerchantInfoEntity.class,pageNo,pageSize);
	}
}

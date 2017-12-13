/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantInfoRepository;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumIdentifyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MerchantInfoService extends BaseService {

	@Autowired
	private MerchantInfoRepository merchantInfoRepository;

	@Autowired
	MerchantSettingsService merchantPushSettingsService;

	/**
	 * 查询指定服务商id下的所有商家
	 * @return
	 */
	//@Cacheable(value= RedisCache.MERCHANT,key="'merchantInfo_'+#")
	public List<MerchantInfoEntity> findAllByProviderId(long providerId){
		return merchantInfoRepository.findAllByProviderIdAndDelFlag(providerId, MerchantInfoEntity.DEL_FLAG_NORMAL);
	}

	@Cacheable(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	public MerchantInfoEntity findByUserId(String userId){
		return merchantInfoRepository.findByUserIdAndDelFlag(userId, MerchantInfoEntity.DEL_FLAG_NORMAL);
	}

	public MerchantInfoEntity findByLicencesNo(String licencesNo){
		return merchantInfoRepository.findByShopLicencesNoAndDelFlag(licencesNo, MerchantInfoEntity.DEL_FLAG_NORMAL);
	}

    public List<MerchantInfoEntity> findAllByIdCardNo(String idCardNo){
        return merchantInfoRepository.findAllByIdCardNo(idCardNo, MerchantInfoEntity.DEL_FLAG_NORMAL);
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
		entity.setUpdateDate(new Date());
		entity.setUserId(userId);
		entity.setApplyDate(new Date());
//		entity.setMerchantStatus(EnumAuthentication.APPLY.getValue());
		merchantInfoRepository.save(entity);
		MerchantSettingsEntity settingsEntity = new MerchantSettingsEntity();
		settingsEntity.setUserId(userId);
		merchantPushSettingsService.updateSettings(userId,settingsEntity);
		return true;
	}

	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#entity.getUserId()")
	@Transactional(readOnly = false)
	public boolean merchantUpdate(MerchantInfoEntity entity){
		entity.setUpdateDate(new Date());
		entity.setDelFlag(MerchantInfoEntity.DEL_FLAG_NORMAL);
		merchantInfoRepository.save(entity);
		return true;
	}

	/**
	 * /**
	 * 审核状态,包括商家状态和货主状态
	 * 如果认证类型是货主，则只更新货主状态为status；
	 * 如果认证类型是商家，status为审核成功或冻结，则商家状态和货主状态均为成功或冻结，如果status为审核失败，则只更新商家状态为失败
	 * @param userId
	 * @param status
	 * @param updateBy
     * @return
     */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public int merchantStatusVerify(String userId,String status,String updateBy,String identifyType,String reason,Long merTypeId) {
		MerchantInfoEntity entity = merchantInfoRepository.findByUserIdAndDelFlag(userId, MerchantInfoEntity.DEL_FLAG_NORMAL);
		if (entity == null)
			return 0;
		int result;
		String merchantStatus = entity.getMerchantStatus();
		String consignorStatus = entity.getConsignorStatus();
		if (EnumIdentifyType.CONSIGNOR.getValue().equals(identifyType)){
			if (StringUtils.isBlank(consignorStatus)||EnumAuthentication.INIT.getValue().equals(consignorStatus)){
				return 0;
			}
			if (EnumAuthentication.FROZEN.getValue().equals(status)||
					EnumAuthentication.FAIL.getValue().equals(status)){
				if (!EnumAuthentication.INIT.getValue().equals(merchantStatus)){
					merchantStatus = status;
				}
			}
			consignorStatus = status;
		}else if (EnumIdentifyType.MERCHANT.getValue().equals(identifyType)){
			if (StringUtils.isBlank(merchantStatus)||
					EnumAuthentication.INIT.getValue().equals(merchantStatus) ){
				return 0;
			}
			if (EnumAuthentication.SUCCESS.getValue().equals(status)){
				consignorStatus = status;
			}
			if (EnumAuthentication.FROZEN.getValue().equals(status)){
				consignorStatus = status;
			}
			merchantStatus = status;
		}
		//判断审核通过时间有否已经存在
		Date verifyDate = entity.getVerifyDate();
		if(null == verifyDate){
			verifyDate = new Date();
		}
		result = merchantInfoRepository.updateVerifyStatus(userId, MerchantOrderEntity.DEL_FLAG_NORMAL, merchantStatus,consignorStatus,
																								new Date(), verifyDate, updateBy, reason,merTypeId);
		return result;
	}

	/**
	 * 更新头像
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public int updateHeadImage(String userId,String picUrl) {
		int result = merchantInfoRepository.updateHeadImage(userId, MerchantOrderEntity.DEL_FLAG_NORMAL, picUrl, new Date());
		return result;
	}

    /**
     * 商家信息查询
     */
    public List<MerchantInfoEntity> findMerchantInfoList(MerchantInfoEntity entity){
        return merchantInfoRepository.findAll(where(entity));
    }

	/**
	 * 商家信息分页查询
	 */
	public Page<MerchantInfoEntity> findMerchantInfoPage(int pageNo, int pageSize, MerchantInfoEntity entity){
		PageRequest pageRequest = new PageRequest(pageNo-1, pageSize,  new Sort(Sort.Direction.DESC, "createDate"));
		Page<MerchantInfoEntity> page = merchantInfoRepository.findAll(where(entity), pageRequest);
		return page;
	}

	private Specification<MerchantInfoEntity> where(final MerchantInfoEntity entity){
		return new Specification<MerchantInfoEntity>() {
			@Override
			public Predicate toPredicate(Root<MerchantInfoEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				list.add(criteriaBuilder.equal(root.get("delFlag").as(String.class), MerchantInfoEntity.DEL_FLAG_NORMAL));
				if(null != entity.getProviderId()){
					list.add(criteriaBuilder.equal(root.get("providerId").as(Long.class), entity.getProviderId()));
				}
				if (StringUtils.isNotBlank(entity.getMerchantStatus())){
					list.add(criteriaBuilder.equal(root.get("merchantStatus").as(String.class), entity.getMerchantStatus()));
				}
				if (StringUtils.isNotBlank(entity.getPhone())){
					list.add(criteriaBuilder.like(root.get("phone").as(String.class), entity.getPhone()));
				}
				if (StringUtils.isNotBlank(entity.getMerchantName())){
					list.add(criteriaBuilder.like(root.get("merchantName").as(String.class), entity.getMerchantName()));
				}
				if (StringUtils.isNotBlank(entity.getBdCode())){
					list.add(criteriaBuilder.equal(root.get("bdCode").as(String.class), entity.getBdCode()));
				}
				if (StringUtils.isNotBlank(entity.getIdCardNo())){
					list.add(criteriaBuilder.equal(root.get("idCardNo").as(String.class), entity.getIdCardNo()));
				}
				if (null != entity.getCreateDate()){
					list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate").as(Date.class), DateUtils.getDateStart(entity.getCreateDate())));
				}
				if (null != entity.getUpdateDate()){
					list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate").as(Date.class), DateUtils.getDateEnd(entity.getUpdateDate())));
				}
				if (StringUtils.isNotBlank(entity.getUserId())){
					list.add(criteriaBuilder.equal(root.get("userId").as(String.class), entity.getUserId()));
				}
				if (StringUtils.isNotBlank(entity.getAddressAdCode())){
					list.add(criteriaBuilder.like(root.get("addressAdCode").as(String.class), entity.getAddressAdCode()+"%"));
				}
				if (null!=entity.getMerTypeId()){
					list.add(criteriaBuilder.equal(root.get("merTypeId").as(Long.class),entity.getMerTypeId()));
				}
				return criteriaQuery.where(list.toArray(new Predicate[list.size()])).getRestriction();
			}
		};
	}

	/**
	 * 设置免密支付
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public int freePayPwdSet(String userId,boolean enable) {
		return merchantInfoRepository.freePayPwdSet(userId, MerchantOrderEntity.DEL_FLAG_NORMAL, enable);
	}

	/**
	 *修改支付密码是否设置标记位
	 * @param userId
	 * @return
     */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public int modifyPwdSetFlag(String userId) {
		int count = merchantInfoRepository.modifyPwdSetFlag(userId, MerchantOrderEntity.DEL_FLAG_NORMAL, true);
		logger.info("修改是否支付密码标志位，userId:{}",userId);
		return count;
	}

	/**
	 * 保存bd邀请码
	 * @param userId
	 * @param bdCode
	 * @return
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional(readOnly = false)
	public int updateDBCode(String userId, String bdCode){
		int count = merchantInfoRepository.updateDBCode(userId, MerchantOrderEntity.DEL_FLAG_NORMAL,bdCode,new Date(), new Date());
		return count;
	}

//	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
//	@Transactional
//	public boolean bindProvider(String userId,Long providerId, String providerName){
//		int count = merchantInfoRepository.bindProvider(userId, MerchantOrderEntity.DEL_FLAG_NORMAL, providerId,providerName,EnumBindStatusType.NOOPERATE.getValue(), EnumBindStatusType.SUCCESS.getValue(),null, new Date(), null);
//		return count == 1;
//	}

	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional
	public boolean bindProviderAgree(String userId,Long providerId){
		int count = merchantInfoRepository.bindProvider(userId, MerchantOrderEntity.DEL_FLAG_NORMAL, providerId,new Date());
		return count == 1;
	}

//	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
//	@Transactional
//	public boolean bindProviderReject(String userId,Long providerId,String providerName){
//		int count = merchantInfoRepository.bindProvider(userId, MerchantOrderEntity.DEL_FLAG_NORMAL, providerId, providerName,EnumBindStatusType.REJECT.getValue(), EnumBindStatusType.SUCCESS.getValue(),null, new Date(), null);
//		return count == 1;
//	}

	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantInfo_'+#userId")
	@Transactional
	public boolean unbindProvider(String userId,long providerId){
		int count = merchantInfoRepository.unbindProvider(userId, null, providerId, MerchantInfoEntity.DEL_FLAG_NORMAL,new Date());
		return count == 1;
	}
	public boolean  isPostMerchant(String userId){
		boolean flag=false;
		MerchantInfoEntity merchant = findByUserId(userId);
		if(null != merchant.getProviderId()){
			flag=true;
		}
		return flag;
	}
}

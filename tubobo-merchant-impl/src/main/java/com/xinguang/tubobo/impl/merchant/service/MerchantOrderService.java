/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.common.CodeGenerator;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumCancelReason;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.TaskCenterToMerchantServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.dao.MerchantOrderDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.handler.TimeoutTaskProducer;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class MerchantOrderService extends BaseService {
	@Autowired
	private MerchantOrderDao merchantOrderDao;
	@Autowired
	private CodeGenerator codeGenerator;

	@Autowired
	private MerchantInfoService merchantInfoService;

	@Autowired
	private TimeoutTaskProducer timeoutTaskProducer;
	@Autowired
	TaskCenterToMerchantServiceInterface taskCenterToMerchantServiceInterface;

	@Autowired
	private DeliveryFeeService deliveryFeeService;

	@Autowired
	private TbbAccountService tbbAccountService;

	@Autowired
	MerchantPushService pushService;

	public MerchantOrderEntity get(String id) {
		return merchantOrderDao.get(id);
	}

	public MerchantOrderEntity findByOrderNo(String orderNo){
		return merchantOrderDao.findByOrderNo(orderNo);
	}
	public MerchantOrderEntity findByOrderNoAndStatus(String orderNo,String orderStatus){
		return merchantOrderDao.findByOrderNoAndStatus(orderNo,orderStatus);
	}
	@Cacheable(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_'+#orderNo")
	public MerchantOrderEntity findByMerchantIdAndOrderNo(String merchantId, String orderNo){
		return merchantOrderDao.findByMerchantIdAndOrderNo(merchantId,orderNo);
	}

	/**
	 * 商家提交订单
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#userId+'_*'")
	@Transactional(readOnly = false)
	public String order(String userId,MerchantOrderEntity entity) throws MerchantClientException {
		MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
		double distance = deliveryFeeService.sumDeliveryDistance(userId,entity.getReceiverLatitude(),entity.getReceiverLongitude());
		String orderNo = codeGenerator.nextCustomerCode();
		entity.setOrderNo(orderNo);
		entity.setSenderName(ConvertUtil.handleNullString(infoEntity.getMerchantName()));
		entity.setSenderPhone(ConvertUtil.handleNullString(infoEntity.getPhone()));
		if (null != infoEntity.getLongitude()){
			entity.setSenderLongitude(infoEntity.getLongitude());
		}
		if (null != infoEntity.getLatitude()){
			entity.setSenderLatitude(infoEntity.getLatitude());
		}
		entity.setDeliveryDistance(distance);
		entity.setSenderAddressProvince(ConvertUtil.handleNullString(infoEntity.getAddressProvince()));
		entity.setSenderAddressCity(ConvertUtil.handleNullString(infoEntity.getAddressCity()));
		entity.setSenderAddressDistrict(ConvertUtil.handleNullString(infoEntity.getAddressDistrict()));
		entity.setSenderAddressStreet(ConvertUtil.handleNullString(infoEntity.getAddressStreet()));
		entity.setSenderAddressDetail(ConvertUtil.handleNullString(infoEntity.getAddressDetail()));
		if (entity.getDeliveryFee() != null){
			if (entity.getTipFee() == null){
				entity.setPayAmount(entity.getDeliveryFee());
			}else {
				entity.setPayAmount(entity.getDeliveryFee()+entity.getTipFee());
			}
		}
		entity.setOrderStatus(EnumMerchantOrderStatus.INIT.getValue());
		merchantOrderDao.save(entity);
		//将订单加入支付超时队列
		timeoutTaskProducer.sendMessage(orderNo);
		return orderNo;
	}

	/**
	 * 商家付款
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public void merchantPay(MerchantOrderDTO merchantOrderDTO,String merchantId,String orderNo,long payId) throws MerchantClientException {
		Date payDate = new Date();
		int count = merchantOrderDao.merchantPay(merchantId,orderNo,payId,payDate);
		if (count != 1){
			logger.error("用户支付，数据更新错误，userID：{}，orderNo:{}",merchantId,orderNo);
			throw new MerchantClientException(EnumRespCode.FAIL);
		}
		try {
			taskCenterToMerchantServiceInterface.merchantOrder(merchantOrderDTO);
		}catch (Exception e){
			logger.error("调用任务中心发单出错，orderNo:{}",orderNo,e);
		}

	}

	/**
	 * 商家取消订单
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public boolean cancelOrder(String merchantId,String orderNo){
		MerchantOrderEntity entity = merchantOrderDao.findByMerchantIdAndOrderNo(merchantId,orderNo);
		if (null == entity )
			return false;
		if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
			boolean cancelResult = merchantOrderDao.merchantCancel(merchantId,orderNo,
					EnumCancelReason.PAY_MERCHANT.getValue());
			if (!cancelResult){
				logger.error("商家取消订单，更改订单状态出错，userId: "+merchantId+" orderNo: "+orderNo);
			}
			return cancelResult;
		}

		if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
			boolean result = taskCenterToMerchantServiceInterface.meachantCancelOrder(orderNo);
			if (result){
				if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
					PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(entity.getPayId(),
							MerchantConstants.PAY_REJECT_REMARKS_CANCEL);
					TbbAccountResponse<PayInfo> resp =  tbbAccountService.payConfirm(confirmRequest);
					if (resp != null || resp.isSucceeded()){
						logger.error("订单取消，资金平台退款成功，userId: "+merchantId+" orderNo: "+orderNo+
								"errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
						boolean cancelResult = merchantOrderDao.merchantCancel(merchantId,orderNo,
								EnumCancelReason.GRAB_MERCHANT.getValue());
						if (!cancelResult){
							logger.error("商家取消订单，更改订单状态出错，userId: "+merchantId+" orderNo: "+orderNo);
						}
						return cancelResult;
					}else {
						logger.error("商家取消订单，资金平台退款出错，userId: "+merchantId+" orderNo: "+orderNo+
								"errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
						return false;
					}
				}else {

				}
			}
		}
		return false;
	}

	/**
	 * 商家删除订单
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public int deleteOrder(String merchantId,String orderNo){
		int count =  merchantOrderDao.deleteOrder(merchantId,orderNo);
		return count;
	}
	/**
	 * 骑手抢单
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public int riderGrabOrder(String merchantId,String riderId,String riderName,String riderPhone,String orderNo, Date grabOrderTime){
		return merchantOrderDao.riderGrabOrder(riderId,riderName,riderPhone,orderNo,grabOrderTime);
	}

	/**
	 * 骑手取货
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public int riderGrabItem(String merchantId,String orderNo, Date grabItemTime){
		return merchantOrderDao.riderGrabItem(orderNo,grabItemTime);
	}

	/**
	 * 骑手完成订单
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public int riderFinishOrder(String merchantId,String orderNo, Date finishOrderTime){
		return merchantOrderDao.riderFinishOrder(orderNo,finishOrderTime);
	}

	/**
	 * 支付超时
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public void payExpired(String merchantId,String orderNo){
		merchantOrderDao.payExpire(orderNo);
	}

	/**
	 * 订单超时
	 */
	@CacheEvict(value= RedisCache.MERCHANT,key="'merchantOrder_'+#merchantId+'_*'")
	@Transactional(readOnly = false)
	public int orderExpire(String merchantId,String orderNo,Date expireTime){
		int count =merchantOrderDao.orderExpire(orderNo,expireTime);
		return count;
	}

	/**
	 * 商家查询订单分页（缓存）
	 */
	@Cacheable(value= RedisCache.MERCHANT,key="'merchantOrder_'+#entity.getUserId()+'_'+#pageNo+'_'+#pageSize+'_'+#entity.getOrderStatus()")
	public Page<MerchantOrderEntity> merchantQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
		return merchantOrderDao.findMerchantOrderPage(pageNo,pageSize,entity);
	}

	/**
	 * 后台查询分页（不缓存）
	 */
	public Page<MerchantOrderEntity> adminQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
		return merchantOrderDao.findMerchantOrderPage(pageNo,pageSize,entity);
	}

	/**
	 * 获取
	 * @param overTimeMilSeconds
	 * @return
     */
	@Transactional(readOnly = true)
	public List<String> getUnCanceledGrabOvertimeOrderNoList(Integer overTimeMilSeconds){
		return merchantOrderDao.getUnCanceledGrabOvertimeOrderNoList(overTimeMilSeconds);
	}
	@Transactional(readOnly = false)
	public boolean dealGrabOvertimeOrders(String orderNo,Date expireTime,boolean enablePushNotice){
		MerchantOrderEntity entity = merchantOrderDao.findByOrderNoAndStatus(orderNo, EnumMerchantOrderStatus.WAITING_GRAB.getValue());
		if (null == entity ){
			logger.info("超时无人接单。订单不存在或状态不允许超时取消，orderNo: "+orderNo);
			return false;
		}
		logger.info("处理超时无人接单：orderNo:{}",orderNo);
		PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(entity.getPayId(),
				MerchantConstants.PAY_REJECT_REMARKS_OVERTIME);
		TbbAccountResponse<PayInfo> resp =  tbbAccountService.payConfirm(confirmRequest);
		if (resp != null && resp.isSucceeded()){
			logger.info("超时无人接单，资金平台退款成功，userId: "+entity.getUserId()+" orderNo: "+orderNo+
					"errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
			orderExpire(entity.getUserId(),orderNo,expireTime);
			if (enablePushNotice){
				pushService.noticeGrabTimeout(entity.getUserId());
			}
			return true;
		}else {
			if (resp == null){
				logger.error("超时无人接单，资金平台退款出错，userId: "+entity.getUserId()+" orderNo: "+orderNo);
			}else {
				logger.error("超时无人接单，资金平台退款出错，userId: "+entity.getUserId()+" orderNo: "+orderNo+
						"errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
			}
		}
		return false;
	}
}

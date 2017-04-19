/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.utils.IdGen;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.TaskCenterToMerchantServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.dao.MerchantOrderDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.handler.TimeoutTaskProducer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional(readOnly = true)
public class MerchantOrderService extends BaseService {
	@Autowired
	private MerchantOrderDao merchantOrderDao;

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


	public MerchantOrderEntity get(String id) {
		return merchantOrderDao.get(id);
	}

	public MerchantOrderEntity findByOrderNo(String orderNo){
		return merchantOrderDao.findByOrderNo(orderNo);
	}

	/**
	 * 商家提交订单
	 */
	@Transactional(readOnly = false)
	public String order(String userId,MerchantOrderEntity entity) throws MerchantClientException {
		MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
		double distance = deliveryFeeService.sumDeliveryDistance(userId,entity.getReceiverLatitude(),entity.getReceiverLongitude(),null);
		//TODO 按规则生成orderNo.
		String orderNo = IdGen.uuid();
		entity.setOrderNo(orderNo);
		entity.setSenderName(ConvertUtil.handleNullString(infoEntity.getRealName()));
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
	@Transactional(readOnly = false)
	public int merchantPay(String merchantId,String orderNo,long payId){
		int count = merchantOrderDao.merchantPay(merchantId,orderNo,payId);
		if (count != 1){
			return count;
		}
		MerchantOrderEntity entity = merchantOrderDao.findByOrderNo(orderNo);
		MerchantOrderDTO merchantOrderDTO = new MerchantOrderDTO();
		BeanUtils.copyProperties(entity,merchantOrderDTO);
		taskCenterToMerchantServiceInterface.merchantOrder(merchantOrderDTO);
		return count;
	}

	/**
	 * 商家取消订单
	 */
	@Transactional(readOnly = false)
	public boolean meachantCancel(String merchantId,String orderNo){
		MerchantOrderEntity entity = merchantOrderDao.findByOrderNo(orderNo);
		if (null == entity )
			return false;
		if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
			boolean cancelResult = merchantOrderDao.merchantCancel(merchantId,orderNo);
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
							MerchantConstants.PAY_REJECT_REMARKS_OVERTIME);
					TbbAccountResponse<PayInfo> resp =  tbbAccountService.payConfirm(confirmRequest);
					if (resp != null || resp.isSucceeded()){
						logger.error("订单取消，资金平台退款成功，userId: "+merchantId+" orderNo: "+orderNo+
								"errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
						boolean cancelResult = merchantOrderDao.merchantCancel(merchantId,orderNo);
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
	@Transactional(readOnly = false)
	public int deleteOrder(String merchantId,String orderNo){
		int count =  merchantOrderDao.deleteOrder(merchantId,orderNo);
		return count;
	}
	/**
	 * 骑手抢单
	 */
	@Transactional(readOnly = false)
	public int riderGrabOrder(String riderId,String riderName,String riderPhone,String orderNo, Date grabOrderTime){
		return merchantOrderDao.riderGrabOrder(riderId,riderName,riderPhone,orderNo,grabOrderTime);
	}

	/**
	 * 骑手取货
	 */
	@Transactional(readOnly = false)
	public int riderGrabItem(String orderNo, Date grabItemTime){
		return merchantOrderDao.riderGrabItem(orderNo,grabItemTime);
	}

	/**
	 * 骑手完成订单
	 */
	@Transactional(readOnly = false)
	public int riderFinishOrder(String orderNo, Date finishOrderTime){
		return merchantOrderDao.riderFinishOrder(orderNo,finishOrderTime);
	}

	@Transactional(readOnly = false)
	public void payExpired(String orderNo){
		merchantOrderDao.payExpire(orderNo);
	}
	/**
	 * 订单超时
	 */
	@Transactional(readOnly = false)
	public int orderExpire(String orderNo){
		int count =merchantOrderDao.orderExpire(orderNo);
		return count;
	}

	/**
	 * 后台关闭订单
	 */
	@Transactional(readOnly = false)
	public int adminClose(String orderNo){
		return merchantOrderDao.adminClose(orderNo);
	}

	/**
	 * 商家订单分页查询
	 */
	public Page<MerchantOrderEntity> findMerchantOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
		return merchantOrderDao.findMerchantOrderPage(pageNo,pageSize,entity);
	}
}

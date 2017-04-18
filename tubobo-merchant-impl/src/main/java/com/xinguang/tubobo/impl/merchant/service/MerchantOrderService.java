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
	public String order(String userId,MerchantOrderEntity entity){
		MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
		//TODO 按规则生成orderNo.
		String orderNo = IdGen.uuid();
		entity.setOrderNo(orderNo);
		entity.setSenderName(infoEntity.getRealName());
		entity.setSenderPhone(infoEntity.getPhone());
		entity.setSenderLongitude(infoEntity.getLongitude());
		entity.setSenderLatitude(infoEntity.getLatitude());
		entity.setSenderAddressProvince(infoEntity.getAddressProvince());
		entity.setSenderAddressCity(infoEntity.getAddressCity());
		entity.setSenderAddressDistrict(infoEntity.getAddressDistrict());

		entity.setSenderAddressStreet(infoEntity.getAddressStreet());
		entity.setSenderAddressDetail(infoEntity.getAddressDetail());
		entity.setPayAmount(entity.getDeliveryFee()+entity.getTipFee());
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
//		merchantOrderDTO.setReceiverAddress(entity.getReceiverAddressFull());
//		merchantOrderDTO.setSenderAddress(entity.getSenderAddressFull());
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
		String sqlString = "update tubobo_merchant_order set order_status = :p1, grab_order_time = :p2, rider_id = :p3, rider_name = :p4, rider_phone = :p5 " +
				"where order_no = :p6 and order_status = :p7 and del_flag = '0' ";
		int count = merchantOrderDao.updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.WAITING_PICK.getValue(),grabOrderTime,riderId,riderName,riderPhone,orderNo, EnumMerchantOrderStatus.WAITING_GRAB.getValue()));
		merchantOrderDao.getSession().clear();
		return count;
	}

	/**
	 * 骑手取货
	 */
	@Transactional(readOnly = false)
	public int riderGrabItem(String orderNo, Date grabItemTime){
		String sqlString = "update tubobo_merchant_order set order_status = :p1, grab_item_time = :p2 where order_no = :p3 and order_status = :p4 and del_flag = '0' ";
		int count =  merchantOrderDao.updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.DELIVERYING.getValue(),
				grabItemTime,orderNo, EnumMerchantOrderStatus.WAITING_PICK.getValue()));
		merchantOrderDao.getSession().clear();
		return count;
	}

	/**
	 * 骑手完成订单
	 */
	@Transactional(readOnly = false)
	public int riderFinishOrder(String orderNo, Date finishOrderTime){
		String sqlString = "update tubobo_merchant_order set order_status = :p1, finish_order_time = :p2 where order_no = :p3 and order_status = :p4 and del_flag = '0' ";
		int count =  merchantOrderDao.updateBySql(sqlString,
				new Parameter(EnumMerchantOrderStatus.FINISH.getValue(),finishOrderTime,orderNo,
						EnumMerchantOrderStatus.DELIVERYING.getValue()));
		merchantOrderDao.getSession().clear();
		return count;
	}

	/**
	 * 订单超时
	 */
	@Transactional(readOnly = false)
	public int orderExpire(String orderNo){
		String sqlString = "update tubobo_merchant_order set order_status = :p1, update_date = :p2 where order_no = :p3 and order_status = :p4 and del_flag = '0' ";
		int count =  merchantOrderDao.updateBySql(sqlString,
				new Parameter(EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue(),new Date(),orderNo,
						EnumMerchantOrderStatus.WAITING_GRAB.getValue()));
		merchantOrderDao.getSession().clear();
		return count;
	}

	/**
	 * 后台关闭订单
	 */
	@Transactional(readOnly = false)
	public int adminClose(String orderNo){
		String sqlString = "update tubobo_merchant_order set order_status = :p1, close_time = :p2 where order_no = :p3 and del_flag = '0' ";
		int count =  merchantOrderDao.updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.CLOSE.getValue(),new Date(),orderNo));
		merchantOrderDao.getSession().clear();
		return count;
	}

	/**
	 * 商家订单分页查询
	 */
	public Page<MerchantOrderEntity> findMerchantOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
		return merchantOrderDao.findMerchantOrderPage(pageNo,pageSize,entity);
	}
}

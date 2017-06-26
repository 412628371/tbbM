/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.manager;

import com.alibaba.fastjson.JSON;
import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.taskcenter.api.TbbTaskResponse;
import com.xinguang.taskcenter.api.common.enums.TaskTypeEnum;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.AddressDTO;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.mq.RmqAddressInfoProducer;
import com.xinguang.tubobo.impl.merchant.service.BaseService;
import com.xinguang.tubobo.impl.merchant.service.MerchantPushService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumCancelReason;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.handler.TimeoutTaskProducer;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class MerchantOrderManager extends BaseService {
	@Autowired
	private OrderService orderService;

	@Autowired
	private RmqAddressInfoProducer rmqAddressInfoProducer;

	@Autowired
	private TimeoutTaskProducer timeoutTaskProducer;

//	@Autowired
//	TaskCenterToMerchantServiceInterface taskCenterToMerchantServiceInterface;
	@Autowired
	TaskDispatchService taskDispatchService;

	@Autowired
	private TbbAccountService tbbAccountService;

	@Autowired
	MerchantPushService pushService;

	@Autowired
	AdminToMerchantService adminToMerchantService;
	@Resource
	Config config;

	public MerchantOrderEntity findByMerchantIdAndOrderNo(String merchantId, String orderNo){
		return orderService.findByMerchantIdAndOrderNo(merchantId,orderNo);
	}
	/**
	 * 商家提交订单
	 */
	public String order(String userId,MerchantOrderEntity entity) throws MerchantClientException {
		String orderNo = orderService.order(userId,entity);
		logger.info("创建订单, userId:{},orderNo:{}，orderType:{}",userId,orderNo,entity.getOrderType());
		//将订单加入支付超时队列 商家订单和车配订单超时时间不同
		int expiredMillSeconds = config.getPayExpiredMilSeconds();
		if (EnumOrderType.BIGORDER.getValue().equals(entity.getOrderType())){
			expiredMillSeconds = config.getConsignorPayExpiredMilliSeconds();
		}
		timeoutTaskProducer.sendMessage(orderNo,expiredMillSeconds);
		AddressDTO dto = getAddressDTO(entity);
		String msg = JSON.toJSONString(dto);
		rmqAddressInfoProducer.sendMessage(msg);
		return orderNo;
	}


	/**
	 * 商家付款
	 */
	public void merchantPay(TaskCreateDTO taskCreateDTO, String merchantId, String orderNo, long payId) throws MerchantClientException {
		int grabExpiredMilliSeconds = config.getTaskGrabExpiredMilSeconds();

		if (TaskTypeEnum.M_SMALL_ORDER.getValue().equals(taskCreateDTO.getTaskType().getValue())){
			taskCreateDTO.setTaskType(TaskTypeEnum.M_SMALL_ORDER);
		}else {
			taskCreateDTO.setTaskType(TaskTypeEnum.M_BIG_ORDER);
			grabExpiredMilliSeconds = config.getConsignorTaskExpiredMilliSeconds();
		}
		taskCreateDTO.setExpireMilSeconds(grabExpiredMilliSeconds);
		Date payDate = new Date();
		int count = orderService.merchantPay(merchantId,orderNo,payId,payDate);
		if (count != 1){
			logger.error("用户支付，数据更新错误，userID：{}，orderNo:{}",merchantId,orderNo);
			throw new MerchantClientException(EnumRespCode.FAIL);
		}
		TbbTaskResponse<Boolean> taskResponse = taskDispatchService.createTask(taskCreateDTO);
		if (taskResponse.isSucceeded() && taskResponse.getData()){
			if (TaskTypeEnum.M_BIG_ORDER.getValue().equals(taskCreateDTO.getTaskType().getValue())){
				adminToMerchantService.sendDistributeTaskSmsAlert();
			}
		}else {
			logger.error("调用任务中心发单出错，orderNo:{},errorCode:{},errorMsg:{}",orderNo,taskResponse.getErrorCode(),taskResponse.getMessage());
		}

	}

	/**
	 * 商家取消订单
	 */
	public boolean cancelOrder(String merchantId,String orderNo){
		MerchantOrderEntity entity = orderService.findByMerchantIdAndOrderNo(merchantId,orderNo);
		if (null == entity )
			return false;
		if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
			boolean cancelResult = orderService.merchantCancel(merchantId,orderNo,
					EnumCancelReason.PAY_MERCHANT.getValue());
			if (!cancelResult){
				logger.error("商家取消订单，更改订单状态出错，userId: "+merchantId+" orderNo: "+orderNo);
			}
			return cancelResult;
		}

		if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
			TbbTaskResponse<Boolean> taskResp = taskDispatchService.cancelTask(orderNo);
			if (taskResp.isSucceeded()){
				if (taskResp.getData()){
					if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
						PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(entity.getPayId(),
								MerchantConstants.PAY_REJECT_REMARKS_CANCEL);
						TbbAccountResponse<PayInfo> resp =  tbbAccountService.payConfirm(confirmRequest);
						if (resp != null || resp.isSucceeded()){
							logger.error("订单取消，资金平台退款成功，userId: "+merchantId+" orderNo: "+orderNo+
									"errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
							boolean cancelResult = orderService.merchantCancel(merchantId,orderNo,
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
					}
				}else {
					logger.error("商家取消订单，任务平台取消失败。userId:{},orderNo:{}",merchantId,orderNo);
				}
			}else {
				logger.error("商家取消订单，任务平台出错。userId:{},orderNo:{},errorCode:{},errorMsg:{}",
						merchantId,orderNo,taskResp.getErrorCode(),taskResp.getMessage());
			}
		}
		return false;
	}

	/**
	 * 商家删除订单
	 */
	public int deleteOrder(String merchantId,String orderNo){
		int count =  orderService.deleteOrder(merchantId,orderNo);
		return count;
	}
//	/**
//	 * 骑手抢单
//	 */
//	public int riderGrabOrder(String merchantId,String riderId,String riderName,String riderPhone,String orderNo, Date grabOrderTime){
//		return orderService.riderGrabOrder(riderId,riderName,riderPhone,orderNo,grabOrderTime);
//	}

	/**
	 * 骑手取货
	 */
	public int riderGrabItem(String merchantId,String orderNo, Date grabItemTime){
		return orderService.riderGrabItem(merchantId,orderNo,grabItemTime);
	}

	/**
	 * 骑手完成订单
	 */
	public int riderFinishOrder(String merchantId,String orderNo, Date finishOrderTime){
		return orderService.riderFinishOrder(merchantId,orderNo,finishOrderTime);
	}

	/**
	 * 支付超时
	 */
//	public void payExpired(String merchantId,String orderNo){
//		orderService.payExpired(merchantId,orderNo);
//	}

	/**
	 * 订单超时
	 */
	public int orderExpire(String merchantId,String orderNo,Date expireTime){
		int count =orderService.orderExpire(merchantId,orderNo,expireTime);
		return count;
	}

	/**
	 * 商家查询订单分页（缓存）
	 */
	public Page<MerchantOrderEntity> merchantQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
		return orderService.merchantQueryOrderPage(pageNo,pageSize,entity);
	}

	/**
	 * 后台查询分页（不缓存）
	 */
	public Page<MerchantOrderEntity> adminQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
		return orderService.adminQueryOrderPage(pageNo,pageSize,entity);
	}

	/**
	 * 获取
	 * @param overTimeMilSeconds
	 * @return
     */
	public List<String> getUnCanceledGrabOvertimeOrderNoList(Integer overTimeMilSeconds){
		return orderService.getUnCanceledGrabOvertimeOrderNoList(overTimeMilSeconds);
	}
	public boolean dealGrabOvertimeOrders(String orderNo,Date expireTime,boolean enablePushNotice){
		MerchantOrderEntity entity = orderService.findByOrderNoAndStatus(orderNo, EnumMerchantOrderStatus.WAITING_GRAB.getValue());
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
				pushService.noticeGrabTimeout(entity.getUserId(),orderNo,MerchantConstants.getPushParamByOrderType(entity.getOrderType()));
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


	public AddressDTO getAddressDTO(MerchantOrderEntity entity){
		AddressDTO dto = new AddressDTO();
		dto.setMerchantId(entity.getUserId());
		dto.setName(entity.getReceiverName());
		dto.setPhone(entity.getReceiverPhone());
		dto.setProvince(entity.getReceiverAddressProvince());
		dto.setCity(entity.getReceiverAddressCity());
		dto.setDistrict(entity.getReceiverAddressDistrict());
		dto.setStreet(entity.getReceiverAddressStreet());
		dto.setDetailAddress(entity.getReceiverAddressDetail());
		dto.setRoomNo(entity.getReceiverAddressRoomNo());
		dto.setLongitude(entity.getReceiverLongitude());
		dto.setLatitude(entity.getReceiverLatitude());
		return dto;
	}
}

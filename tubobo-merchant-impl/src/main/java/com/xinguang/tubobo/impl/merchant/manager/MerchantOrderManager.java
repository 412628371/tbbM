/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.manager;

import com.alibaba.fastjson.JSON;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.taskcenter.api.TbbTaskResponse;
import com.xinguang.taskcenter.api.common.enums.TaskTypeEnum;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.FineRequest;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.request.SubsidyRequest;
import com.xinguang.tubobo.account.api.response.FineInfo;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.SubsidyInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.AddressDTO;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.handler.TimeoutTaskProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqAddressInfoProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqNoticeProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqTakeoutAnswerProducer;
import com.xinguang.tubobo.impl.merchant.mq.TuboboReportDateMqHelp;
import com.xinguang.tubobo.impl.merchant.service.*;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantTaskOperatorCallbackDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumCancelReason;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.takeout.answer.DispatcherInfoDTO;
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

	@Autowired
	ThirdOrderService thirdOrderService;
	@Autowired
	TaskDispatchService taskDispatchService;

	@Autowired
	private TbbAccountService tbbAccountService;

	@Autowired private MerchantPushService pushService;

	@Autowired private AdminToMerchantService adminToMerchantService;
	@Autowired private TuboboReportDateMqHelp tuboboReportDateMqHelp;
	@Autowired private RmqNoticeProducer rmqNoticeProducer;
	@Autowired private RmqTakeoutAnswerProducer rmqTakeoutAnswerProducer;
	@Resource private Config config;

	@Autowired private MerchantInfoService merchantInfoService;

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
		if(StringUtils.isNotBlank(entity.getOrderType()) && EnumOrderType.SMALLORDER.getValue().equals(entity.getOrderType())){
			AddressDTO dto = getAddressDTO(entity);
			String msg = JSON.toJSONString(dto);
			rmqAddressInfoProducer.sendMessage(msg);
		}
		if (StringUtils.isNotBlank(entity.getPlatformCode())){
			try{
				thirdOrderService.processOrder(entity.getUserId(),entity.getPlatformCode(),entity.getOriginOrderId());
			}catch (Exception e){
				logger.error("发单,更新第三方订单异常,userId:{},platformCode:{},originOrderId:{}",
						entity.getUserId(),entity.getPlatformCode(),entity.getOriginOrderId());
			}
		}
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

			//推送消息到报表mq
			tuboboReportDateMqHelp.merchantOrder(taskCreateDTO);
		}else {
			logger.error("调用任务中心发单出错，orderNo:{},errorCode:{},errorMsg:{}",orderNo,taskResponse.getErrorCode(),taskResponse.getMessage());
		}

	}

	/**
	 * 商家取消订单
	 */
	public boolean cancelOrder(String merchantId,String orderNo,boolean isAdminCancel,String waitPickCancelType){
		MerchantOrderEntity entity = orderService.findByMerchantIdAndOrderNo(merchantId,orderNo);
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(merchantId);

		if (null == entity || EnumMerchantOrderStatus.CANCEL.getValue().equals(entity.getOrderStatus())||
				EnumMerchantOrderStatus.FINISH.getValue().equals(entity.getOrderStatus()))
			return false;

		if (isAdminCancel){
			String cancelReason = EnumCancelReason.ADMIN_CANCEL.getValue();
			boolean result ;
			if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
				result = dealCancel(entity.getUserId(),entity.getOrderNo(),cancelReason,true,waitPickCancelType);
			}else {
				result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());
				if (result){
					//TODO
					rmqNoticeProducer.sendOrderCancelNotice(entity.getUserId(),entity.getOrderNo(),
							entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
					result = dealCancel(entity.getUserId(),entity.getOrderNo(),cancelReason,true,waitPickCancelType);
				}
			}
			return result;
		}else {
			boolean result = false;
			if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
				return dealCancel(entity.getUserId(),entity.getOrderNo(),EnumCancelReason.PAY_MERCHANT.getValue(),false,waitPickCancelType);
			}else if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())||EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())){
				TbbTaskResponse<Double> taskResp = taskDispatchService.cancelTask(orderNo);
				if (taskResp.isSucceeded()){
					result = rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());
					if (result){
						result = dealCancel(entity.getUserId(),entity.getOrderNo(),EnumCancelReason.GRAB_MERCHANT.getValue(),false,waitPickCancelType);
					}

					Double punishFee = taskResp.getData();
					if (punishFee!=null&&punishFee>0.0){
							//	 进行扣款
							double punishd=punishFee.doubleValue();
							FineRequest fineRequest = new FineRequest(entity.getOrderNo(),(int)punishd*100,merchant.getAccountId(),"取消订单罚款");
							TbbAccountResponse<FineInfo> fineResponse = tbbAccountService.fine(fineRequest);
							if (fineResponse.isSucceeded()){
								logger.info("商家取消任务罚款 成功. taskNo:{}, riderId:{}, accountId:{}, amount:{},",
										entity.getOrderNo(),entity.getRiderId(),merchant.getAccountId(),punishd);
							}else {
								logger.error("商家取消任务罚款 失败. taskNo:{}, riderId:{}, accountId:{}, amount:{},errorCode:{}, errorMsg:{}",
										entity.getOrderNo(),entity.getRiderId(),merchant.getAccountId(),punishd,fineResponse.getErrorCode(),fineResponse.getMessage());
							}
											}
				}else {
					logger.error("商家取消订单，任务平台出错。userId:{},orderNo:{},errorCode:{},errorMsg:{}",
							merchantId,orderNo,taskResp.getErrorCode(),taskResp.getMessage());
				}
			}
			return result;
		}

	}

	/**
	 * 资金账户退款操作
	 * @param payId
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	private boolean rejectPayConfirm(Long payId,String userId,String orderNo) {
		PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(payId,
				MerchantConstants.PAY_REJECT_REMARKS_CANCEL);
		TbbAccountResponse<PayInfo> resp = tbbAccountService.payConfirm(confirmRequest);
		if (resp != null || resp.isSucceeded()) {
			logger.info("订单取消，资金平台退款成功，userId: {}  orderNo: {} ",userId,orderNo);
			return true;
		}else {
			logger.error("订单取消，资金平台退款失败，userId: {}  orderNo: {} ",userId,orderNo);
			return false;
		}
	}

	/**
	 * 将订单状态改为取消，取消原因为后台取消
	 * @param userId
	 * @param orderNo
	 * @param cancelReason
	 * @param isAdminCancel
	 * @return
	 */
	private boolean dealCancel(String userId,String orderNo,String cancelReason,boolean isAdminCancel,String waitPickCancelType){
		boolean cancelResult;
		if (isAdminCancel){
			cancelResult = orderService.adminCancel(userId,orderNo,cancelReason);
		}else {
			cancelResult = orderService.merchantCancel(userId, orderNo,cancelReason,waitPickCancelType);
		}
		if (!cancelResult) {
			logger.error("取消订单，更改订单状态出错，userId:{} ,orderNo:{},cancelReason:{}" ,userId,orderNo,cancelReason);
		}else{
			//推送消息到报表mq
			tuboboReportDateMqHelp.orderCancel(orderNo,"",cancelReason);
		}
		return cancelResult;
	}
	/**
	 * 商家删除订单
	 */
	public int deleteOrder(String merchantId,String orderNo){
		int count =  orderService.deleteOrder(merchantId,orderNo);
		return count;
	}
	/**
	 * 骑手抢单
	 */
	public boolean riderGrabOrder(MerchantGrabCallbackDTO dto,boolean enableNotice){
		if (null == dto || StringUtils.isBlank(dto.getTaskNo())){
			logger.error("抢单回调dto为空");
		}
		logger.info("抢单回调：dto:{}",dto.toString());

		String orderNo = dto.getTaskNo();
		MerchantOrderEntity entity = orderService.findByOrderNoAndStatus(orderNo,
				EnumMerchantOrderStatus.WAITING_GRAB.getValue());
		if (null == entity){
			logger.error("骑手已接单通知，未找到订单或已处理接单。orderNo:{}",orderNo);
			return false;
		}
		logger.info("处理骑手接单：orderNo:{}",orderNo);
		boolean result = orderService.riderGrabOrder(entity.getUserId(),dto.getRiderId(),dto.getRiderName(),dto.getRiderPhone(),
				orderNo,dto.getGrabTime(),dto.getExpectFinishTime(),dto.getRiderCarNo(),dto.getRiderCarType()) > 0;
		if (enableNotice){
			if (result){
				rmqNoticeProducer.sendGrabNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
			}
			rmqTakeoutAnswerProducer.sendAccepted(entity.getPlatformCode(),entity.getUserId(),entity.getOrderNo(),
					entity.getOriginOrderId(),new DispatcherInfoDTO(dto.getRiderName(),dto.getRiderPhone()));
		}
		return result;
	}

	/**
	 * 骑手取货
	 */
	public boolean riderGrabItem(String orderNo, Date grabItemTime,boolean enableNotice){
		MerchantOrderEntity entity = orderService.findByOrderNoAndStatus(orderNo,
				EnumMerchantOrderStatus.WAITING_PICK.getValue());
		if (null == entity){
			logger.info("骑手取货，未找到订单或已取货完成。orderNo:{}",orderNo);
			return false;
		}
		logger.info("处理骑手取货：orderNo:{}",orderNo);
		return orderService.riderGrabItem(entity.getUserId(),orderNo,grabItemTime)>0;
	}

	/**
	 * 骑手完成订单
	 */
	public boolean riderFinishOrder(String orderNo, Date finishOrderTime, Double expiredMinute, Double expiredCompensation,boolean enableNotice){
		MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(entity.getUserId());
		if (null == entity || EnumMerchantOrderStatus.FINISH.getValue().equals(entity.getOrderStatus())){
			logger.info("骑手完成配送，未找到订单或订单已完成。orderNo:{}",orderNo);
			return false;
		}
		logger.info("处理骑手送达完成：orderNo:{}",orderNo);
		boolean result = orderService.riderFinishOrder(entity.getUserId(),orderNo,finishOrderTime, expiredMinute, expiredCompensation)==1;
		if (result){
			if (enableNotice){
				//发送骑手完成送货通知
				rmqNoticeProducer.sendOrderFinishNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
				//推送到报表mq
				tuboboReportDateMqHelp.orderFinish(entity,finishOrderTime);
			}
		}
		if (expiredCompensation!=null&&expiredCompensation>0.0){
			SubsidyRequest subsidyRequest = new SubsidyRequest(expiredCompensation.intValue()*100,merchant.getAccountId(),entity.getOrderNo(),"超时送达商家赔付");
			TbbAccountResponse<SubsidyInfo> subResponse = tbbAccountService.subsidize(subsidyRequest);
			if (subResponse.isSucceeded()){
				logger.info("骑手超时送达任务罚款 成功. taskNo:{}, riderId:{}, accountId:{}, amount:{},",
						entity.getOrderNo(),entity.getRiderId(),merchant.getAccountId(),expiredCompensation);
			}else {
				logger.error("商家取消任务罚款 失败. taskNo:{}, riderId:{}, accountId:{}, amount:{},errorCode:{}, errorMsg:{}",
						entity.getOrderNo(),entity.getRiderId(),merchant.getAccountId(),expiredCompensation,subResponse.getErrorCode(),subResponse.getMessage());
			}
		}
		return result;
	}


	/**
	 * 订单超时
	 */
	public int orderExpire(String merchantId,String orderNo,Date expireTime){
		int count =orderService.orderExpire(merchantId,orderNo,expireTime);
		return count;
	}

	/**
	 * 重新发单
	 */
	public int orderResend(String merchantId, String originOrderNo){
		int count = orderService.orderResend(merchantId, originOrderNo);
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
				rmqNoticeProducer.sendGrabTimeoutNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
				//推送消息到报表mq
				tuboboReportDateMqHelp.orderCancel(orderNo,"system",EnumCancelReason.GRAB_OVERTIME.getValue());
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

	public void dealFromRiderCancelOrders(MerchantTaskOperatorCallbackDTO dtoCancel) {
		String orderNo = dtoCancel.getTaskNo();
		MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
		if (null == entity || !EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())){
			logger.info("骑手取消配送，未找到订单或订单状态异常。orderNo:{} orderStatus:{}",entity.getOrderStatus());
			return;
		}
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(entity.getUserId());
		if (null == merchant){
			logger.info("商家不存在。userId:{} ",entity.getUserId());
			return;
		}
		Long accountId= Long.valueOf(merchant.getAccountId());
		boolean result = orderService.riderCancel(orderNo, EnumCancelReason.RIDER_CANCEL.getValue(), dtoCancel.getOperateTime(), dtoCancel.getSubsidy());
		if (result) {
			//订单返还
			result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());
			// 被取消任务补贴
			if (dtoCancel.getSubsidy() != null && dtoCancel.getSubsidy() > 0){
				double subsidy=dtoCancel.getSubsidy();
				SubsidyRequest subsidyRequest = new SubsidyRequest((int)subsidy*100,accountId,orderNo,"骑手取消赔付");
				TbbAccountResponse<SubsidyInfo> subsidyResponse = tbbAccountService.subsidize(subsidyRequest);
				if (subsidyResponse.isSucceeded()){
					logger.info("骑手任务被取消 骑手赔付 成功. taskNo:{}, riderId:{}, accountId:{}, amount:{},",
							entity.getOrderNo(),entity.getRiderId(),accountId,subsidy);
				}else {
					logger.error("骑手任务被取消 骑手赔付 失败. taskNo:{}, riderId:{}, accountId:{}, amount:{},errorCode:{}, errorMsg:{}",
							entity.getOrderNo(),entity.getRiderId(),accountId,subsidy,subsidyResponse.getErrorCode(),subsidyResponse.getMessage());
				}
			}

			rmqNoticeProducer.sendOrderCancelByRiderNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
		}else{
			logger.error("骑手取消订单，更改订单状态出错 ,orderNo:{}" ,orderNo);
		}

		logger.info("骑手取消配送：orderNo:{}",orderNo);
	}
}

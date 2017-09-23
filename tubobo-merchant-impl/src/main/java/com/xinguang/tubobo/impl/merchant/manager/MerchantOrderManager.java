/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.manager;

import com.alibaba.fastjson.JSON;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.taskcenter.api.TbbTaskResponse;
import com.xinguang.taskcenter.api.common.enums.PostOrderUnsettledStatusEnum;
import com.xinguang.taskcenter.api.common.enums.TaskTypeEnum;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.FineRequest;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.request.SubsidyRequest;
import com.xinguang.tubobo.account.api.response.*;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.AddressDTO;
import com.xinguang.tubobo.api.enums.EnumOrderStatus;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.handler.TimeoutTaskProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqAddressInfoProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqNoticeProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqTakeoutAnswerProducer;
import com.xinguang.tubobo.impl.merchant.service.*;
import com.xinguang.tubobo.impl.merchant.serviceInterface.OrderManagerBaseService;
import com.xinguang.tubobo.launcher.inner.api.TbbOrderServiceInterface;
import com.xinguang.tubobo.launcher.inner.api.entity.OrderStatusInfoDTO;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.MerchantToThirdPartyServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantTaskOperatorCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantUnsettledDTO;
import com.xinguang.tubobo.merchant.api.dto.OrderStatusStatsDTO;
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

import static com.xinguang.tubobo.merchant.api.enums.EnumRespCode.CANT_CANCEL_DUE_BALANCE;


@Service
public class MerchantOrderManager extends OrderManagerBaseService {

	@Autowired
	private RmqAddressInfoProducer rmqAddressInfoProducer;

	@Autowired
	private TimeoutTaskProducer timeoutTaskProducer;

	@Autowired
    private ThirdOrderService thirdOrderService;
	@Autowired
    private TaskDispatchService taskDispatchService;

	@Autowired
	private TbbAccountService tbbAccountService;


	@Autowired private AdminToMerchantService adminToMerchantService;
	@Autowired private RmqNoticeProducer rmqNoticeProducer;
	@Autowired private RmqTakeoutAnswerProducer rmqTakeoutAnswerProducer;
	@Resource private Config config;

	@Autowired private MerchantInfoService merchantInfoService;
	@Autowired private TbbOrderServiceInterface launcherInnerTbbOrderService;
	@Autowired private OrderService orderService;

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
		taskCreateDTO.setExpireMilSeconds(grabExpiredMilliSeconds);
		Date payDate = new Date();
		String status = EnumMerchantOrderStatus.WAITING_GRAB.getValue();
		//TODO 分业务拆分处理，支持驿站订单的多种派发方式
		//驿站订单，支付之后的状态变为待取货
		if (TaskTypeEnum.POST_ORDER.getValue().equals(taskCreateDTO.getTaskType().getValue())){
			status = EnumMerchantOrderStatus.WAITING_PICK.getValue();
			taskCreateDTO.setExpireMilSeconds(config.getTaskPostOrderGrabExpiredMilSeconds());
		}
		int count = orderService.merchantPay(merchantId,orderNo,payId,payDate,status);
		if (count != 1){
			logger.error("用户支付，数据更新错误，userID：{}，orderNo:{}",merchantId,orderNo);
			throw new MerchantClientException(EnumRespCode.FAIL);
		}
		TbbTaskResponse<Boolean> taskResponse = taskDispatchService.createTask(taskCreateDTO);
		if (taskResponse.isSucceeded() && taskResponse.getData()){
		}else {
			logger.error("调用任务中心发单出错，orderNo:{},errorCode:{},errorMsg:{}",orderNo,taskResponse.getErrorCode(),taskResponse.getMessage());
		}
	}



	/**
	 * 商家取消订单
	 */
	public boolean cancelOrder(String merchantId,String orderNo,boolean isAdminCancel,String waitPickCancelType) throws MerchantClientException {
		MerchantOrderEntity entity = orderService.findByMerchantIdAndOrderNo(merchantId,orderNo);
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(merchantId);

		if (null == entity || EnumMerchantOrderStatus.CANCEL.getValue().equals(entity.getOrderStatus())||
				EnumMerchantOrderStatus.FINISH.getValue().equals(entity.getOrderStatus()))
			return false;

		if (isAdminCancel){
			String cancelReason = EnumCancelReason.ADMIN_CANCEL.getValue();
			boolean result ;
			if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
				result = dealCancel(entity.getUserId(),entity.getOrderNo(),cancelReason,true,waitPickCancelType,null,null);
			}else {
				result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());
				if (result){
					//TODO
					rmqNoticeProducer.sendOrderCancelNotice(entity.getUserId(),entity.getOrderNo(),
							entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
					result = dealCancel(entity.getUserId(),entity.getOrderNo(),cancelReason,true,waitPickCancelType,null,null);
				}
			}
			return result;
		}else {
			boolean result = false;
			if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
				return dealCancel(entity.getUserId(),entity.getOrderNo(),EnumCancelReason.PAY_MERCHANT.getValue(),false,waitPickCancelType,null,null);
			}else if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())||EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())){
				if (EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())&&null==waitPickCancelType){
					//历史状态拦截 骑手已取货 商家端仍停留在带接单取消页面
					return false;
				}

				if (EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())){
					//waitgrab时因为要判断余额是否可以支付
					judgeBalanceForCancel(merchant);
				}
				TbbTaskResponse<Double> taskResp = taskDispatchService.cancelTask(orderNo);
				if (taskResp.isSucceeded()){
					result = rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());
					if (result){
						Double punishFee = taskResp.getData();
						if (punishFee!=null&&punishFee>0.0){
							//	 进行扣款
							double punishd=punishFee.doubleValue();
							double punishFen=CalCulateUtil.mul(punishd,100);
							FineRequest fineRequest = new FineRequest(entity.getOrderNo(),(int)(punishFen),merchant.getAccountId(),MerchantConstants.MERCHANT_CANCEL_FINE,null);
							TbbAccountResponse<FineInfo> fineResponse = tbbAccountService.fineAny(fineRequest);
							if (fineResponse.isSucceeded()){
								logger.info("商家取消任务罚款 成功. taskNo:{}, riderId:{}, accountId:{}, amount:{},",
										entity.getOrderNo(),entity.getRiderId(),merchant.getAccountId(),punishd);
							}else {
								logger.error("商家取消任务罚款 失败. taskNo:{}, riderId:{}, accountId:{}, amount:{},errorCode:{}, errorMsg:{}",
										entity.getOrderNo(),entity.getRiderId(),merchant.getAccountId(),punishd,fineResponse.getErrorCode(),fineResponse.getMessage());
							}
						}
						result = dealCancel(entity.getUserId(),entity.getOrderNo(),EnumCancelReason.GRAB_MERCHANT.getValue(),false,waitPickCancelType,punishFee,null);


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
	 * 	判断余额是否允许取消订单
	 */
	private void judgeBalanceForCancel(MerchantInfoEntity merchant) throws MerchantClientException {
		TbbAccountResponse<AccountInfo> accountInfo = tbbAccountService.getAccountInfo(merchant.getAccountId());
		long balance = accountInfo.getData().getBalance();
		Double punishDouble = taskDispatchService.getCancelPrice().getData();
		if (null!=punishDouble){
            long punishLong=(long)(punishDouble*100);
            if (balance<punishLong){
                throw new MerchantClientException(CANT_CANCEL_DUE_BALANCE);
            }
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
	private boolean dealCancel(String userId,String orderNo,String cancelReason,boolean isAdminCancel,String waitPickCancelType,Double punishFee,Double subsidyFee){
		boolean cancelResult;
		if (isAdminCancel){
			cancelResult = orderService.adminCancel(userId,orderNo,cancelReason);
		}else {
			cancelResult = orderService.merchantCancel(userId, orderNo,cancelReason,waitPickCancelType,punishFee,subsidyFee);
		}
		if (!cancelResult) {
			logger.error("取消订单，更改订单状态出错，userId:{} ,orderNo:{},cancelReason:{}" ,userId,orderNo,cancelReason);
		}else{
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
		MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
		if (null == entity){
			logger.error("骑手已接单通知，未找到订单或已处理接单。orderNo:{}",orderNo);
			return false;
		}
		logger.info("处理骑手接单：orderNo:{}",orderNo);
		boolean result =false ;
		//驿站订单回调后直接是已取货状态，短信发送给收货人 TODO 代码拆分与整合
		if (EnumOrderType.POSTORDER.getValue().equals(entity.getOrderType())&& EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())){
			result = orderService.riderGrabOrderOfPost(entity.getUserId(),dto.getRiderId(),dto.getRiderName(),dto.getRiderPhone(),
					orderNo,dto.getGrabTime(),dto.getExpectFinishTime(),entity.getGrabOrderTime(),dto.getPickupDistance())>0;

			if (result){
				//推送
				rmqNoticeProducer.sendGrabNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
				//短信通知骑手
				if (entity.getShortMessage()){
					adminToMerchantService.sendRiderMessageToReceiver(entity.getRiderName(), entity.getRiderPhone(), entity.getReceiverPhone());
				}
			}
		}else if ( EnumOrderType.SMALLORDER.getValue().equals(entity.getOrderType())&&EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
			result = orderService.riderGrabOrder(entity.getUserId(),dto.getRiderId(),dto.getRiderName(),dto.getRiderPhone(),
					orderNo,dto.getGrabTime(),dto.getExpectFinishTime(),dto.getRiderCarNo(),dto.getRiderCarType(),dto.getPickupDistance()) > 0;
			if (enableNotice){
				if (result){
					rmqNoticeProducer.sendGrabNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
				}
				rmqTakeoutAnswerProducer.sendAccepted(entity.getPlatformCode(),entity.getUserId(),entity.getOrderNo(),
						entity.getOriginOrderId(),new DispatcherInfoDTO(dto.getRiderName(),dto.getRiderPhone()));
			}
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
		boolean flag=orderService.riderGrabItem(entity.getUserId(),orderNo,grabItemTime)>0;
		if (flag){
			//短信通知骑手
			if (entity.getShortMessage()){
				adminToMerchantService.sendRiderMessageToReceiver(entity.getRiderName(), entity.getRiderPhone(), entity.getReceiverPhone());
			}
			try {
                //  通知食集
                OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
                orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.DELIVERYING.getValue());
                orderStatusInfoDTO.setOrderNo(orderNo);
                orderStatusInfoDTO.setRiderId(entity.getRiderId());
                orderStatusInfoDTO.setRiderName(entity.getRiderName());
                orderStatusInfoDTO.setRiderPhone(entity.getRiderPhone());
                launcherInnerTbbOrderService.statusChange(entity.getUserId(),orderStatusInfoDTO);
            }catch (Exception e){

            }
		}
		return flag;
	}

	/**
	 * 骑手完成订单
	 */
	public boolean riderFinishOrder(String orderNo, Date finishOrderTime, Double expiredMinute,  Double expiredCompensation, boolean enableNotice){
		MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(entity.getUserId());
		if (null == entity || EnumMerchantOrderStatus.FINISH.getValue().equals(entity.getOrderStatus())){
			logger.info("骑手完成配送，未找到订单或订单已完成。orderNo:{}",orderNo);
			return false;
		}
		logger.info("处理骑手送达完成：orderNo:{},expiredCompensation:{}",orderNo,expiredCompensation);
		expiredCompensation=expiredMinute==null?0.0:expiredCompensation;
		boolean result = orderService.riderFinishOrder(entity.getUserId(),orderNo,finishOrderTime, expiredMinute, expiredCompensation/100)==1;
		if (result){
			if (enableNotice && !EnumOrderType.POSTORDER.getValue().equals(entity.getOrderType())){
				//发送骑手完成送货通知
				rmqNoticeProducer.sendOrderFinishNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId(),entity.getExpiredMinute(),entity.getCancelCompensation());
			}
            try {
                //  通知食集
                OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
                orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.FINISH.getValue());
                orderStatusInfoDTO.setOrderNo(orderNo);
                orderStatusInfoDTO.setRiderId(entity.getRiderId());
                orderStatusInfoDTO.setRiderName(entity.getRiderName());
                orderStatusInfoDTO.setRiderPhone(entity.getRiderPhone());
                launcherInnerTbbOrderService.statusChange(entity.getUserId(),orderStatusInfoDTO);
            }catch (Exception e){

            }
		}
		if (expiredCompensation!=null&&expiredCompensation>0.0){
			SubsidyRequest subsidyRequest = new SubsidyRequest(expiredCompensation.intValue(), merchant.getAccountId(),entity.getOrderNo(),MerchantConstants.OVERTIME_DELIVERY,null);
			TbbAccountResponse<SubsidyInfo> subResponse = tbbAccountService.subsidize(subsidyRequest);
			if (subResponse.isSucceeded()){
				logger.info("骑手超时送达任务您获得补贴 成功. taskNo:{}, riderId:{}, accountId:{}, amount:{},",
						entity.getOrderNo(),entity.getRiderId(),merchant.getAccountId(),expiredCompensation);
			}else {
				logger.error("骑手超时送达任务您获得补贴 失败. taskNo:{}, riderId:{}, accountId:{}, amount:{},errorCode:{}, errorMsg:{}",
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

	public Page<MerchantOrderEntity> postHouseQueryOrderPage(int pageNo, int pageSize, String expectFinishTimeSort,
														 String orderTimeSort, MerchantOrderEntity entity){
		return orderService.postHouseQueryOrderPage(pageNo, pageSize, expectFinishTimeSort, orderTimeSort, entity);
	}

	public OrderStatusStatsDTO findMerchantOrderCounts(Long providerId){
		OrderStatusStatsDTO statusStatsDTO = new OrderStatusStatsDTO();
		Long deliveryingCounts = orderService.getOrderWithProviderIdAndStatus(providerId, EnumMerchantOrderStatus.DELIVERYING.getValue(), null);
		Long waitingPickCounts = orderService.getOrderWithProviderIdAndStatus(providerId, EnumMerchantOrderStatus.WAITING_PICK.getValue(), null);
		Long undeliveredCounts = orderService.getOrderWithProviderIdAndStatus(providerId, null, PostOrderUnsettledStatusEnum.ING.getValue());
		if(null != undeliveredCounts){
			statusStatsDTO.setUndeliveredCounts(undeliveredCounts);
		}
		if(null != waitingPickCounts){
			statusStatsDTO.setWaitingPickCounts(waitingPickCounts);
		}
		if(null != deliveryingCounts){
			statusStatsDTO.setDeliveryingCounts(deliveryingCounts);
			if(null != undeliveredCounts){
				statusStatsDTO.setDeliveryingCounts(deliveryingCounts-undeliveredCounts);
			}
		}
		statusStatsDTO.setProgressCounts(statusStatsDTO.getDeliveryingCounts()+statusStatsDTO.getWaitingPickCounts()+statusStatsDTO.getUndeliveredCounts());
		logger.info("订单进行中：{}， 配送中：{}，待接单：{}，未妥投：{}", statusStatsDTO.getProgressCounts(), statusStatsDTO.getDeliveryingCounts(),
				statusStatsDTO.getWaitingPickCounts(), statusStatsDTO.getUndeliveredCounts());
		return statusStatsDTO;
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

		MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);

		boolean flag=false;
		if (null == entity){
			logger.info("超时无人接单。订单不存在或状态不允许超时取消，orderNo: "+orderNo);
			return flag;
		}
		String type=entity.getOrderType();
		String orderStatus = entity.getOrderStatus();
		if (!(EnumOrderType.POSTORDER.getValue().equals(type)&&EnumMerchantOrderStatus.WAITING_PICK.equals(orderStatus))&&!(EnumOrderType.SMALLORDER.getValue().equals(type)&&EnumMerchantOrderStatus.WAITING_GRAB.equals(orderStatus))){
			//取消 驿站单必须为待取货,众包单必须为带接单
			logger.info("超时无人接单。订单不存在或状态不允许超时取消，orderNo: "+orderNo);
			return flag;
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
			}
			if(EnumOrderType.POSTORDER.getValue().equals(type)){
				// 通知食集 TODO?
				OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
				orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue());
				orderStatusInfoDTO.setOrderNo(orderNo);
				launcherInnerTbbOrderService.statusChange(entity.getUserId(),orderStatusInfoDTO);
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
		/*try {

        }catch (Exception e){
            // 通知食集 TODO?
            OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
            orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue());
            orderStatusInfoDTO.setOrderNo(orderNo);
            launcherInnerTbbOrderService.statusChange(entity.getUserId(),orderStatusInfoDTO);
        }*/
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
    /**
     * 骑手取消订单
    * */
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
		boolean result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());

		if (result) {
			//订单返还
			// 被取消任务补贴
			result = orderService.riderCancel(orderNo, EnumCancelReason.RIDER_CANCEL.getValue(), dtoCancel.getOperateTime(), dtoCancel.getSubsidy(),entity.getUserId());
			if (dtoCancel.getSubsidy() != null && dtoCancel.getSubsidy() > 0){
				double subsidy=dtoCancel.getSubsidy();
				double subsidyFen=CalCulateUtil.mul(subsidy,100);
				SubsidyRequest subsidyRequest = new SubsidyRequest((int)(subsidyFen),accountId,orderNo,MerchantConstants.MERCHANT_CANCEL_BY_RIDER_SUBSIDY,null);
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
			logger.error("骑手取消订单，更改订单状态出错,退款失败 ,orderNo:{}" ,orderNo);
		}

		logger.info("骑手取消配送：orderNo:{}",orderNo);
	}


    /**
     * 骑手提交未妥投请求
     * @return
     */
    public boolean riderUnsettledOrder(MerchantUnsettledDTO dto){
    	logger.info("商家处理骑手未妥投,dto:{}",dto.toString());
        MerchantOrderEntity order = orderService.findByOrderNo(dto.getOrderNo());
        if (order != null){
            int result = orderService.riderUnsettledOrder(order.getSenderId(),order.getOrderNo(),dto.getUnsettledReason(),dto.getDeliveryTime());
			logger.info("商家处理骑手未妥投,result:{}",result);
			if (result > 0){
                try {
                    //通知食集
                    OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
                    orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.UNDELIVERED.getValue());
                    orderStatusInfoDTO.setOrderNo(dto.getOrderNo());
                    launcherInnerTbbOrderService.statusChange(order.getUserId(),orderStatusInfoDTO);
                }catch (Exception e){

                }
                return true;
            }
        }
        return false;
    }

    /**
     * 商家处理 未妥投订单
     * @param orderNo
     * @return
     */
	public boolean merchantHandlerUnsettledOrder(String merchantId,String orderNo,String message){
        Date unsettledTime = new Date();
        TbbTaskResponse<Boolean> result = taskDispatchService.merchantHandlerUnsettledTask(orderNo,unsettledTime);
        if (result != null && result.getData()){
            orderService.merchantHandlerUnsettledOrder(merchantId,orderNo,unsettledTime,message);
            try {
				//  通知食集
				OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
				orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.UNDELIVERED.getValue());
				orderStatusInfoDTO.setOrderNo(orderNo);
				launcherInnerTbbOrderService.statusChange(merchantId,orderStatusInfoDTO);
			}catch (Exception e){
			}
            return true;
        }
	    return false;
    }

}

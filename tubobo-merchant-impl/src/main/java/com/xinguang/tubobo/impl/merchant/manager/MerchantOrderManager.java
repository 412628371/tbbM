/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.manager;

import com.alibaba.fastjson.JSON;
import com.hzmux.hzcms.common.utils.AliOss;
import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.hzmux.hzcms.common.utils.MerchantUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.taskcenter.api.TbbTaskResponse;
import com.xinguang.taskcenter.api.common.enums.PostOrderUnsettledStatusEnum;
import com.xinguang.taskcenter.api.common.enums.TaskTypeEnum;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.*;
import com.xinguang.tubobo.account.api.response.*;
import com.xinguang.tubobo.account.api.trade.TbbAccountTradeService;
import com.xinguang.tubobo.account.api.trade.request.TradePayRequest;
import com.xinguang.tubobo.account.api.trade.request.TradeRefundRequest;
import com.xinguang.tubobo.account.api.trade.response.TradePayInfo;
import com.xinguang.tubobo.account.api.trade.response.TradeRefundInfo;
import com.xinguang.tubobo.admin.api.AdminToMerchantService;
import com.xinguang.tubobo.admin.api.dto.AddressDTO;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.*;
import com.xinguang.tubobo.impl.merchant.handler.TimeoutTaskProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqAddressInfoProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqMessagePayRecordProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqNoticeProducer;
import com.xinguang.tubobo.impl.merchant.mq.RmqTakeoutAnswerProducer;
import com.xinguang.tubobo.impl.merchant.service.*;
import com.xinguang.tubobo.impl.merchant.serviceInterface.OrderManagerBaseService;
import com.xinguang.tubobo.launcher.inner.api.TbbOrderServiceInterface;
import com.xinguang.tubobo.launcher.inner.api.entity.OrderStatusInfoDTO;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantTaskOperatorCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantUnsettledDTO;
import com.xinguang.tubobo.merchant.api.dto.OrderStatusStatsDTO;
import com.xinguang.tubobo.merchant.api.enums.*;
import com.xinguang.tubobo.takeout.answer.DispatcherInfoDTO;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sun.awt.EmbeddedFrame;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
	@Autowired private RmqMessagePayRecordProducer rmqMessagePayRecordProducer;
	@Autowired private DataCheckingService dataCheckingService;
	@Autowired private TbbAccountTradeService tbbAccountTradeService;

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
	public void merchantPay(TaskCreateDTO taskCreateDTO, String merchantId, String orderNo, String payId) throws MerchantClientException {
		//postNormalOrder,smallOrder grab时间相同 task端可以使用同一延时队列处理
		//int grabExpiredMilliSeconds = config.getTaskGrabExpiredMilSeconds();
		//taskCreateDTO.setExpireMilSeconds(grabExpiredMilliSeconds);
		Date payDate = new Date();
		String status = EnumMerchantOrderStatus.WAITING_GRAB.getValue();
		//TODO 分业务拆分处理，支持驿站订单的多种派发方式
		//postOrder驿站订单，支付之后的状态变为待取货   postNormalOrder,smallOrder-待接单
		if (TaskTypeEnum.POST_ORDER.getValue().equals(taskCreateDTO.getTaskType().getValue())){
			status = EnumMerchantOrderStatus.WAITING_PICK.getValue();
			//postOrder驿站订单 待取货超时时间设置 task端新开队列
			//taskCreateDTO.setExpireMilSeconds(config.getTaskPostOrderGrabExpiredMilSeconds());
		}
		int count = orderService.merchantPay(merchantId,orderNo,payId,payDate,status);
		if (count != 1){
			logger.error("用户支付，数据更新错误，userID：{}，orderNo:{}",merchantId,orderNo);
			throw new MerchantClientException(EnumRespCode.FAIL);
		}
		createTask(taskCreateDTO);
	}

	private void createTask(TaskCreateDTO taskCreateDTO)  {
		String orderNo = taskCreateDTO.getOrderNo();
		try{
			TbbTaskResponse<Boolean> taskResponse = taskDispatchService.createTask(taskCreateDTO);
			if (taskResponse.isSucceeded() && taskResponse.getData()){
				return;
			}else {
				logger.error("调用任务中心发单出错，orderNo:{},errorCode:{},errorMsg:{}",orderNo,taskResponse.getErrorCode(),taskResponse.getMessage());
			}
		}catch (Exception e){
			logger.error("调用任务中心发单出错，orderNo:{},exception:{}",orderNo,e);
		}
		//TODO
		dataCheckingService.reportTaskCreateRpcException(orderNo);
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
				result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo(),merchant.getAccountId()
				);
				if (result){
					//TODO
					result = dealCancel(entity.getUserId(),entity.getOrderNo(),cancelReason,true,waitPickCancelType,null,null);
					rmqNoticeProducer.sendOrderCancelNotice(entity.getUserId(),entity.getOrderNo(),
							entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
				}
			}
			return result;
		}else {
			boolean result = false;
			if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
				return dealCancel(entity.getUserId(),entity.getOrderNo(),EnumCancelReason.PAY_MERCHANT.getValue(),false,waitPickCancelType,null,null);
			}else if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())||EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())){
				if (EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())&&StringUtils.isBlank(waitPickCancelType)){
					//历史状态拦截 骑手已取货 商家端仍停留在带接单取消页面
					return false;
				}

				if (EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())&&
						entity.getOrderType().equals(EnumOrderType.SMALLORDER.getValue())){//目前只有普通商家需要罚款，所以需要判断余额是否充足
					//waitgrab时因为要判断余额是否可以支付
					judgeBalanceForCancel(merchant);
				}
				//TODO 补偿处理没有做
				TbbTaskResponse<Double> taskResp = taskDispatchService.cancelTask(orderNo);
				if (taskResp.isSucceeded()){
					result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo(),merchant.getAccountId());
							//result = rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());
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
	private boolean rejectPayConfirm(String payId,String userId,String orderNo,Long accountId) {
		TradeRefundRequest.RefundDetail refundDetail = new TradeRefundRequest.RefundDetail(payId, orderNo);
		TradeRefundRequest request = TradeRefundRequest.builder("refundOrder"+orderNo, accountId,userId, orderNo).addRefundDetail(refundDetail).build();
		TbbAccountResponse<TradeRefundInfo> resp = tbbAccountTradeService.refund(request);
		/*PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(payId,
				MerchantConstants.PAY_REJECT_REMARKS_CANCEL);
		TbbAccountResponse<PayInfo> resp = tbbAccountService.payConfirm(confirmRequest);*/
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
		//驿站订单抢单取货入口  TODO 代码拆分与整合
		if (EnumOrderType.POSTORDER.getValue().equals(entity.getOrderType())&&EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(entity.getOrderStatus())){
			result = orderService.riderGrabOrderOfPost(entity.getUserId(),dto.getRiderId(),dto.getRiderName(),dto.getRiderPhone(),
					orderNo,dto.getGrabTime(),dto.getExpectFinishTime(),entity.getGrabOrderTime(),dto.getPickupDistance())>0;

			if (result){
				//推送
				rmqNoticeProducer.sendGrabNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
				//短信通知骑手
				sendTxtMessage(entity,dto.getRiderName(),dto.getRiderPhone());
				//通知天下食集
				notifyKASHIJI(orderNo, EnumMerchantOrderStatus.DELIVERYING.getValue(), dto.getRiderId(), dto.getRiderName(), dto.getRiderPhone(), entity.getUserId(), "骑手取货通知失败.orderNo:{}");
			}
		}else if ( (EnumOrderType.SMALLORDER.getValue().equals(entity.getOrderType())||(EnumOrderType.POST_NORMAL_ORDER.getValue().equals(entity.getOrderType())))&&EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
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
		logger.info("处理骑手取货：orderNo:{}",orderNo);
		MerchantOrderEntity entity = orderService.findByOrderNoAndStatus(orderNo,
				EnumMerchantOrderStatus.WAITING_PICK.getValue());
		if (null == entity){
			logger.info("骑手取货，未找到订单或已取货完成。orderNo:{}",orderNo);
			return false;
		}
		boolean flag=orderService.riderGrabItem(entity.getUserId(),orderNo,grabItemTime)>0;
		if (flag){
			//短信通知骑手
			sendTxtMessage(entity,entity.getRiderName(),entity.getRiderPhone());
		}
		return flag;
	}

	private void notifyKASHIJI(String orderNo, String value, String riderId, String riderName, String riderPhone, String userId, String s) {
		try {
			//  通知食集
			OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
			orderStatusInfoDTO.setOrderStatus(value);
			orderStatusInfoDTO.setOrderNo(orderNo);
			orderStatusInfoDTO.setRiderId(riderId);
			orderStatusInfoDTO.setRiderName(riderName);
			orderStatusInfoDTO.setRiderPhone(riderPhone);
			launcherInnerTbbOrderService.statusChange(userId, orderStatusInfoDTO);
		} catch (Exception e) {
			logger.info(s, orderNo);
		}
	}

	/**
	 * 骑手完成订单
	 */
	public boolean riderFinishOrder(String orderNo, Date finishOrderTime, Double expiredMinute,  Double expiredCompensation, boolean enableNotice){
		logger.info("处理骑手送达完成：orderNo:{},expiredCompensation:{}",orderNo,expiredCompensation);
		MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(entity.getUserId());
		if (null == entity || EnumMerchantOrderStatus.FINISH.getValue().equals(entity.getOrderStatus())){
			logger.info("骑手完成配送，未找到订单或订单已完成。orderNo:{}",orderNo);
			return false;
		}
		expiredCompensation=expiredMinute==null?0.0:expiredCompensation;

		boolean result = orderService.riderFinishOrder(entity.getUserId(),orderNo,finishOrderTime, expiredMinute,CalCulateUtil.div(expiredCompensation,100,2))==1;
		if (result){
			if (enableNotice){
				//发送骑手完成送货通知
				rmqNoticeProducer.sendOrderFinishNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId(),entity.getExpiredMinute(),entity.getCancelCompensation());
			}
			if (EnumOrderType.POSTORDER.getValue().equals(entity.getOrderType())){
				notifyKASHIJI(orderNo, EnumMerchantOrderStatus.FINISH.getValue(), entity.getRiderId(), entity.getRiderName(), entity.getRiderPhone(), entity.getUserId(), "骑手完成通知失败.orderNo:{}");
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
	public int orderExpire(String merchantId,String orderNo,Date expireTime,String orderStatus){
		int count =orderService.orderExpire(merchantId,orderNo,expireTime, orderStatus);
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
	public Page<OrderEntity> merchantQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
		return orderService.merchantQueryOrderPage(pageNo,pageSize,entity);
	}

	public Page<OrderEntity> postHouseQueryOrderPage(int pageNo, int pageSize, String expectFinishTimeSort,
													 String orderTimeSort, MerchantOrderEntity entity){
		return orderService.postHouseQueryOrderPage(pageNo, pageSize, expectFinishTimeSort, orderTimeSort, entity);
	}

	public OrderStatusStatsDTO findMerchantOrderCounts(Long providerId){
		OrderStatusStatsDTO statusStatsDTO = new OrderStatusStatsDTO();
		Long deliveryingCounts = orderService.getOrderWithProviderIdAndStatus(providerId, EnumMerchantOrderStatus.DELIVERYING.getValue(), null);
		Long waitingPickCounts = orderService.getOrderWithProviderIdAndStatus(providerId, EnumMerchantOrderStatus.WAITING_PICK.getValue(), null);
		Long waitingGrabCounts = orderService.getOrderWithProviderIdAndStatus(providerId, EnumMerchantOrderStatus.WAITING_GRAB.getValue(), null);
		Long undeliveredCounts = orderService.getOrderWithProviderIdAndStatus(providerId, EnumMerchantOrderStatus.DELIVERYING.getValue(), PostOrderUnsettledStatusEnum.ING.getValue());
		if(null != undeliveredCounts){
			statusStatsDTO.setUndeliveredCounts(undeliveredCounts);
		}
		if(null != waitingPickCounts){
			statusStatsDTO.setWaitingPickCounts(waitingPickCounts);
		}
		if(null != waitingGrabCounts){
			statusStatsDTO.setWaitingGrabCounts(waitingGrabCounts);
		}
		if(null != deliveryingCounts){
			statusStatsDTO.setDeliveryingCounts(deliveryingCounts);
			if(null != undeliveredCounts){
				statusStatsDTO.setDeliveryingCounts(deliveryingCounts-undeliveredCounts);
			}
		}
		statusStatsDTO.setProgressCounts(statusStatsDTO.getDeliveryingCounts()+statusStatsDTO.getWaitingPickCounts()+statusStatsDTO.getUndeliveredCounts()+statusStatsDTO.getWaitingGrabCounts());
		logger.info("订单进行中：{}， 配送中：{}，待接单：{}，未妥投：{}, 待接单：{}", statusStatsDTO.getProgressCounts(), statusStatsDTO.getDeliveryingCounts(),
				statusStatsDTO.getWaitingPickCounts(), statusStatsDTO.getUndeliveredCounts(), statusStatsDTO.getWaitingGrabCounts());
		return statusStatsDTO;
	}

	/**
	 * 后台查询分页（不缓存）
	 */
	public Page<OrderEntity> adminQueryOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
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
		if(EnumOrderType.POSTORDER.getValue().equals(type)&&EnumMerchantOrderStatus.WAITING_PICK.getValue().equals(orderStatus)){
			flag=true;
		}
		if(EnumOrderType.POST_NORMAL_ORDER.getValue().equals(type)&&EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(orderStatus)){
			flag=true;
		}
		if(EnumOrderType.SMALLORDER.getValue().equals(type)&&EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(orderStatus)){
			flag=true;
		}
		if (!flag){
			logger.info("超时无人接单。订单不存在或状态不允许超时取消，orderNo:{},orderType:{},orderStatus:{} ",orderNo,type,orderStatus);
			return false;
		}
		/*if (!(EnumOrderType.POSTORDER.getValue().equals(type)&&EnumMerchantOrderStatus.WAITING_PICK.equals(orderStatus))&&!(EnumOrderType.SMALLORDER.getValue().equals(type)&&EnumMerchantOrderStatus.WAITING_GRAB.equals(orderStatus))){
			//取消 驿站单必须为待取货,众包单必须为带接单
			logger.info("超时无人接单。订单不存在或状态不允许超时取消，orderNo:{},orderType,orderStatus:{} "+orderNo,type,orderStatus);
			return flag;
		}*/

		logger.info("处理超时无人接单：orderNo:{}",orderNo);


		MerchantInfoEntity merchantInfo = merchantInfoService.findByUserId(entity.getUserId());
		boolean	result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo(),merchantInfo.getAccountId());
		/*TradeRefundRequest.RefundDetail refundDetail = new TradeRefundRequest.RefundDetail(entity.getPayId(), orderNo);
		TradeRefundRequest request = TradeRefundRequest.builder(UUID.randomUUID().toString(), merchantInfo.getAccountId(), orderNo).addRefundDetail(refundDetail).build();
		TbbAccountResponse<TradeRefundInfo> resp = tbbAccountTradeService.refund(request);*/
		/*PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(entity.getPayId(),
				MerchantConstants.PAY_REJECT_REMARKS_OVERTIME);
		TbbAccountResponse<PayInfo> resp =  tbbAccountService.payConfirm(confirmRequest);*/
		if (result){
			logger.info("超时无人接单，资金平台退款成功，userId: "+entity.getUserId()+" orderNo: "+orderNo
					);
			orderExpire(entity.getUserId(),orderNo,expireTime,orderStatus);
			if (enablePushNotice){
				rmqNoticeProducer.sendGrabTimeoutNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
			}
			if(EnumOrderType.POSTORDER.getValue().equals(type)){
				try {
					// 通知食集
					OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
					orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue());
					orderStatusInfoDTO.setOrderNo(orderNo);
					launcherInnerTbbOrderService.statusChange(entity.getUserId(),orderStatusInfoDTO);
				}catch (Exception e){
					logger.error("超时无人接单推送食集失败 orderNo: "+orderNo);
				}
			}
			return true;
		}else {
				logger.error("超时无人接单，资金平台退款出错，userId: "+entity.getUserId()+" orderNo: "+orderNo);
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
    /**
     * 骑手取消订单
    * */
	public void dealFromRiderCancelOrders(MerchantTaskOperatorCallbackDTO dtoCancel) {
		String orderNo = dtoCancel.getTaskNo();

		MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
		Boolean messageOpen=entity.getShortMessage();
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

		boolean result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo(),merchant.getAccountId());
		//boolean result =rejectPayConfirm(entity.getPayId(),entity.getUserId(),entity.getOrderNo());
		if (result) {
			//订单返还
			// 被取消任务补贴
			result = orderService.riderCancel(orderNo, EnumCancelReason.RIDER_CANCEL.getValue(), dtoCancel.getOperateTime(), dtoCancel.getSubsidy(),entity.getUserId());
			if (entity.getShortMessage()){
				//取消短信标记位
				orderService.updateShortMessage(false,entity.getOrderNo(),entity.getUserId());
			}
			Double subsidy=dtoCancel.getSubsidy();
			if (dtoCancel.getSubsidy() != null && dtoCancel.getSubsidy() > 0){
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
			//有必要?驿站单骑手无法取消
			if(EnumOrderType.POSTORDER.getValue().equals(entity.getOrderType())){
				try {
					// 通知食集
					OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
					orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.CANCEL.getValue());
					orderStatusInfoDTO.setOrderNo(orderNo);
					launcherInnerTbbOrderService.statusChange(entity.getUserId(),orderStatusInfoDTO);
				}catch (Exception e){
					logger.error("骑手取消订单推送食集失败 orderNo: "+orderNo);
				}
			}
			//重新发单
			String newOrderNo = riderCancelResend(entity, messageOpen, dtoCancel.getSubsidy());

			if (null!=newOrderNo){
				rmqNoticeProducer.sendOrderCancelByRiderNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId(),subsidy,newOrderNo);
			}
		}else{
			logger.error("骑手取消订单，更改订单状态出错,退款失败 ,orderNo:{}" ,orderNo);
		}

		logger.info("骑手取消配送：orderNo:{}",orderNo);
	}
	/**
	 * 骑手取消订单后 重新发单
	 * order表新增字段需知会改方法()
	 * @return
	 */
	private String riderCancelResend(MerchantOrderEntity entity,Boolean messageOpen,Double subsidyFromRider) {
		Double amountD=entity.getPayAmount();
		//新建订单的金额为原来订单金额+骑手罚款
		if (null!=subsidyFromRider){
			amountD=CalCulateUtil.add(amountD,subsidyFromRider);
		}
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(entity.getUserId());
		OrderEntity orderEntity = new OrderEntity();
		OrderDetailEntity detailEntity = new OrderDetailEntity();
		//复制原有信息
		packageOrderInfoForResend(entity, messageOpen, subsidyFromRider, amountD, orderEntity, detailEntity);
		//保存订单
		String newOrderNo= null;
		try {
			newOrderNo = orderService.saveOrderOnly(entity.getUserId(),orderEntity,detailEntity);
		} catch (MerchantClientException e) {
			logger.error("骑手取消后自动创建订单失败,orderNo:{}",entity.getOrderNo(),e);
			return null;
		}

		//进行支付
		MerchantOrderEntity newOrderEntity = orderService.findByOrderNo(newOrderNo);

		//check订单短信开关,if开启--扣除短信费用,短信费用扣除发生在骑手取货时 生成额外短信流水
		if (messageOpen){
			amountD=  CalCulateUtil.sub(amountD,MerchantConstants.MESSAGE_FEE);
		}

		long amount = ConvertUtil.convertYuanToFen(amountD);
		long commission = ConvertUtil.convertYuanToFen(newOrderEntity.getPlatformFee());


		/*PayWithOutPwdRequest payWithOutPwdRequest = new PayWithOutPwdRequest();
		payWithOutPwdRequest.setOrderId(newOrderNo);
		payWithOutPwdRequest.setAccountId(merchant.getAccountId());
		payWithOutPwdRequest.setAmount(amount);
		payWithOutPwdRequest.setCommission(commission);*/

		logger.info("骑手取消订单,免密重新支付请求：userId:{}, orderNo:{} ,amount:{}分 ",entity.getUserId(),newOrderNo,amount);
		//TbbAccountResponse<PayInfo> response = tbbAccountService.payWithOutPwd(payWithOutPwdRequest);

		TradePayRequest tradePayRequest = MerchantUtils.getPayRequestBuilder(merchant.getAccountId(), newOrderNo, amount).notCheckPwd().buildBalancePay();
		TbbAccountResponse<TradePayInfo> response = tbbAccountTradeService.pay(tradePayRequest);

		if (response != null && response.isSucceeded()){
			String payId = response.getData().getTradePayId();
			TaskCreateDTO orderDTO = buildMerchantOrderDTO(newOrderEntity,merchant);
			orderDTO.setPayId(payId);
			logger.info("pay  SUCCESS. orderNo:{}, accountId:{}, payId:{}, amount:{}",newOrderNo
					,merchant.getAccountId(),response.getData().getTradePayId(),amount);
			try {
				merchantPay(orderDTO,merchant.getUserId(),newOrderNo,payId);
			} catch (MerchantClientException e) {
				logger.info("骑手取消订单后重新发单失败:该订单已支付 orderNo:{}",newOrderNo);
			}
		}else {
			if (response == null){
				logger.error("pay  FAIL.orderNo:{}, accountId:{}}",
						newOrderNo,merchant.getAccountId());
			}else {
				if (response.getErrorCode().equals(TbbAccountResponse.ErrorCode.ERROR_AMOUNT_NOT_ENOUGH.getCode())){
					logger.error("pay  FAIL.,余额不足。orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
							newOrderNo,merchant.getAccountId(),response.getErrorCode(),response.getMessage());
				}
				logger.error("pay  FAIL.,orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
						newOrderNo,merchant.getAccountId(),response.getErrorCode(),response.getMessage());
			}
		}
		return newOrderNo;
	}
	/**
	 * 封装重发单订单信息
	 * */
	private void packageOrderInfoForResend(MerchantOrderEntity entity, Boolean messageOpen, Double subsidyFromRider, Double amountD, OrderEntity orderEntity, OrderDetailEntity detailEntity) {
		orderEntity.setWeatherOverFee(entity.getWeatherOverFee());
		orderEntity.setShortMessage(messageOpen);
		orderEntity.setOrderType(entity.getOrderType());
		orderEntity.setDeliveryFee(entity.getDeliveryFee());
		orderEntity.setDeliveryDistance(entity.getDeliveryDistance());
		orderEntity.setOriginOrderId(entity.getOriginOrderId());
		orderEntity.setOriginOrderViewId(entity.getOriginOrderViewId());
		orderEntity.setPayAmount(amountD);
		orderEntity.setPeekOverFee(entity.getPeekOverFee());
		orderEntity.setPlatformCode(entity.getPlatformCode());
		orderEntity.setProviderId(entity.getProviderId());
		//orderEntity.setProviderName(entity.getProviderName());
		orderEntity.setTipFee(entity.getTipFee());
		orderEntity.setReceiverAddressCity(entity.getReceiverAddressCity());
		orderEntity.setReceiverAddressDetail(entity.getReceiverAddressDetail());
		orderEntity.setReceiverAddressDistrict(entity.getReceiverAddressDistrict());
		orderEntity.setReceiverAddressProvince(entity.getReceiverAddressProvince());
		orderEntity.setReceiverAddressRoomNo(entity.getReceiverAddressRoomNo());
		orderEntity.setReceiverAddressStreet(entity.getReceiverAddressStreet());
		orderEntity.setReceiverLatitude(entity.getReceiverLatitude());
		orderEntity.setReceiverLongitude(entity.getReceiverLongitude());
		orderEntity.setReceiverName(entity.getReceiverName());
		orderEntity.setReceiverPhone(entity.getReceiverPhone());
		orderEntity.setSenderName(entity.getSenderName());
		orderEntity.setUserId(entity.getUserId());
		orderEntity.setSenderAdcode(entity.getSenderAdcode());

		detailEntity.setOrderRemark(entity.getOrderRemark());
		detailEntity.setPickupDistance(entity.getPickupDistance());
		detailEntity.setPlatformFee(entity.getPlatformFee());
		detailEntity.setUserId(entity.getUserId());
		//detailEntity.setSenderName(entity.getSenderName());
		detailEntity.setSenderPhone(entity.getSenderPhone());
		detailEntity.setSenderAddressCity(entity.getSenderAddressCity());
		detailEntity.setSenderAddressDetail(entity.getSenderAddressDetail());
		detailEntity.setSenderAddressDistrict(entity.getSenderAddressDistrict());
		detailEntity.setSenderAddressProvince(entity.getSenderAddressProvince());
		detailEntity.setSenderAddressRoomNo(entity.getSenderAddressRoomNo());
		detailEntity.setSenderAddressStreet(entity.getSenderAddressStreet());
		detailEntity.setSenderLatitude(entity.getSenderLatitude());
		detailEntity.setSenderLongitude(entity.getSenderLongitude());
		detailEntity.setUserId(entity.getUserId());
		//设置重发特殊标记位
		orderEntity.setOrderFeature(EnumOrderFeature.RIDER_CANCEL_RESEND.getValue());
		orderEntity.setOrderStatus(EnumMerchantOrderStatus.INIT.getValue());
		orderEntity.setPayStatus(EnumPayStatus.UNPAY.getValue());
		orderEntity.setOrderTime(new Date());
		detailEntity.setCancelSourceDeliveryFee(entity.getDeliveryFee());
		detailEntity.setCancelSourceDeliverySubsidy(CalCulateUtil.add(subsidyFromRider,entity.getCancelSourceDeliverySubsidy()));
		detailEntity.setCancelSourceOrderNo(entity.getOrderNo());
	}


	/**
     * 骑手提交未妥投请求
     * @return
     */
    public boolean riderUnsettledOrder(MerchantUnsettledDTO dto){
    	logger.info("商家处理骑手未妥投,dto:{}",dto.toString());
        MerchantOrderEntity order = orderService.findByOrderNo(dto.getOrderNo());
        if (order != null){
            int result = orderService.riderUnsettledOrder(order.getUserId(),order.getOrderNo(),dto.getUnsettledReason(),dto.getDeliveryTime(),dto.getExpiredMinute());
			logger.info("商家处理骑手未妥投,result:{}",result);
			if (result > 0){
				if(EnumOrderType.POSTORDER.getValue().equals(order.getOrderType())){
					notifyKASHIJI(order.getOrderNo(), EnumMerchantOrderStatus.UNDELIVERED.getValue(), order.getRiderId(), order.getRiderName(), order.getRiderPhone(), order.getUserId(), "未妥投通知商家失败,orderNo:{}");
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
				orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.CONFIRM.getValue());
				orderStatusInfoDTO.setOrderNo(orderNo);
				launcherInnerTbbOrderService.statusChange(merchantId,orderStatusInfoDTO);
			}catch (Exception e){
				logger.info("未妥投确认通知商家失败,orderNo:{}",orderNo);
			}
            return true;
        }
	    return false;
    }


	public void payForMessage(MerchantOrderEntity entity) throws MerchantClientException {
		Double messageFeeD = MerchantConstants.MESSAGE_FEE;
		long messageFee = ConvertUtil.convertYuanToFen(messageFeeD);
		MerchantInfoEntity info = merchantInfoService.findByUserId(entity.getUserId());
		Long accountId = info.getAccountId();
		BuyRequest fineRequest = new BuyRequest(entity.getOrderNo(), (int) messageFee,accountId,MerchantConstants.MERCHANT_MESSAGE_REMARK,MerchantConstants.MERCHANT_MESSAGE);
		TbbAccountResponse<BuyInfo> buyResponse = tbbAccountService.buy(fineRequest);
		if (buyResponse.isSucceeded()){
			logger.info("商家扣除短信费用成功. taskNo:{}, riderId:{}, accountId:{}, amount:{},",
					entity.getOrderNo(),entity.getRiderId(),accountId,messageFee);
			MerchantMessageRecordEntity message = new MerchantMessageRecordEntity();
			message.setOrderNo(entity.getOrderNo());
			message.setRecordMessageId(buyResponse.getData().getId());
			String msg = JSON.toJSONString(message);
			rmqMessagePayRecordProducer.sendMessage(msg);

		}else {
			//更改短信标志
			orderService.updateShortMessage(false,entity.getOrderNo(),entity.getUserId());
			logger.error("商家取消任务罚款 失败. taskNo:{}, riderId:{}, accountId:{}, amount:{},errorCode:{}, errorMsg:{}",
					entity.getOrderNo(),entity.getRiderId(),accountId,messageFee,buyResponse.getErrorCode(),buyResponse.getMessage());
		}

	}



	public TaskCreateDTO buildMerchantOrderDTO(MerchantOrderEntity entity, MerchantInfoEntity infoEntity){
		TaskCreateDTO merchantOrderDTO = new TaskCreateDTO();
		BeanUtils.copyProperties(entity,merchantOrderDTO);
		merchantOrderDTO.setOrderRemark(entity.getOrderRemark());
		merchantOrderDTO.setExpireMilSeconds(config.getTaskGrabExpiredMilSeconds());
		if (EnumOrderType.SMALLORDER.getValue().equals(entity.getOrderType())){
			merchantOrderDTO.setTaskType(TaskTypeEnum.M_SMALL_ORDER);
		}else if (EnumOrderType.POSTORDER.getValue().equals(entity.getOrderType())){
			merchantOrderDTO.setTaskType(TaskTypeEnum.POST_ORDER);
			merchantOrderDTO.setExpireMilSeconds(config.getTaskPostOrderGrabExpiredMilSeconds());
			merchantOrderDTO.setProviderId(entity.getProviderId());
			merchantOrderDTO.setProviderName(entity.getProviderName());
		}else if ((EnumOrderType.POST_NORMAL_ORDER.getValue().equals(entity.getOrderType()))){
			merchantOrderDTO.setTaskType(TaskTypeEnum.POST_NORMAL_ORDER);
			//merchantOrderDTO.setExpireMilSeconds(config.getTaskPostOrderGrabExpiredMilSeconds());
			merchantOrderDTO.setProviderId(entity.getProviderId());
			merchantOrderDTO.setProviderName(entity.getProviderName());
		}
/*		if (entity.getPayAmount() != null){
			merchantOrderDTO.setPayAmount(ConvertUtil.convertYuanToFen(entity.getPayAmount()).intValue());
		}*/
		if (entity.getDeliveryFee() != null){
			merchantOrderDTO.setDeliveryFee(ConvertUtil.convertYuanToFen(entity.getDeliveryFee()).intValue());
		}
		if (entity.getTipFee() != null){
			merchantOrderDTO.setTipFee(ConvertUtil.convertYuanToFen(entity.getTipFee()).intValue());
		}
		if (entity.getPeekOverFee() != null){
			merchantOrderDTO.setPeekOverFee(ConvertUtil.convertYuanToFen(entity.getPeekOverFee()).intValue());
		}
		if (entity.getWeatherOverFee() != null){
			merchantOrderDTO.setWeatherOverFee(ConvertUtil.convertYuanToFen(entity.getWeatherOverFee()).intValue());
		}
		//同时修改为骑手获取的金额  这个金额未计算短信费
		if (entity.getRiderFee()!=null){
			merchantOrderDTO.setPayAmount(ConvertUtil.convertYuanToFen(entity.getRiderFee()).intValue());
		}
		//传入平台抽成费用
		if (entity.getPlatformFee()!=null){
			merchantOrderDTO.setPlatformFee(ConvertUtil.convertYuanToFen(entity.getPlatformFee()).intValue());
		}
		//传给任务的支付金额，减去短信费用  modified by xqh on 2017-10-11
/*		if(entity.getShortMessage()){
			if (merchantOrderDTO.getPayAmount()!=null && merchantOrderDTO.getPayAmount()>MerchantConstants.MESSAGE_FEE*100){
				merchantOrderDTO.setPayAmount(merchantOrderDTO.getPayAmount()- CalCulateUtil.mul(MerchantConstants.MESSAGE_FEE,100).intValue());
			}
		}*/
		merchantOrderDTO.setSenderAvatar(ConvertUtil.handleNullString(infoEntity.getAvatarUrl()));
		String [] shopUrls = new String[5];
		shopUrls[0] = AliOss.generateSignedUrlUseDefaultBucketName(ConvertUtil.handleNullString(infoEntity.getShopImageUrl()));
		shopUrls[1] = AliOss.generateSignedUrlUseDefaultBucketName(ConvertUtil.handleNullString(infoEntity.getShopImageUrl2()));
		merchantOrderDTO.setSenderShopUrls(shopUrls);
		merchantOrderDTO.setAreaCode(infoEntity.getAddressAdCode());
		merchantOrderDTO.setSenderId(infoEntity.getUserId());
		return merchantOrderDTO;
	}
	/**
	 *给收货人发送短信
	 */
	public void sendTxtMessage(MerchantOrderEntity entity,String riderName,String riderPhone){
		if (entity.getShortMessage()){
			try {
				//扣除短信费用
				payForMessage( entity);
				adminToMerchantService.sendRiderMessageToReceiver(riderName, riderPhone, entity.getReceiverPhone());
			}catch (Exception e){
				logger.error("短信通知骑手失败",e.getMessage());
			}
		}
	}
}

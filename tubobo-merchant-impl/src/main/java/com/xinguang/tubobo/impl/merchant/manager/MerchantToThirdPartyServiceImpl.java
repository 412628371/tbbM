package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.utils.AliOss;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.taskcenter.api.TbbTaskResponse;
import com.xinguang.taskcenter.api.common.enums.TaskTypeEnum;
import com.xinguang.taskcenter.api.exception.TbbTaskBaseException;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayWithOutPwdRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.OverFeeService;
import com.xinguang.tubobo.api.dto.OverFeeDTO;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.OrderUtil;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.launcher.inner.api.TbbOrderServiceInterface;
import com.xinguang.tubobo.launcher.inner.api.entity.OrderStatusInfoDTO;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.MerchantToThirdPartyServiceInterface;
import com.xinguang.tubobo.merchant.api.TbbMerchantResponse;
import com.xinguang.tubobo.merchant.api.condition.OrderInfo;
import com.xinguang.tubobo.merchant.api.condition.OverFeeInfo;
import com.xinguang.tubobo.merchant.api.condition.PayInfoDto;
import com.xinguang.tubobo.merchant.api.condition.RiderInfo;
import com.xinguang.tubobo.merchant.api.dto.*;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.api.exception.TbbMerchantBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yangxb on 2017/9/11.
 */
public class MerchantToThirdPartyServiceImpl implements MerchantToThirdPartyServiceInterface {
    @Autowired
    Config config;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Autowired
    private OverFeeService overFeeService;
    @Autowired
    private DeliveryFeeService deliveryFeeService;
    @Autowired
    private RoutePlanning routePlanning;
    @Autowired private TbbAccountService tbbAccountService;
    @Autowired
    private TaskDispatchService taskDispatchService;
    @Autowired private AdminToMerchantService adminToMerchantService;
    @Autowired private TbbOrderServiceInterface launcherInnerTbbOrderService;


    private static final Logger logger = LoggerFactory.getLogger(MerchantToThirdPartyServiceImpl.class);

    public  <D> TbbMerchantResponse<D> wrapErrorCodeResponse(Exception e) {
        if (e instanceof TbbTaskBaseException){
            logger.error("异常：code:{},message:{}",((TbbTaskBaseException) e).getErrorCode(),e.getMessage());
        }else {
            logger.error("任务异常：",e);
        }
        return TbbMerchantBaseException.genErrorCodeResponse(e);
    }

    @Override
    public MerchantOrderCreateResultDto createOrder(MerchantOrderCreateDto req) {
        MerchantOrderCreateResultDto merchantOrderCreateResultDto;
        OverFeeInfo overFeeInfo = new OverFeeInfo();
        String orderNo =null;
        Double distance=0.0;
        Double fee =0.0;
        Double peekOverFee=0.0;
        Double weatherOverFee=0.0;
        merchantOrderCreateResultDto = new MerchantOrderCreateResultDto();
        MerchantOrderEntity entity;
        String userId;

        if (req.getUserId()!=null&&req.getConsignor()!=null&&req.getReceiver()!=null){
            if( StringUtils.isNotBlank(req.getConsignor().getAddressDetail())&&Double.isNaN(req.getConsignor().getLatitude())
                    &&Double.isNaN(req.getConsignor().getLongitude())&&StringUtils.isNotBlank(req.getConsignor().getTelephone())
                    &&StringUtils.isNotBlank(req.getReceiver().getAddressDetail())&&Double.isNaN(req.getReceiver().getLatitude())
                    &&Double.isNaN(req.getReceiver().getLongitude())&&StringUtils.isNotBlank(req.getReceiver().getTelephone())){
                MerchantInfoEntity merchantInfoEntity = merchantInfoService.findByUserId(req.getUserId());

                if (merchantInfoEntity!=null){
                    userId = merchantInfoEntity.getUserId();
                    // 计算各种费用
                    // 计算配送费
                    logger.info("计算配送费请求, userId:{}",req.getUserId());
                    //获取实际距离
                    distance = routePlanning.getDistanceWithWalkFirst(req.getReceiver().getLongitude(),req.getReceiver().getLatitude(),
                            merchantInfoEntity.getLongitude(),merchantInfoEntity.getLatitude());
                    try {
                        fee = deliveryFeeService.sumDeliveryFeeByLocation(distance, merchantInfoEntity.getAddressAdCode(),null);
                    } catch (MerchantClientException e) {
                        e.printStackTrace();
                        logger.error("计算费用   不支持的车辆类型");
                        return null;
                    }
                    // 计算溢价
                    //获得本地区域码
                    String nativeAreaCode = merchantInfoEntity.getAddressAdCode();
                    //调用后台传来的溢价信息
                    OverFeeDTO overFee = overFeeService.findOverFee(nativeAreaCode);
                    if (null!=overFee){
                        boolean peekIsOpen = overFee.getPeekIsOpen();
                        Double weatherOverFeeRemote=overFee.getWeatherOverFee();
                        if (peekIsOpen){
                            peekOverFee=overFee.getPeekOverFee();
                        }
                        if (null!=weatherOverFeeRemote){
                            weatherOverFee=weatherOverFeeRemote;
                        }
                    }
                    //封装数据
                    entity = translateRequestToEntity(merchantInfoEntity,req,peekOverFee,weatherOverFee,distance,fee,userId);
                    entity.setOrderType(EnumOrderType.POSTORDER.getValue());
                    entity.setSenderId(userId);
                    //执行创建订单
                    orderPost(merchantInfoEntity,config,entity);
                }
            }
        }
        //封装返回值
        merchantOrderCreateResultDto.setOrderNo(orderNo);
        merchantOrderCreateResultDto.setDeliverDistance(distance);//配送距离
        merchantOrderCreateResultDto.setDeliveryFee(fee);//配送费 　
        overFeeInfo.setPeekOverFee(peekOverFee);//	高峰溢价
        overFeeInfo.setTotalOverFee(add(peekOverFee,weatherOverFee));//	总溢价
        overFeeInfo.setWeatherOverFee(weatherOverFee);//	天气溢价
        merchantOrderCreateResultDto.setOverFeeInfo(overFeeInfo);
        return merchantOrderCreateResultDto;
    }

    @Override
    public MerchantOrderDetailDTO orderDetail(String orderNo,String userId) {
        MerchantOrderDetailDTO merchantOrderDetailDTO;
        MerchantOrderEntity merchantOrderEntity;
        AddressInfoDTO consignor;
        AddressInfoDTO receiver;
        OrderInfo orderInfo;
        RiderInfo riderInfo;
        PayInfoDto payInfoDto;

        merchantOrderDetailDTO = new MerchantOrderDetailDTO();
        consignor = new AddressInfoDTO();
        receiver = new AddressInfoDTO();
        orderInfo = new OrderInfo();
        riderInfo = new RiderInfo();
        payInfoDto = new PayInfoDto();
        if (StringUtils.isBlank(orderNo)||StringUtils.isBlank(userId)){
            return merchantOrderDetailDTO;
        }
        merchantOrderEntity = orderService.findByMerchantIdAndOrderNo(userId,orderNo);
        if (merchantOrderEntity!=null){
            //插入发货方
            consignor.setAddressCity(merchantOrderEntity.getSenderAddressCity());
            consignor.setAddressDetail(merchantOrderEntity.getSenderAddressDetail());
            consignor.setAddressDistrict(merchantOrderEntity.getSenderAddressDistrict());
            consignor.setAddressProvince(merchantOrderEntity.getSenderAddressProvince());
            consignor.setLatitude(merchantOrderEntity.getSenderLatitude());
            consignor.setLongitude(merchantOrderEntity.getSenderLongitude());
            consignor.setName(merchantOrderEntity.getSenderName());
            consignor.setTelephone(merchantOrderEntity.getSenderPhone());
            //插入收货方
            receiver.setAddressCity(merchantOrderEntity.getReceiverAddressCity());
            receiver.setAddressDetail(merchantOrderEntity.getReceiverAddressDetail());
            receiver.setAddressDistrict(merchantOrderEntity.getReceiverAddressDistrict());
            receiver.setAddressProvince(merchantOrderEntity.getReceiverAddressProvince());
            receiver.setLatitude(merchantOrderEntity.getReceiverLatitude());
            receiver.setLongitude(merchantOrderEntity.getReceiverLongitude());
            receiver.setName(merchantOrderEntity.getReceiverName());
            receiver.setTelephone(merchantOrderEntity.getReceiverPhone());

            //拷贝订单详情
            BeanUtils.copyProperties(merchantOrderEntity,orderInfo);

            riderInfo.setName(merchantOrderEntity.getRiderName());
            riderInfo.setTelephone(merchantOrderEntity.getRiderPhone());

            payInfoDto.setDeliveryFee(merchantOrderEntity.getDeliveryFee());
            payInfoDto.setPayAmount(merchantOrderEntity.getPayAmount());
            payInfoDto.setPayStatus(merchantOrderEntity.getPayStatus());
            payInfoDto.setTipFee(merchantOrderEntity.getTipFee());
        }
        merchantOrderDetailDTO.setConsignor(consignor);
        merchantOrderDetailDTO.setReceiver(receiver);
        merchantOrderDetailDTO.setOrderInfo(orderInfo);
        merchantOrderDetailDTO.setRiderInfo(riderInfo);
        merchantOrderDetailDTO.setPayInfoDto(payInfoDto);
        return merchantOrderDetailDTO;
    }

    @Override
    public TbbMerchantResponse<Boolean> orderCancel(String orderNo,String userId) {
        boolean result = false;
        try {
        MerchantOrderEntity entity = merchantOrderManager.findByMerchantIdAndOrderNo(userId,orderNo);
        String waitPickCancelType = entity.getWaitPickCancelType();
        if (null == entity){
            logger.error("订单操作(删除，取消)，订单不存在。orderNo:{}",orderNo);
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if (StringUtils.isBlank(waitPickCancelType)){
            //待接单之前取消订单 根据WaitPickCancelType为null判断
            result = merchantOrderManager.cancelOrder(userId,orderNo,false,null);
            if (!result){
                logger.error("订单操作(删除，取消)，订单不允许取消。orderNo:{}",orderNo);
                throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_CANCEL);
            }
        }
        if (StringUtils.isNotBlank(waitPickCancelType)){
            // 已接单取消订单 根据WaitPickCancelType不为null判断
            result = merchantOrderManager.cancelOrder(userId,orderNo,false,waitPickCancelType);
            if (!result){
                logger.error("订单操作(删除，取消)，订单不允许取消。orderNo:{}",orderNo);
                throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_CANCEL);
            }
        }
        }catch (Exception e){
            return wrapErrorCodeResponse(e);
        }
        return new TbbMerchantResponse<Boolean>(result);
    }

    @Override
    public boolean abortConfirm(MerchantAbortConfirmDTO req) {
        boolean result;
        if (StringUtils.isBlank(req.getOrderNo())||StringUtils.isBlank(req.getUserId())||req.getConfirm()==null){
            return false;
        }
        result =  merchantOrderManager.merchantHandlerUnsettledOrder(req.getUserId(),req.getOrderNo());
        return  result;
    }


    private MerchantOrderEntity translateRequestToEntity(MerchantInfoEntity merchantInfoEntity,MerchantOrderCreateDto req,Double peekOverFee,Double weatherOverFee,Double distance,Double fee,String userId){
        MerchantOrderEntity entity;
        entity = new MerchantOrderEntity();

        entity.setUserId(userId);
        //插入发货方
        entity.setSenderAddressCity(req.getConsignor().getAddressCity());
        entity.setSenderAddressDetail(req.getConsignor().getAddressDetail());
        entity.setSenderAddressDistrict(req.getConsignor().getAddressDistrict());
        entity.setSenderAddressProvince(req.getConsignor().getAddressProvince());
        entity.setSenderLatitude(req.getConsignor().getLatitude());
        entity.setSenderLongitude(req.getConsignor().getLongitude());
        entity.setSenderName(req.getConsignor().getName());
        entity.setSenderPhone(req.getConsignor().getTelephone());
        //插入收货方
        entity.setReceiverAddressCity(req.getReceiver().getAddressCity());
        entity.setReceiverAddressDetail(req.getReceiver().getAddressDetail());
        entity.setReceiverAddressDistrict(req.getReceiver().getAddressDistrict());
        entity.setReceiverAddressProvince(req.getReceiver().getAddressProvince());
        entity.setReceiverLatitude(req.getReceiver().getLatitude());
        entity.setReceiverLongitude(req.getReceiver().getLongitude());
        entity.setReceiverName(req.getReceiver().getName());
        entity.setReceiverPhone(req.getReceiver().getTelephone());

        //插入预定时间
        entity.setAppointTime(req.getUserAppointTime());
        //订单备足
        entity.setOrderRemark(req.getOrderRemarks());

        //插入对应的服务商
        entity.setProviderId(merchantInfoEntity.getProviderId());
        entity.setProviderName(merchantInfoEntity.getProviderName());

        //插入溢价费用
        entity.setWeatherOverFee(weatherOverFee);
        entity.setPeekOverFee(peekOverFee);

        //配送距离和费用
        entity.setDeliveryDistance(distance);
        entity.setDeliveryFee(fee);

        return  entity;
    }
    public  Double add(Number value1, Number value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(value2.doubleValue()));
        return b1.add(b2).doubleValue();
    }

    private TaskCreateDTO buildMerchantOrderDTO(MerchantOrderEntity entity, MerchantInfoEntity infoEntity){
        TaskCreateDTO merchantOrderDTO = new TaskCreateDTO();
        BeanUtils.copyProperties(entity,merchantOrderDTO);
        merchantOrderDTO.setExpireMilSeconds(config.getTaskGrabExpiredMilSeconds());
        if (EnumOrderType.BIGORDER.getValue().equals(entity.getOrderType())){
            merchantOrderDTO.setTaskType(TaskTypeEnum.M_BIG_ORDER);
        }else if (EnumOrderType.SMALLORDER.getValue().equals(entity.getOrderType())){
            merchantOrderDTO.setTaskType(TaskTypeEnum.M_SMALL_ORDER);
        }else if (EnumOrderType.POSTORDER.getValue().equals(entity.getOrderType())){
            merchantOrderDTO.setTaskType(TaskTypeEnum.POST_ORDER);
            merchantOrderDTO.setExpireMilSeconds(config.getTaskPostOrderGrabExpiredMilSeconds());
        }
        if (entity.getPayAmount() != null){
            merchantOrderDTO.setPayAmount(ConvertUtil.convertYuanToFen(entity.getPayAmount()).intValue());
        }
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
        merchantOrderDTO.setSenderAvatar(ConvertUtil.handleNullString(infoEntity.getAvatarUrl()));
        String [] shopUrls = new String[5];
        shopUrls[0] = AliOss.generateSignedUrlUseDefaultBucketName(ConvertUtil.handleNullString(infoEntity.getShopImageUrl()));
        shopUrls[1] = AliOss.generateSignedUrlUseDefaultBucketName(ConvertUtil.handleNullString(infoEntity.getShopImageUrl2()));
        merchantOrderDTO.setSenderShopUrls(shopUrls);
        merchantOrderDTO.setAreaCode(infoEntity.getAddressAdCode());
        return merchantOrderDTO;
    }

    public String orderPost(MerchantInfoEntity merchantInfoEntity,Config config,MerchantOrderEntity entity){
        String orderNo = null;
        try {
            OrderUtil.judgeOrderCondition(merchantInfoEntity.getMerchantStatus(),config.getBeginWorkTime(),config.getEndWorkTime(),false);
            orderNo = orderService.order(entity.getUserId(),entity);
            MerchantOrderEntity orderEntity = orderService.findByOrderNo(orderNo);
            //TODO 商家扣款 并修改数据   扣款封装小方法
            PayWithOutPwdRequest payWithOutPwdRequest = new PayWithOutPwdRequest();
            payWithOutPwdRequest.setOrderId(orderNo);
            payWithOutPwdRequest.setAccountId(merchantInfoEntity.getAccountId());
            long amount = ConvertUtil.convertYuanToFen(orderEntity.getPayAmount());
            payWithOutPwdRequest.setAmount(amount);
            logger.info("免密支付请求：userId:{}, orderNo:{} ,amount:{}分 ",entity.getUserId(),orderNo,payWithOutPwdRequest.getAmount());
            TbbAccountResponse<PayInfo> response;
            response =  tbbAccountService.payWithOutPwd(payWithOutPwdRequest);
            if (response != null && response.isSucceeded()){
                long payId = response.getData().getId();
                orderEntity.setPayId(payId);
                TaskCreateDTO orderDTO = buildMerchantOrderDTO(orderEntity,merchantInfoEntity);
                orderDTO.setPayId(payId);
                logger.info("pay  SUCCESS. orderNo:{}, accountId:{}, payId:{}, amount:{}",orderNo
                                ,merchantInfoEntity.getAccountId(),response.getData().getId(),amount);
                        orderDTO.setTaskType(TaskTypeEnum.POST_ORDER);
                        Date payDate = new Date();
                        System.out.println("orderNo:"+orderNo);
                        int count = orderService.merchantPay(entity.getUserId(),orderNo,payId,payDate, EnumMerchantOrderStatus.WAITING_PICK.getValue());
                        if (count != 1){
                            logger.error("用户支付，数据更新错误，userID：{}，orderNo:{}",entity.getUserId(),orderNo);
                            throw new MerchantClientException(EnumRespCode.FAIL);
                        }
                        TbbTaskResponse<Boolean> taskResponse = taskDispatchService.createTask(orderDTO);
                        if (taskResponse.isSucceeded() && taskResponse.getData()){
                            if (TaskTypeEnum.M_BIG_ORDER.getValue().equals(orderDTO.getTaskType().getValue())){
                                adminToMerchantService.sendDistributeTaskSmsAlert();
                    }
                }else {
                    logger.error("调用任务中心发单出错，orderNo:{},errorCode:{},errorMsg:{}",orderNo,taskResponse.getErrorCode(),taskResponse.getMessage());
                }

//                //TODO 推送数据给天下食集
//                OrderStatusInfoDTO orderStatusInfoDTO = new OrderStatusInfoDTO();
//                orderStatusInfoDTO.setOrderNo(orderNo);
//                orderStatusInfoDTO.setOrderStatus(EnumMerchantOrderStatus.WAITING_PICK.getValue());
//                boolean result = launcherInnerTbbOrderService.statusChange(entity.getUserId(),orderStatusInfoDTO);
//                if (!result){
//                    logger.error("驿站推送订单状态失败 orderNo:{},userId:{}",
//                            orderNo,entity.getUserId());
//                    throw  new MerchantClientException(EnumRespCode.POST_ORDER_STATUS_PUSH_FAILURE);
//                }

            }else {
                if (response == null){
                    logger.error("pay  FAIL.orderNo:{}, accountId:{}}",
                            orderNo,merchantInfoEntity.getAccountId());
                }else {
                    if (response.getErrorCode().equals(TbbAccountResponse.ErrorCode.ERROR_AMOUNT_NOT_ENOUGH.getCode())){
                        logger.error("pay  FAIL.,余额不足。orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
                                orderNo,merchantInfoEntity.getAccountId(),response.getErrorCode(),response.getMessage());
                        throw  new MerchantClientException(EnumRespCode.ACCOUNT_NOT_ENOUGH);
                    }
                }
                throw  new MerchantClientException(EnumRespCode.ACCOUNT_PAY_FAIL);
            }
        } catch (MerchantClientException e) {
            e.printStackTrace();
            //TODO 根据异常类型  回滚数据
        }
        return orderNo;
    }
}


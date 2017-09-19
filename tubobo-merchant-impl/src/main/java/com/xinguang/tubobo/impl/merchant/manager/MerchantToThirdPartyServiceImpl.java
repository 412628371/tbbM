package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.taskcenter.api.exception.TbbTaskBaseException;
import com.xinguang.tubobo.api.OverFeeService;
import com.xinguang.tubobo.api.dto.OverFeeDTO;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.MerchantToThirdPartyServiceInterface;
import com.xinguang.tubobo.merchant.api.TbbMerchantResponse;
import com.xinguang.tubobo.merchant.api.condition.OrderInfo;
import com.xinguang.tubobo.merchant.api.condition.OverFeeInfo;
import com.xinguang.tubobo.merchant.api.condition.PayInfo;
import com.xinguang.tubobo.merchant.api.condition.RiderInfo;
import com.xinguang.tubobo.merchant.api.dto.*;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.api.exception.TbbMerchantBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by yangxb on 2017/9/11.
 */
public class MerchantToThirdPartyServiceImpl implements MerchantToThirdPartyServiceInterface {

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

        if (req.getUserId()!=null&&req.getConsignor()!=null&&req.getReceiver()!=null){
            if( StringUtils.isNotBlank(req.getConsignor().getAddressDetail())&&Double.isNaN(req.getConsignor().getLatitude())
                    &&Double.isNaN(req.getConsignor().getLongitude())&&StringUtils.isNotBlank(req.getConsignor().getTelephone())
                    &&StringUtils.isNotBlank(req.getReceiver().getAddressDetail())&&Double.isNaN(req.getReceiver().getLatitude())
                    &&Double.isNaN(req.getReceiver().getLongitude())&&StringUtils.isNotBlank(req.getReceiver().getTelephone())){
                MerchantInfoEntity merchantInfoEntity = merchantInfoService.findByUserId(req.getUserId());

                if (merchantInfoEntity!=null){
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
                    entity = translateRequestToEntity(merchantInfoEntity,req,peekOverFee,weatherOverFee,distance,fee);
                    //TODO 执行创建订单
                    try {
                        orderNo = merchantOrderManager.order(req.getUserId(),entity);
                    } catch (MerchantClientException e) {
                        e.printStackTrace();
                    }
                    //TODO 商家扣款
                    //merchantOrderManager.merchantPay();
                    //TODO 修改订单状态未 待取货

                }
            }
        }
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
        PayInfo payInfo;

        merchantOrderDetailDTO = new MerchantOrderDetailDTO();
        consignor = new AddressInfoDTO();
        receiver = new AddressInfoDTO();
        orderInfo = new OrderInfo();
        riderInfo = new RiderInfo();
        payInfo = new PayInfo();
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

            payInfo.setDeliveryFee(merchantOrderEntity.getDeliveryFee());
            payInfo.setPayAmount(merchantOrderEntity.getPayAmount());
            payInfo.setPayStatus(merchantOrderEntity.getPayStatus());
            payInfo.setTipFee(merchantOrderEntity.getTipFee());
        }
        merchantOrderDetailDTO.setConsignor(consignor);
        merchantOrderDetailDTO.setReceiver(receiver);
        merchantOrderDetailDTO.setOrderInfo(orderInfo);
        merchantOrderDetailDTO.setRiderInfo(riderInfo);
        merchantOrderDetailDTO.setPayInfo(payInfo);
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


    private MerchantOrderEntity translateRequestToEntity(MerchantInfoEntity merchantInfoEntity,MerchantOrderCreateDto req,Double peekOverFee,Double weatherOverFee,Double distance,Double fee){
        MerchantOrderEntity entity;
        entity = new MerchantOrderEntity();
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

}


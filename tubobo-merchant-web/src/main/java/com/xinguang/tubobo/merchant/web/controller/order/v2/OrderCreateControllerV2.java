package com.xinguang.tubobo.merchant.web.controller.order.v2;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.CarTypeDTO;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.*;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.common.info.AddressInfo;
import com.xinguang.tubobo.merchant.web.common.AddressInfoToOrderBeanHelper;
import com.xinguang.tubobo.merchant.web.request.order.v2.ReqOrderCreateV2;
import com.xinguang.tubobo.merchant.web.response.order.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * 2.0版订单创建.
 */
@Controller
@RequestMapping("/2.0/order/create")
public class OrderCreateControllerV2 extends MerchantBaseController<ReqOrderCreateV2,CreateOrderResponse> {
    @Autowired
    MerchantOrderManager merchantOrderManager;
    @Autowired
    MerchantInfoService merchantInfoService;
    @Autowired
    Config config;
    @Autowired
    AdminToMerchantService adminToMerchantService;
    @Override
    protected CreateOrderResponse doService(String userId, ReqOrderCreateV2 req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }

        String orderType = req.getType();
        MerchantOrderEntity entity = new MerchantOrderEntity();
        if (EnumOrderType.BIGORDER.getValue().equals(orderType)){
            String status = infoEntity.getMerchantStatus();
            if (!EnumAuthentication.SUCCESS.getValue().equals(status)){
                status = infoEntity.getConsignorStatus();
            }
            judgeOrderCondition(status,config.getConsignorBeginWorkTime(),config.getConsignorEndWorkTime(),true);
            AddressInfo senderAddressInfo = req.getConsignor();
            //大件订单，把发货人地址信息设置到实体
            AddressInfoToOrderBeanHelper.putSenderFromAddressInfo(entity,senderAddressInfo);
            CarTypeDTO carTypeDTO =  adminToMerchantService.queryCarTypeInfo(req.getCarType());
            if (null == carTypeDTO){
                throw new MerchantClientException(EnumRespCode.MERCHANT_CAR_TYPE_NOT_SUPPORT);
            }
            entity.setCarType(req.getCarType());
            entity.setCarTypeName(carTypeDTO.getName());

            entity.setAppointTime(req.getAppointTask().getAppointTime());
            if(EnumAppointType.DELIVERY_IMMED.getValue().equals(req.getAppointTask().getDeliveryType())){
                entity.setDeliveryType(EnumAppointType.DELIVERY_IMMED.getValue());
            }else if(EnumAppointType.DELIVERY_APPOINT.getValue().equals(req.getAppointTask().getDeliveryType())){
                entity.setDeliveryType(EnumAppointType.DELIVERY_APPOINT.getValue());
            }else{
                throw new MerchantClientException(EnumRespCode.PARAMS_ERROR);
            }

        }else if (EnumOrderType.SMALLORDER.getValue().equals(orderType)){
            AddressInfoToOrderBeanHelper.putSenderFromMerchantInfoEntity(entity,infoEntity);
            judgeOrderCondition(infoEntity.getMerchantStatus(),config.getBeginWorkTime(),config.getEndWorkTime(),false);
        }else {
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_TYPE_NOT_SUPPORT);
        }
        entity.setOrderType(orderType);
        AddressInfo receiverAddressInfo = req.getReceiver();
        //把收货人地址信息设置到实体
        AddressInfoToOrderBeanHelper.putReceiverAddressInfo(entity,receiverAddressInfo);
        entity.setDeliveryFee(req.getDeliveryFee());
        entity.setTipFee(req.getTipFee());
        entity.setUserId(userId);
        entity.setSenderId(userId);
        entity.setOrderStatus(EnumMerchantOrderStatus.INIT.getValue());
        entity.setOrderTime(new Date());
        entity.setPayStatus(EnumPayStatus.UNPAY.getValue());
        entity.setDispatchRadius(config.getDispatchRadiusKiloMiles());
        entity.setOrderRemark(ConvertUtil.handleNullString(req.getOrderRemarks()));
        entity.setDelFlag(MerchantOrderEntity.DEL_FLAG_NORMAL);
        String orderNo = merchantOrderManager.order(userId,entity);
        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderNo(orderNo);
        return response;
    }


    /**
     * 根据审核状态和上下班时间判断是否有权限发单
     * @param status 审核状态
     * @param beginWorkTime 开始工作时间
     * @param endWorkTime 结束工作时间
     * @throws MerchantClientException
     */
    private void judgeOrderCondition(String status,String beginWorkTime,String endWorkTime,boolean isBigOrder) throws MerchantClientException {
        if (!EnumAuthentication.SUCCESS.getValue().equals(status)){
            throw new MerchantClientException(EnumRespCode.MERCHANT_STATUS_CANT_OPERATE);
        }
        if (isBigOrder){
            if (!DateUtils.isAfterBeginTimeInOneDay(beginWorkTime)||
                    !DateUtils.isBeforeEndTimeInOneDay(endWorkTime)){
                throw new MerchantClientException(EnumRespCode.CONSIGNOR_NOT_WORK);
            }
        }else {
            if (!DateUtils.isAfterBeginTimeInOneDay(beginWorkTime)||
                    !DateUtils.isBeforeEndTimeInOneDay(endWorkTime)){
                throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_WORK);
            }
        }

    }

    @Override
    protected boolean needIdentify() {
        return true;
    }
}

package com.xinguang.tubobo.merchant.web.controller.order.v2;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.OverFeeService;
import com.xinguang.tubobo.api.dto.CarTypeDTO;
import com.xinguang.tubobo.api.dto.OverFeeDTO;
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
import com.xinguang.tubobo.merchant.web.common.info.OverFeeInfo;
import com.xinguang.tubobo.merchant.web.common.info.ThirdInfo;
import com.xinguang.tubobo.merchant.web.request.order.v2.ReqOrderCreateV2;
import com.xinguang.tubobo.merchant.web.response.order.CreateOrderResponse;
import freemarker.template.utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private OverFeeService overFeeService;
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
            if (req.getAppointTask() != null){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String appointTime = req.getAppointTask().getAppointTime();
                String appointType = req.getAppointTask().getAppointType();
                Date appointDate,currentDate, tomorrowDate;
                if(EnumAppointType.DELIVERY_APPOINT.getValue().equals(appointType)){
                    //获取当前可预约时间
                    String currentTime = DateUtils.getHourAfter(new Date(), 1);
                    String tomorrowTime = DateUtils.getDaysAfter(new Date(), 1, "23:59:59");//明日凌晨
                    try{
                        appointDate = dateFormat.parse(appointTime);
                        currentDate = dateFormat.parse(currentTime);
                        tomorrowDate = dateFormat.parse(tomorrowTime);
                    }catch(ParseException e){
                        throw new MerchantClientException(EnumRespCode.PARAMS_ERROR);
                    }
                    if(currentDate.getTime()<=appointDate.getTime()&&
                            tomorrowDate.getTime()>=appointDate.getTime()){
                        entity.setAppointType(EnumAppointType.DELIVERY_APPOINT.getValue());
                        entity.setAppointTime(appointDate);
                    }else{
                        throw new MerchantClientException(EnumRespCode.MERCHANT_APPOINTTIME_ERROR);
                    }
                }else {
                    entity.setAppointType(EnumAppointType.DELIVERY_IMMED.getValue());
                    entity.setAppointTime(new Date());
                }
            }else {
                entity.setAppointType(EnumAppointType.DELIVERY_IMMED.getValue());
                entity.setAppointTime(new Date());
            }

        }else if (EnumOrderType.SMALLORDER.getValue().equals(orderType)){
            AddressInfoToOrderBeanHelper.putSenderFromMerchantInfoEntity(entity,infoEntity);
            judgeOrderCondition(infoEntity.getMerchantStatus(),config.getBeginWorkTime(),config.getEndWorkTime(),false);

        }else {
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_TYPE_NOT_SUPPORT);
        }
        entity.setOrderType(orderType);
        AddressInfo receiverAddressInfo = req.getReceiver();
        //封装溢价信息并校验

        OverFeeInfo overFeeInfo = req.getOverFeeInfo();
        if (overFeeInfo==null&&EnumOrderType.SMALLORDER.getValue().equals(orderType)){
            //处理老版本,此时判断
            checkOverFeeForOldVersion(infoEntity.getAddressAdCode());
        }

        Double weatherOverFee = 0.0;
        Double peekOverFee = 0.0;
        if (overFeeInfo!=null){
            //校验溢价信息是否实时原来一样
            checkOverFee(overFeeInfo,infoEntity.getAddressAdCode());
            weatherOverFee= overFeeInfo.getWeatherOverFee();
            peekOverFee=overFeeInfo.getPeekOverFee();
            if (peekOverFee!=null){
                peekOverFee=overFeeInfo.getPeekOverFee();
            }
            if (weatherOverFee!=null){
                weatherOverFee= overFeeInfo.getWeatherOverFee();
            }
        }
        entity.setWeatherOverFee(weatherOverFee);
        entity.setPeekOverFee(peekOverFee);


        ThirdInfo thirdInfo = req.getThirdInfo();
        if (null != thirdInfo){
            entity.setPlatformCode(thirdInfo.getPlatformCode());
            entity.setOriginOrderId(thirdInfo.getOriginOrderId());
            entity.setOriginOrderViewId(thirdInfo.getOriginOrderViewId());
        }
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
        entity.setWeatherOverFee(weatherOverFee);
        entity.setPeekOverFee(peekOverFee);
        entity.setDeliveryDistance(req.getDeliveryDistance());
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

   /* public void checkOverFee(OverFeeInfo overFeeInfo,String AreaCode) throws MerchantClientException {
        OverFeeDTO overFee = overFeeService.findOverFee(AreaCode);
        if(null!=overFee){
            Double peekOverFeeOld = overFeeInfo.getPeekOverFee();
            Double weatherOverFeeOld = overFeeInfo.getWeatherOverFee();
            Double weatherOverFeeNew = overFee.getWeatherOverFee();
            Double peekOverFeeNew = overFee.getPeekOverFee();
            if (!overFee.getPeekIsOpen()){
                peekOverFeeNew=0.0;
            }
            if (weatherOverFeeNew!=null){
                //该地区实时开启了天气溢价
                if (!peekOverFeeNew.equals(peekOverFeeOld)&&!weatherOverFeeNew.equals(weatherOverFeeOld)){
                    //天气溢价高峰溢价均发生改变抛出如下信息
                    throw new MerchantClientException(EnumRespCode.ALL_OVER_FEE_CHANGE);
                }
                if (!weatherOverFeeNew.equals(weatherOverFeeOld)){
                    //天气溢价发生改变抛出如下信息
                    throw new MerchantClientException(EnumRespCode.WEATHER_OVER_FEE_CHANGE);
                }
            }else{
                //该地区实时关闭了天气溢价
                if (!weatherOverFeeOld.equals(0.0)&&peekOverFeeNew.equals(0.0)){
                    //实际后台已经关闭该区域天气溢价和高峰溢价, 但是订单天气溢价不为零
                    throw new MerchantClientException(EnumRespCode.OVER_FEE_CLOSE);
                }
                if (!weatherOverFeeOld.equals(0)){
                    //原有天气溢价不为零 实时后台天气溢价已经关闭 发生改变抛出如下信息
                    throw new MerchantClientException(EnumRespCode.WEATHER_OVER_FEE_CHANGE);
                }
            }
            if (!peekOverFeeNew.equals(peekOverFeeOld)){
                //高峰溢价均发生改变抛出如下信息
                throw new MerchantClientException(EnumRespCode.PEEK_OVER_FEE_CHANGE);
            }
        }
    }*/


    public void checkOverFee(OverFeeInfo overFeeInfo,String AreaCode) throws MerchantClientException {
        OverFeeDTO overFee = overFeeService.findOverFee(AreaCode);
        if(null!=overFee){
            Double peekOverFeeOld = overFeeInfo.getPeekOverFee();
            Double weatherOverFeeOld = overFeeInfo.getWeatherOverFee();
            Double weatherOverFeeNew = overFee.getWeatherOverFee();
            Double peekOverFeeNew = overFee.getPeekOverFee();
            if (!overFee.getPeekIsOpen()){
                peekOverFeeNew=0.0;
            }
            if (weatherOverFeeNew!=null){
                if(weatherOverFeeNew.equals(weatherOverFeeOld)&&peekOverFeeOld.equals(peekOverFeeNew)){
                    logger.info("price is right");
                }else{
                    //该地区实时开启了天气溢价
                    if (!peekOverFeeNew.equals(peekOverFeeOld)&&!weatherOverFeeNew.equals(weatherOverFeeOld)){
                        //天气溢价高峰溢价均发生改变抛出如下信息
                        throw new MerchantClientException(EnumRespCode.ALL_OVER_FEE_CHANGE);
                    }
                    if (!weatherOverFeeNew.equals(weatherOverFeeOld)){
                        //天气溢价发生改变抛出如下信息
                        throw new MerchantClientException(EnumRespCode.WEATHER_OVER_FEE_CHANGE);
                    }
                }
            }else{
                if(weatherOverFeeOld==0.0&&peekOverFeeOld.equals(peekOverFeeNew)){
                    logger.info("price is right");
                }else{
                    //该地区实时关闭了天气溢价
                    if (!(weatherOverFeeOld==0.0)&&(!peekOverFeeNew.equals(peekOverFeeOld))&&!overFee.getPeekIsOpen()){
                        //实际后台已经关闭该区域天气溢价和高峰溢价, 但是订单天气溢价不为零
                        throw new MerchantClientException(EnumRespCode.OVER_FEE_CLOSE);
                    }
                    if (!(weatherOverFeeOld==0.0)){
                        //原有天气溢价不为零 实时后台天气溢价已经关闭 发生改变抛出如下信息
                        throw new MerchantClientException(EnumRespCode.WEATHER_OVER_FEE_CHANGE);
                    }
                }
            }
            if (!peekOverFeeNew.equals(peekOverFeeOld)){
                //高峰溢价均发生改变抛出如下信息
                throw new MerchantClientException(EnumRespCode.PEEK_OVER_FEE_CHANGE);
            }
        }
    }

/**
 * 检查版本是否过低不支持溢价费
 */
    public void checkOverFeeForOldVersion(String AreaCode) throws MerchantClientException {
        OverFeeDTO overFee = overFeeService.findOverFee(AreaCode);
        if (overFee!=null){
            if (overFee.getPeekIsOpen()||null!=overFee.getWeatherOverFee()){
                throw new MerchantClientException(EnumRespCode.OVERFEE_VERSION_LOW);
            }
        }
    }




    @Override
    protected boolean needIdentify() {
        return true;
    }
}

package com.xinguang.tubobo.merchant.web.controller.order.v2;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.enums.EnumCarType;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.common.OrderBeanToAddressInfoHelper;
import com.xinguang.tubobo.merchant.web.common.info.*;
import com.xinguang.tubobo.merchant.web.manager.OrderDetailRespManager;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderDetail;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderDetail;
import com.xinguang.tubobo.merchant.web.response.order.v2.RespOrderDetailV2;
import com.xinguang.tubobo.rider.api.RiderToAdminServiceInterface;
import com.xinguang.tubobo.rider.api.dto.RiderInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 订单详情controller 2.0.
 */
@Controller
@RequestMapping("/2.0/order/detail")
public class OrderDetailControllerV2 extends MerchantBaseController<ReqOrderDetail,RespOrderDetailV2> {
    @Autowired
    OrderDetailRespManager manager;
    @Autowired
    RiderToAdminServiceInterface riderToAdminServiceInterface;

    @Override
    protected RespOrderDetailV2 doService(String userId, ReqOrderDetail req) throws MerchantClientException {

        String orderNo = req.getOrderNo();
        MerchantOrderEntity entity = manager.getOrderEntity(userId,orderNo);
        RespOrderDetail respOrderDetail = manager.getDetail(userId,orderNo,entity);

        AddressInfo consignor = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(entity,new AddressInfo(),true);
        AddressInfo receiver = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(entity,new AddressInfo(),false);
        PayInfo payInfo = new PayInfo();
        BeanUtils.copyProperties(respOrderDetail,payInfo);

        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(respOrderDetail,orderInfo);
        if (null==entity.getUnsettledStatus()){
            orderInfo.setUnsettledStatus("");
        }
        CommentsInfo commentsInfo = new CommentsInfo(respOrderDetail.getRatedFlag());
        BeanUtils.copyProperties(respOrderDetail,commentsInfo);
        String headImage=null;
        if (StringUtils.isNotEmpty(entity.getRiderId())){
            try {
                RiderInfoDTO byUserId = riderToAdminServiceInterface.findByUserId(entity.getRiderId());
                if (null!=byUserId){
                    headImage = byUserId.getHeadImage();
                }
            }catch (Exception e){
                logger.info("获取骑手riderd端照片失败");
            }
        }
        DriverInfo driverInfo = new DriverInfo(entity.getRiderName(),entity.getRiderPhone()
           ,headImage);

       /* //获取用车时间对象
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr="";
        if(entity.getAppointTime() != null){
            timeStr = sm.format(entity.getAppointTime());
        }
        AppointTask appointTask = new AppointTask(timeStr, entity.getAppointType());*/

        //封装overfee对象
        OverFeeInfo overFeeInfo = new OverFeeInfo();
        Double weatherOverFee= entity.getWeatherOverFee();
        Double peekOverFee=entity.getPeekOverFee();
        if (peekOverFee==null){
            peekOverFee=0.0;
        }
        if (weatherOverFee==null){
            weatherOverFee=0.0;
        }
        overFeeInfo.setPeekOverFee(peekOverFee);
        overFeeInfo.setWeatherOverFee(weatherOverFee);
        overFeeInfo.setTotalOverFee(peekOverFee+weatherOverFee);

        ThirdInfo thirdInfo = new ThirdInfo();
        thirdInfo.setPlatformCode(entity.getPlatformCode());
        thirdInfo.setOriginOrderId(entity.getOriginOrderId());
        thirdInfo.setOriginOrderViewId(entity.getOriginOrderViewId());

        RespOrderDetailV2 resp = new RespOrderDetailV2();
        resp.setCommentsInfo(commentsInfo);
        resp.setConsignor(consignor);
        resp.setReceiver(receiver);
        resp.setDriverInfo(driverInfo);
        resp.setOrderInfo(orderInfo);
        resp.setPayInfo(payInfo);
      //  resp.setAppointTask(appointTask);
        resp.setOverFeeInfo(overFeeInfo);
        resp.setThirdInfo(thirdInfo);
        return resp;
    }

}

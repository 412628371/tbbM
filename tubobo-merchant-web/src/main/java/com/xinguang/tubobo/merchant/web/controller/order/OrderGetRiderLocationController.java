package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.api.TaskCenterToMerchantServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.GeoLocation;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.merchant.web.request.ReqOrderRiderLocation;
import com.xinguang.tubobo.merchant.web.response.RespRiderLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/17.
 */
@Controller
@RequestMapping("/order/riderLocation")
public class OrderGetRiderLocationController extends MerchantBaseController<ReqOrderRiderLocation,RespRiderLocation> {
    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Autowired
    private TaskCenterToMerchantServiceInterface taskCenterToMerchantServiceInterface;
    @Override
    protected RespRiderLocation doService(String userId, ReqOrderRiderLocation req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderManager.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (entity == null){
            logger.error("获取骑手位置，订单不存在。orderNo:{}",req.getOrderNo());
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        GeoLocation location =taskCenterToMerchantServiceInterface.getRiderLocation(entity.getRiderId());
        if (null == location){
            logger.error("获取骑手位置，位置数据不存在。orderNo:{}",req.getOrderNo());
            throw new MerchantClientException(EnumRespCode.MERCHANT_RIDER_LOCATION_NOT_FOUND);
        }
        logger.info("查询骑手位置信息：userId:{},orderNo:{},geoLocation:{}",userId,req.getOrderNo(),location.toString());
        RespRiderLocation respRiderLocation = new RespRiderLocation();
        respRiderLocation.setLatitude(location.getLatitude());
        respRiderLocation.setLongtitude(location.getLongitude());
        return respRiderLocation;
    }
}

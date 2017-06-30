package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.taskcenter.api.TbbTaskResponse;
import com.xinguang.taskcenter.api.request.GeoLocation;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderRiderLocation;
import com.xinguang.tubobo.merchant.web.response.RespRiderLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 获取骑手位置controller.
 */
@Controller
@RequestMapping("/order/riderLocation")
public class OrderGetRiderLocationController extends MerchantBaseController<ReqOrderRiderLocation,RespRiderLocation> {
    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Autowired
    private TaskDispatchService taskDispatchService;
    @Override
    protected RespRiderLocation doService(String userId, ReqOrderRiderLocation req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderManager.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (entity == null){
            logger.error("获取骑手位置，订单不存在。orderNo:{}",req.getOrderNo());
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }

        boolean isRider = true;
        if (EnumOrderType.BIGORDER.getValue().equals(entity.getOrderType())){
            isRider = false;
        }
        TbbTaskResponse<GeoLocation> response =taskDispatchService.getRiderOrDriverLocation(entity.getRiderId(),isRider);
        if (!response.isSucceeded() || response.getData()==null){
            logger.error("获取骑手位置，位置数据不存在。orderNo:{}",req.getOrderNo());
            throw new MerchantClientException(EnumRespCode.MERCHANT_RIDER_LOCATION_NOT_FOUND);
        }
        GeoLocation location = response.getData();
        logger.info("查询骑手位置信息：userId:{},orderNo:{},geoLocation:{}",userId,req.getOrderNo(),location.toString());
        RespRiderLocation respRiderLocation = new RespRiderLocation();
        respRiderLocation.setLatitude(location.getLatitude());
        respRiderLocation.setLongtitude(location.getLongitude());
        return respRiderLocation;
    }
}

package com.xinguang.tubobo.merchant.web.controller.order.v2;

import com.xinguang.tubobo.impl.merchant.common.AddressInfo;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.OrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumBindStatusType;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.common.OrderBeanToAddressInfoHelper;
import com.xinguang.tubobo.merchant.web.common.info.*;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderList;
import com.xinguang.tubobo.merchant.web.response.order.v2.RespOrderDetailV2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表controller v2.0.
 */
@Controller
@RequestMapping("/2.0/order/list")
public class OrderListControllerV2 extends MerchantBaseController<ReqOrderList,PageDTO<RespOrderDetailV2>>{
    @Autowired
    MerchantOrderManager merchantOrderManager;
    @Autowired
    MerchantInfoService merchantInfoService;


    @Override
    protected PageDTO<RespOrderDetailV2> doService(String userId, ReqOrderList req) throws MerchantClientException {
        MerchantOrderEntity queryEntity = new MerchantOrderEntity();
        queryEntity.setUserId(userId);
        queryEntity.setOrderStatus(req.getOrderStatus());
        MerchantInfoEntity merchant = merchantInfoService.findByUserId(userId);
        String orderType= EnumOrderType.SMALLORDER.getValue();
        Long providerId = merchant.getProviderId();
        if (null != providerId){
            orderType=EnumOrderType.POSTORDER.getValue();
        }
        queryEntity.setOrderType(orderType);
        Page<OrderEntity> page = merchantOrderManager.merchantQueryOrderPage(req.getPageNo(),req.getPageSize(),queryEntity);
        List<RespOrderDetailV2> list = new ArrayList<>(req.getPageNo());
        if (page.hasContent()) {
            for (OrderEntity entity :page) {
                MerchantOrderEntity merchantOrderEntity= new MerchantOrderEntity();
                BeanUtils.copyProperties(entity,merchantOrderEntity);
                RespOrderDetailV2 detailVo = new RespOrderDetailV2();
                AddressInfo consignor = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(merchantOrderEntity,new AddressInfo(),true);
                AddressInfo receiver = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(merchantOrderEntity,new AddressInfo(),false);
                PayInfo payInfo = new PayInfo();
                BeanUtils.copyProperties(entity,payInfo);

                OrderInfo orderInfo = new OrderInfo();
                BeanUtils.copyProperties(entity,orderInfo);
                if (null==entity.getUnsettledStatus()){
                    orderInfo.setUnsettledStatus("");
                }
                CommentsInfo commentsInfo = new CommentsInfo(entity.getRatedFlag());
                BeanUtils.copyProperties(entity,commentsInfo);

                DriverInfo driverInfo = new DriverInfo(entity.getRiderName(),entity.getRiderPhone(),null);

                ThirdInfo thirdInfo = new ThirdInfo();
                thirdInfo.setPlatformCode(entity.getPlatformCode());
                thirdInfo.setOriginOrderId(entity.getOriginOrderId());
                thirdInfo.setOriginOrderViewId(entity.getOriginOrderViewId());

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

                detailVo.setOrderInfo(orderInfo);
                detailVo.setPayInfo(payInfo);
                detailVo.setDriverInfo(driverInfo);
                detailVo.setConsignor(consignor);
                detailVo.setCommentsInfo(commentsInfo);
                detailVo.setReceiver(receiver);
                detailVo.setThirdInfo(thirdInfo);
                detailVo.setOverFeeInfo(overFeeInfo);
                list.add(detailVo);
            }
        }
        return new PageDTO<>(req.getPageNo(),req.getPageSize(),page.getTotalElements(),list);
    }
}

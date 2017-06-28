package com.xinguang.tubobo.merchant.web.controller.order.v2;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.common.OrderBeanToAddressInfoHelper;
import com.xinguang.tubobo.merchant.web.common.info.*;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderList;
import com.xinguang.tubobo.merchant.web.response.order.v2.RespOrderDetailV2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
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

    @Override
    protected PageDTO<RespOrderDetailV2> doService(String userId, ReqOrderList req) throws MerchantClientException {
        MerchantOrderEntity queryEntity = new MerchantOrderEntity();
        queryEntity.setUserId(userId);
        queryEntity.setOrderStatus(req.getOrderStatus());
        queryEntity.setOrderType(req.getOrderType());
        Page<MerchantOrderEntity> page = merchantOrderManager.merchantQueryOrderPage(req.getPageNo(),req.getPageSize(),queryEntity);
        List<RespOrderDetailV2> list = new ArrayList<>(page.getPageNo());
        if (page!= null && page.getList() != null && page.getList().size() > 0) {
            for (MerchantOrderEntity entity :page.getList()) {
                RespOrderDetailV2 detailVo = new RespOrderDetailV2();
                AddressInfo consignor = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(entity,new AddressInfo(),true);
                AddressInfo receiver = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(entity,new AddressInfo(),false);
                PayInfo payInfo = new PayInfo();
                BeanUtils.copyProperties(entity,payInfo);

                OrderInfo orderInfo = new OrderInfo();
                BeanUtils.copyProperties(entity,orderInfo);
                CommentsInfo commentsInfo = new CommentsInfo(entity.getRatedFlag());
                BeanUtils.copyProperties(entity,commentsInfo);

                DriverInfo driverInfo = new DriverInfo(entity.getRiderName(),entity.getRiderPhone(),entity.getRiderCarNo(),
                        entity.getRiderCarType());
                CarInfo carInfo = new CarInfo(entity.getCarType(),entity.getCarTypeName());
                //获取用车时间对象
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStr="";
                if(entity.getAppointTime() != null){
                    timeStr = sm.format(entity.getAppointTime());
                }
                AppointTask appointTask = new AppointTask(timeStr, entity.getAppointType());

                detailVo.setCarInfo(carInfo);
                detailVo.setOrderInfo(orderInfo);
                detailVo.setPayInfo(payInfo);
                detailVo.setDriverInfo(driverInfo);
                detailVo.setConsignor(consignor);
                detailVo.setCommentsInfo(commentsInfo);
                detailVo.setReceiver(receiver);
                detailVo.setAppointTask(appointTask);
                list.add(detailVo);
            }
        }
        return new PageDTO<>(page.getPageNo(),page.getPageSize(),page.getCount(),list);
    }
}

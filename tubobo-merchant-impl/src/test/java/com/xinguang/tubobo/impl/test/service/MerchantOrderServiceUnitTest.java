package com.xinguang.tubobo.impl.test.service;

import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.impl.test.base.BaseJunit4Test;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14.
 */
public class MerchantOrderServiceUnitTest extends BaseJunit4Test {
       @Resource
       MerchantOrderService merchantOrderService;
    @Test
    @Transactional
    @Rollback()
    public void testUpdateOrderStatus(){
        String userId = "1234";
        MerchantOrderEntity orderRO = new MerchantOrderEntity();
        orderRO.setReceiverName("xqh");
        orderRO.setReceiverLongitude(12.3456789120);
        orderRO.setReceiverLatitude(32.8765432189);
        orderRO.setDeliveryFee(12.45);
        orderRO.setPayMethod("1");
        orderRO.setReceiverAddressDetail("浙江省杭州市近江时代大厦");
        orderRO.setReceiverAddressProvince("近江时代大厦");
        orderRO.setReceiverPhone("18668123035");
        String orderNo =  merchantOrderService.order(userId,orderRO);

        MerchantOrderEntity entity0 = merchantOrderService.findByOrderNo("qqq");
        int payResult = merchantOrderService.merchantPay(userId,"qqq",19000);
        MerchantOrderEntity entity = merchantOrderService.findByOrderNo("qqq");
        Assert.assertEquals(EnumMerchantOrderStatus.WAITING_GRAB.getValue(),entity.getOrderStatus());

//        int expireResult = merchantOrderService.orderExpire("qqq");
//        MerchantOrderEntity entity2 = merchantOrderService.findByOrderNo("qqq");
//        Assert.assertEquals(EnumMerchantOrderStatus.OVERTIME.getValue(),entity2.getOrderStatus());
        boolean cancelCount = merchantOrderService.meachantCancel("1234","qqq");
        Assert.assertEquals(true,cancelCount);
        int grabCount = merchantOrderService.riderGrabOrder("111","hhh","18911111111","qqq",new Date());
        Assert.assertEquals(1,grabCount);
        int pickCount = merchantOrderService.riderGrabItem("qqq",new Date());
        Assert.assertEquals(1,pickCount);
        int finishCount = merchantOrderService.riderFinishOrder("qqq",new Date());
        Assert.assertEquals(1,finishCount);
    }
}

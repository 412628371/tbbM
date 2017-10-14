package com.xinguang.tubobo.merchant.web.test;

import com.xinguang.tubobo.Application;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.dto.MerchantTaskOperatorCallbackDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by yanxu on 2017/10/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestService {
    @Autowired
    private MerchantOrderManager orderManager;
    @Test
    public void testService(){
        MerchantTaskOperatorCallbackDTO dto = new MerchantTaskOperatorCallbackDTO();
        dto.setSubsidy(1.0);
        dto.setExpiredCompensation(2.0);
        dto.setFine(3.0);
        dto.setTaskNo("031710140000106");

        orderManager.dealFromRiderCancelOrders(dto);
    }
}
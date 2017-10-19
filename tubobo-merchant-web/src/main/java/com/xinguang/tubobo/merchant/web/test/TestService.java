package com.xinguang.tubobo.merchant.web.test;

import com.xinguang.tubobo.Application;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.impl.merchant.service.MerchantSettingsService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.dto.MerchantTaskOperatorCallbackDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by yanxu on 2017/10/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class
TestService {
    @Autowired
    private MerchantOrderManager orderManager;
    @Autowired
    private MerchantSettingsService merchantSettingsService;
    @Test
//    @Transactional(propagation = Propagation.NEVER)
    public void testService(){
/*        MerchantTaskOperatorCallbackDTO dto = new MerchantTaskOperatorCallbackDTO();
        dto.setSubsidy(1.0);
        dto.setExpiredCompensation(2.0);
        dto.setFine(3.0);
        dto.setTaskNo("031710140000106");*/
        MerchantSettingsEntity settingsEntity = new MerchantSettingsEntity();
        settingsEntity.setUserId("12345");

        merchantSettingsService.updateSettings("123",settingsEntity);
        int a=1/0;
       // orderManager.dealFromRiderCancelOrders(dto);
    }
}
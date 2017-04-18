package com.xinguang.tubobo.impl.test.service;

import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.test.base.BaseJunit4Test;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/14.
 */
public class MerchantInfoServiceUnitTest extends BaseJunit4Test {
    Logger logger = LoggerFactory.getLogger(MerchantInfoServiceUnitTest.class);
    @Resource
    MerchantInfoService merchantInfoService;
    @Test
    @Transactional
    @Rollback(false)
    public void testMerchantApply(){
        MerchantInfoEntity merchantShopRO = new MerchantInfoEntity();
        merchantShopRO.setAddressProvince("杭州市近江时代大厦20楼");
        merchantShopRO.setAddressDetail("近江时代大厦");
        merchantShopRO.setIdCardBackImageUrl("http://www.baidu.com");
        merchantShopRO.setIdCardFrontImageUrl("http://www.sohu.com");
        merchantShopRO.setLatitude(123.8909090909);
        merchantShopRO.setLongitude(12.7865123490);
        merchantShopRO.setIdCardNo("423123199001239078");
        merchantShopRO.setMerchantName("宠物店");
        merchantShopRO.setPhone("18900001111");
        merchantShopRO.setAccountId(123456L);
        merchantInfoService.merchantApply("1234",merchantShopRO);

        int result = merchantInfoService.merchantStatusVerify("1234", EnumAuthentication.SUCCESS.getValue(),"1234");
        logger.info("update status result: "+result);
        merchantInfoService.updateHeadImage("1234","www.a.c");

        MerchantInfoEntity entity = merchantInfoService.findByUserId("1234");
        logger.info(entity.toString());
        Assert.assertEquals("18900001111",entity.getPhone());
        Assert.assertEquals( EnumAuthentication.SUCCESS.getValue(),entity.getMerchantStatus());
        Assert.assertEquals("www.a.c",entity.getAvatarUrl());

        merchantInfoService.findMerchantInfoPage(1,10,entity);
    }
}

package com.xinguang.tubobo.merchant.web;

import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.impl.merchant.service.MerchantPushService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2017/4/24.
 */
public class App {
    public static void main(String[] args) throws MerchantClientException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        MerchantPushService merchantPushService = (MerchantPushService) context.getBean("merchantPushService");
        merchantPushService.pushToUser("30126","test","订单被接单");
        DeliveryFeeService feeService = (DeliveryFeeService) context.getBean("deliveryFeeService");
        Double money = feeService.sumDeliveryFeeByDistance(4300.0);
        System.out.println(money);
    }

}

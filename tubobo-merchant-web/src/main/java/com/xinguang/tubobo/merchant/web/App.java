package com.xinguang.tubobo.merchant.web;

import com.xinguang.tubobo.impl.merchant.service.MerchantPushService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2017/4/24.
 */
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        MerchantPushService merchantPushService = (MerchantPushService) context.getBean("merchantPushService");
        merchantPushService.pushToUser("30126","test","订单被接单");
    }
}

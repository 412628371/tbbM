package com.xinguang.tubobo.merchant.web;

import com.xinguang.tubobo.impl.merchant.common.CodeGenerator;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/4/24.
 */
public class App {
    public static void main(String[] args) throws MerchantClientException, SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        CodeGenerator codeGenerator = (CodeGenerator) context.getBean("codeGenerator");
        for (int i = 0;i< 40;i++){
            String orderNo = codeGenerator.nextCustomerCode();
            System.out.println(orderNo);
        }
//        MerchantPushService merchantPushService = (MerchantPushService) context.getBean("merchantPushService");
//        merchantPushService.pushToUser("30126","test","订单被接单");
//        DeliveryFeeService feeService = (DeliveryFeeService) context.getBean("deliveryFeeService");
//        Double money = feeService.sumDeliveryFeeByDistance(4300.0);
//        System.out.println(money);
    }

}

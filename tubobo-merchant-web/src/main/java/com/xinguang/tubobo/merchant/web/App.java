package com.xinguang.tubobo.merchant.web;

import com.xinguang.tubobo.merchant.api.MerchantClientException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/4/24.
 */
public class App {
    public static void main(String[] args) throws MerchantClientException, SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-mvc.xml");
//        CodeGenerator codeGenerator = (CodeGenerator) context.getBean("codeGenerator");
//        for (int i = 0;i< 40;i++){
//            String orderNo = codeGenerator.nextCustomerCode();
//            System.out.println(orderNo);
//        }
//        MerchantPushService merchantPushService = (MerchantPushService) context.getBean("merchantPushService");
//        merchantPushService.pushToUser("30126","test","订单被接单");
//        DeliveryFeeService feeService = (DeliveryFeeService) context.getBean("deliveryFeeService");
//        Double minute = feeService.sumDistributionLimitation(20.0);
////        Double money = feeService.sumDeliveryFeeByDistance(5000.0);
//        System.out.println(minute);
//        RoutePlanning routePlanning = (RoutePlanning) context.getBean("routePlanning");
//        Double distance = routePlanning.getDistanceWithCar(116.481028,39.989643,114.465302,40.004717);
//        System.out.println("distance: "+distance);
//        RedisOp redisOp = (RedisOp)context.getBean("redisOp");
//        redisOp.initZero(MerchantConstants.KEY_PWD_WRONG_TIMES_FREE);
//        redisOp.increment(MerchantConstants.KEY_PWD_WRONG_TIMES_MODIFY,1);
//        Long time = redisOp.getLongValue(MerchantConstants.KEY_PWD_WRONG_TIMES_MODIFY);
//        System.out.println(time);
    }

}

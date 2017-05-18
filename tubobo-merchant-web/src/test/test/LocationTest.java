package com.xinguang.tubobo.impl.test;

import com.hzmux.hzcms.common.utils.LocationUtil;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.hzmux.hzcms.common.utils.LocationUtil.getDistance;
import static com.hzmux.hzcms.common.utils.SpringContextHolder.getBean;

/**
 * Created by Administrator on 2017/4/25.
 */
public class LocationTest {
    public static void main(String[] args) {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
//        DeliveryFeeService feeService = (DeliveryFeeService) applicationContext.getBean("deliveryFeeService");
       Double location =  LocationUtil.getDistance(30.251736,120.21557,30.2310148111979,120.195802951389);
        System.out.println(location);
    }
}

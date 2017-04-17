package com.xinguang.tubobo.impl.test.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import com.xinguang.tubobo.impl.rider.entity.RiderTaskEntity;
import com.xinguang.tubobo.impl.rider.service.RiderInfoService;
import com.xinguang.tubobo.impl.rider.service.RiderTaskService;
import com.xinguang.tubobo.impl.test.base.BaseJunit4Test;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

public class RiderTaskServiceUnitTest extends BaseJunit4Test {

    @Resource
    RiderTaskService riderTaskService;

    @Test
    @Transactional
    @Rollback(false)
    public void save(){
        RiderTaskEntity task = new RiderTaskEntity();

        task.setTaskNo("123452343246");
        task.setPayId(123452343246l);
        task.setTaskStatus(1);
        task.setReceiverName("setReceiverName");
        task.setReceiverPhone("setReceiverPhone");
        task.setReceiverLatitude(10.00000003d);
        task.setReceiverLongitude(10.00000002d);

        task.setSenderName("setSenderName");
        task.setSenderPhone("setSenderPhone");
        task.setSenderAvatarUrl("setSenderAvatarUrl");
        task.setSenderLatitude(10.00000004d);
        task.setSenderLongitude(10.00000052d);

        task.setDeliveryDistance(100d);

        task.setAcceptTime(new Date());

        task.setRiderId("100");
        task.setTipFee(500);
        task.setDeliveryFee(500);
        task.setPayAmount(1000);

        riderTaskService.save(task);
    }

//    @Test
//    @Transactional
//    @Rollback(false)
//    public void findByRiderIdAndTaskNo(){
//        RiderTaskEntity result = riderTaskService.findByRiderIdAndTaskNo("100","1");
//        System.out.print(result.getId());
//    }


//    @Test
//    @Transactional
//    @Rollback(false)
//    public void findRiderTaskPage(){
//        Page<RiderTaskEntity> page = riderTaskService.findRiderTaskPage(1,10,new RiderTaskEntity());
//        System.out.println(page.getPageNo());
//        System.out.println(page.getPageSize());
//        System.out.println(page.getCount());
//        System.out.println(page.getList().get(0).getId());
//    }
}

package com.xinguang.tubobo.impl.test.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import com.xinguang.tubobo.impl.rider.service.RiderInfoService;
import com.xinguang.tubobo.impl.test.base.BaseJunit4Test;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class RiderInfoServiceUnitTest extends BaseJunit4Test {

    @Resource
    RiderInfoService riderInfoService;

//    @Test
//    @Transactional
//    @Rollback(false)
//    public void riderApply(){
//        RiderInfoEntity rider = new RiderInfoEntity();
//        rider.setRealName("欧阳");
//        rider.setPhone("15088628805");
//        rider.setAccountId(100l);
//        rider.setIdCardNo("330327199008010259");
//        rider.setIdCardImageBack("http://www.baidu.com");
//        rider.setIdCardImageFront("http://www.sohu.com");
//
//        RiderInfoEntity result = riderInfoService.riderApply("100",rider);
//        System.out.print(result.getId());
//    }

//    @Test
//    @Transactional
//    @Rollback(false)
//    public void findByUserId(){
//        RiderInfoEntity result = riderInfoService.findByUserId("100");
//        System.out.print(result.getId());
//    }


//    @Test
//    @Transactional
//    @Rollback(false)
//    public void findRiderInfoPage(){
//        Page<RiderInfoEntity> page = riderInfoService.findRiderInfoPage(1,10,new RiderInfoEntity());
//        System.out.println(page.getPageNo());
//        System.out.println(page.getPageSize());
//        System.out.println(page.getCount());
//        System.out.println(page.getList().get(0).getId());
//    }
}

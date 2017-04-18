package com.xinguang.tubobo.impl.test.dao;

import com.xinguang.tubobo.impl.test.base.BaseJunit4Test;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * OrderDAO的单元测试类.
 */
public class OrderDAOTest extends BaseJunit4Test {
//    @Resource
//    OrderDAO orderDAO;
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void testUpdateOrderStatus(){
//        TaskOrderEntity taskOrderEntity = new TaskOrderEntity();
//        taskOrderEntity.setTaskNo("aaa");
//        taskOrderEntity.setDispatchType(DispatchTypeEnum.GRAB.getCode());
//        taskOrderEntity.setTaskStatus(TaskStatusEnum.INIT.getValue());
//        taskOrderEntity.setReceiverPhone("18668123035");
//        taskOrderEntity.setReceiverAddress("bbbbb");
//
//        orderDAO.save(taskOrderEntity);
//        orderDAO.updateOrderStatus(TaskStatusEnum.ACCEPTED.getValue(),taskOrderEntity.getTaskNo());
//        TaskOrderEntity actual = orderDAO.getByTaskNo(taskOrderEntity.getTaskNo());
//        Assert.assertEquals(taskOrderEntity.getTaskStatus(),actual.getTaskStatus());
//
//    }
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void testUpdateOrderStatusAndRider(){
//        TaskOrderEntity taskOrderEntity = new TaskOrderEntity();
//        taskOrderEntity.setTaskNo("bbb");
//        taskOrderEntity.setDispatchType(DispatchTypeEnum.GRAB.getCode());
//        taskOrderEntity.setTaskStatus(TaskStatusEnum.INIT.getValue());
//        taskOrderEntity.setReceiverPhone("18668123035");
//        taskOrderEntity.setReceiverAddress("bbbbb");
//        taskOrderEntity.setDeliveryFee(1234);
//        taskOrderEntity.setReceiverLatitude(12.9090909090);
//        orderDAO.save(taskOrderEntity);
//        int count = orderDAO.updateOrderStatusAndRiderInfo(TaskStatusEnum.WAITING_ACCEPTED.getValue(),
//                taskOrderEntity.getTaskNo(),"111","18911111111","xqhhh");
//        Assert.assertEquals(1,count);
//        TaskOrderEntity actual = orderDAO.getByTaskNo(taskOrderEntity.getTaskNo());
//        Assert.assertEquals(TaskStatusEnum.WAITING_ACCEPTED.getValue(),actual.getTaskStatus().intValue());
//        Assert.assertEquals("111",actual.getRiderId());
//
//    }
}

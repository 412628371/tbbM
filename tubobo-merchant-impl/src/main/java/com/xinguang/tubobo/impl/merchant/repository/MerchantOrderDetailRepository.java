/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.repository;
import com.xinguang.tubobo.impl.merchant.entity.OrderDetailEntity;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.Query;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

public interface MerchantOrderDetailRepository extends JpaRepository<OrderDetailEntity, String>, JpaSpecificationExecutor<OrderDetailEntity> {

    Logger logger = LoggerFactory.getLogger(MerchantOrderDetailRepository.class);

    /**
     * 订单信息
     */
    OrderDetailEntity findByOrderNoAndDelFlag(String orderNo, String delFlag);

    /**
     * 设置取消原因
     * @param orderNo
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set  a.updateDate =:updateDate, a.cancelReason=:cancelReason where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int updateCancelReason(@Param("orderNo")String orderNo,@Param("cancelReason")String cancelReason,@Param("updateDate")Date updateDate,@Param("delFlag")String delFlag);


    /**
     * update订单超时
     * @param orderNo
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set  a.expiredMinute =:expiredMinute, a.expiredCompensation=:expiredCompensation where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int riderFinishExpiredOrder(@Param("orderNo")String orderNo, @Param("expiredMinute") Double expiredMinute, @Param("expiredCompensation")Double expiredCompensation,@Param("delFlag")String delFlag);
    /**
     * 设置取消原因(带有处罚金额)
     * @param orderNo
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set  a.updateDate =:updateDate, a.cancelReason=:cancelReason ,  a.waitPickCancelType =:waitPickCancelType,  a.cancelFine =:cancelFine,  a.cancelCompensation =:cancelCompensation" +
            " where  a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int updateCancelReasonWithFine(@Param("updateDate")Date updateDate,@Param("cancelReason")String cancelReason,@Param("waitPickCancelType")String waitPickCancelType ,@Param("cancelFine")Double cancelFine,@Param("cancelCompensation")Double cancelCompensation,@Param("orderNo")String orderNo,@Param("delFlag")String delFlag);


    @Modifying
    @Query("update #{#entityName} a set a.unsettledTime =:unsettledTime" +
            ",a.merMessage =:merMessage where a.orderNo=:orderNo and a.delFlag =:delFlag")
    int merchantHandlerUnsettledOrder( @Param("unsettledTime")Date unsettledTime,@Param("merMessage")String merMessage,@Param("orderNo")String orderNo, @Param("delFlag")String  delFlag);

    @Modifying
    @Query("update #{#entityName} a set a.pickupDistance =:pickupDistance " +
            "where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int riderGrabOrder(@Param("pickupDistance")Double pickupDistance, @Param("orderNo")String orderNo, @Param("delFlag")String delFlag);

    @Modifying
    @Query("update #{#entityName} a set a.pickupDistance =:pickupDistance " +
            "where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int riderGrabOrderOfPost( @Param("pickupDistance") Double pickupDistance,@Param("orderNo")String orderNo,@Param("delFlag")String delFlag);

    @Modifying
    @Query("update #{#entityName} a set a.unsettledReason =:unsettledReason,a.expiredMinute =:expiredMinute where a.orderNo=:orderNo  and a.delFlag =:delFlag")
    int riderUnsettledOrder( @Param("unsettledReason")String unsettledReason,  @Param("expiredMinute")Double expiredMinute, @Param("orderNo")String orderNo,@Param("delFlag") String delFlag);

}

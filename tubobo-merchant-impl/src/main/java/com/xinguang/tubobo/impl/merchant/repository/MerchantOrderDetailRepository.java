/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.repository;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.taskcenter.api.common.enums.PostOrderUnsettledStatusEnum;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.OrderDetailEntity;
import com.xinguang.tubobo.impl.merchant.entity.OrderEntity;
import com.xinguang.tubobo.merchant.api.enums.EnumCancelReason;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumPayStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.Query;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
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
    int updateCancelReason(String orderNo,String cancelReason,Date updateDate,String delFlag);


    /**
     * update订单超时
     * @param orderNo
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set  a.expiredMinute =:expiredMinute, a.expiredCompensation=:expiredCompensation where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int riderFinishExpiredOrder(String orderNo,  Double expiredMinute, Double expiredCompensation,String delFlag);
    /**
     * 设置取消原因(带有处罚金额)
     * @param orderNo
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set  a.updateDate =:updateDate, a.cancelReason=:cancelReason ,  a.waitPickCancelType =:waitPickCancelType,  a.cancelFine =:cancelFine,  a.cancelCompensation =:cancelCompensation" +
            " where  a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int updateCancelReasonWithFine(Date updateDate,String cancelReason,String waitPickCancelTypeString ,Double cancelFine,Double cancelCompensation,String orderNo,String delFlag);


    @Modifying
    @Query("update #{#entityName} a set a.unsettledTime =:unsettledTime" +
            ",a.merMessage =:merMessage where a.orderNo=:orderNo and a.delFlag =:delFlag")
    int merchantHandlerUnsettledOrder( Date unsettledTime,String merMessage,String orderNo, String  delFlag);

    @Modifying
    @Query("update #{#entityName} a set a.pickupDistance =:pickupDistance " +
            "where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int riderGrabOrder(Double pickupDistance, String orderNo, String delFlag);

    @Modifying
    @Query("update #{#entityName} a set a.pickupDistance =:pickupDistance " +
            "where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int riderGrabOrderOfPost(  Double pickupDistance,String orderNo,String delFlag);

    @Modifying
    @Query("update #{#entityName} a set a.unsettledReason =:unsettledReason,a.expiredMinute =:expiredMinute where a.orderNo=:orderNo  and a.delFlag =:delFlag")
    int riderUnsettledOrder( String unsettledReason,  Double expiredMinute, String orderNo, String delFlag);

}

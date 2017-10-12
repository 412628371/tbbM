/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface MerchantOrderRepository extends JpaRepository<OrderEntity, String>, JpaSpecificationExecutor<OrderEntity> {
    Logger logger = LoggerFactory.getLogger(MerchantOrderRepository.class);


   /* @Modifying
    @Query("update #{#entityName} a set a.evalFlag =:evalFlag, a.updateDate =:updateDate where a.orderNo =:orderNo and a.delFlag =:delFlag ")
    int updateEvalFlag(@Param("orderNo") String orderNo, @Param("delFlag") String delFlag, @Param("evalFlag") boolean evalFlag, @Param("updateDate") Date updateDate);
*/
    /**
     * 订单信息
     */
    OrderEntity findByOrderNoAndDelFlag(String orderNo, String delFlag);

    /**
     * 订单信息
     * (带有商家id)
     */
    OrderEntity findByOrderNoAndUserIdAndDelFlag(String orderNo, String userId, String delFlag);

    /**
     * 订单信息
     *
     * @param orderNo
     * @param orderStatus
     * @param delFlag
     */
    OrderEntity findByOrderNoAndOrderStatusAndDelFlag(String orderNo, String orderStatus, String delFlag);

  /*  *//**
     * 商家确认未妥投
     * @param orderNo
     * @return
     *//*
    @Modifying
    @Query("update #{#entityName} a set a.evalFlag =:evalFlag, a.updateDate =:updateDate where a.orderNo =:orderNo and a.delFlag =:delFlag ")
    int abortConfirm(String orderNo,Boolean confirm,String message,String userId)*/

    /**
     * 订单取消
     *
     * @param orderNo
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.orderStatus =:orderStatus, a.updateDate =:updateDate, a.cancelTime=:cancelTime where a.orderNo =:orderNo and a.orderStatus in:whereStatus and a.delFlag =:delFlag ")
    int orderCancel(@Param("orderNo")String orderNo, @Param("cancelTime")Date cancelTime, @Param("orderStatus")String orderStatus,@Param("updateDate") Date updateDate,  @Param("whereStatus")List<String>  whereStatus, @Param("delFlag")String delFlag);

    /**
     * 重新发单
     * @param originOrderNo
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.orderStatus =:orderStatus, a.updateDate =:updateDate where a.orderNo =:orderNo and orderStatus=:whereStatus and a.delFlag =:delFlag ")
    int orderResend(@Param("orderNo")String orderNo,@Param("orderStatus")String orderStatus, @Param("whereStatus")String whereStatus,@Param("updateDate")Date updateDate,@Param("delFlag")String delFlag);

    /***
     * 订单支付
     * @param merchantId
     * @param orderNo
     * @param payId
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.orderStatus =:orderStatus,a.payStatus=:payStatus ,a.payTime=:payTime,a.payId=:payId,a.updateDate =:payTime " +
            "where a.userId=:userId and a.orderNo =:orderNo and orderStatus=:whereStatus and a.delFlag =:delFlag ")
    int merchantPay(@Param("orderStatus")String orderStatus,@Param("payStatus")String payStatus ,@Param("payTime")Date payTime,@Param("payId")long payId,@Param("userId")String userId,@Param("orderNo")String orderNo,@Param("whereStatus")String whereStatus,@Param("delFlag")String delFlag);


    /**
     * 骑手抢单
     * @param riderId
     * @param riderName
     * @param riderPhone
     * @param orderNo
     * @param grabOrderTime
     * @param pickupDistance
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.orderStatus =:orderStatus,a.riderId=:riderId ,a.riderPhone=:riderPhone,a.riderName=:riderName,a.grabOrderTime =:grabOrderTime,a.expectFinishTime =:expectFinishTime " +
            " where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
    int riderGrabOrder(@Param("orderStatus")String orderStatus, @Param("riderId")String riderId,@Param("riderName") String riderName, @Param("riderPhone")String riderPhone, @Param("orderNo")String orderNo, @Param("grabOrderTime")Date grabOrderTime,
                       @Param("expectFinishTime")Date expectFinishTime, @Param("delFlag")String delFlag);

   /**
     * 驿站订单骑手接单与取货
     * 同时修改取货和抢货时间
     * @param riderId
     * @param riderName
     * @param riderPhone
     * @param orderNo
     * @param grabOrderTime
     * @param expectFinishTime
     * @param pickupDistance
     * @return
     */
   @Modifying
   @Query("update #{#entityName} a set a.orderStatus =:orderStatus,a.riderId=:riderId ,a.riderPhone=:riderPhone,a.riderName=:riderName,a.grabOrderTime =:grabOrderTime,a.grabItemTime=:grabOrderTime,a.expectFinishTime =:expectFinishTime " +
           "where a.orderNo =:orderNo  and a.delFlag =:delFlag ")
   int riderGrabOrderOfPost(@Param("orderStatus")String orderStatus,@Param("riderId")String riderId, @Param("riderName")String riderName, @Param("riderPhone")String riderPhone, @Param("orderNo")String orderNo, @Param("grabOrderTime")Date grabOrderTime,
                            @Param("expectFinishTime")Date expectFinishTime,@Param("delFlag")String delFlag);


   /**
     * 骑手取货
     * @param orderNo
     * @param grabItemTime
     * @return
     */
   @Modifying
   @Query("update #{#entityName} a set a.orderStatus =:orderStatus,a.grabItemTime=:grabItemTime where a.orderNo =:orderNo  and orderStatus=:whereStatus and a.delFlag =:delFlag ")
   int riderGrabItem(@Param("orderStatus")String orderStatus, @Param("grabItemTime")Date grabItemTime,@Param("orderNo")String orderNo,@Param("whereStatus")String whereStatus,@Param("delFlag")String delFlag );



     /**
     * 骑手完成送货
     * @param orderNo
     * @param finishOrderTime
     * @return
     */
     @Modifying
     @Query("update #{#entityName} a set a.orderStatus =:orderStatus,a.finishOrderTime=:finishOrderTime where a.orderNo =:orderNo  and orderStatus=:whereStatus and a.delFlag =:delFlag ")
     int riderFinishOrder(@Param("orderStatus")String orderStatus,@Param("finishOrderTime") Date finishOrderTime,@Param("orderNo")String orderNo,@Param("whereStatus")String whereStatus,@Param("delFlag")String delFlag );




    /**
     * 商家删除订单
     * @param orderNo
     * @param
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.delFlag =:delFlag where a.userId=:userId and a.orderNo =:orderNo and a.orderStatus in:whereStatus and a.delFlag =:whereDelFlag  ")
    int deleteOrder(@Param("delFlag")String delFlag,@Param("userId")String userId,@Param("orderNo")String orderNo, @Param("whereStatus")List<String>  whereStatus,@Param("whereDelFlag")String whereDelFlag);


    /**
     * 管理员关闭订单
     * @param orderNo
     * @return
     *//*
    @Modifying
    @Query("update #{#entityName} a set a.orderStatus =:orderStatus,a.closeTime =:closeTime where  a.orderNo =:orderNo and a.delFlag =:whereDelFlag  ")
    int adminClose(String orderStatus,Date closeTime,String orderNo,String whereDelFlag);*/

    /**
     * 查询
     * @param diff
     * @return
     */
    //TODO 待验证
    @Query(value ="select order_no FROM tubobo_order WHERE timediff(NOW(), pay_time)> :diff and order_status = 'WAITING_GRAB' ORDER BY pay_time desc ", nativeQuery = true )
    List<String> getUnCanceledGrabOvertimeOrderNoList(Date diff);


    /**
     * 查询每日发单量
     * @return
     */
    //TODO
    @Query(value ="select count(order_no) FROM tubobo_order WHERE finish_order_time >:now and user_id=:user_id  and del_flag=:del_flag  ", nativeQuery = true )
    Long getTodayFinishOrderNum(@Param("now")Date now,@Param("user_id")String user_id,@Param("del_flag")String del_flag);



    /**
     * 查询每日发单量(含短信)
     * @return
     */
    //TODO
    @Query(value ="select count(order_no) FROM tubobo_order WHERE grab_item_time >:now and user_id=:user_id and short_message=:short_message and del_flag=:del_flag  ", nativeQuery = true )
    Long getTodayFinishOrderWithShortTextNum(@Param("now")Date now,@Param("user_id")String user_id,@Param("short_message")Boolean short_message,@Param("del_flag")String del_flag);

    /**
     * 更新订单的状态为已评价
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.ratedFlag =:ratedFlag where a.orderNo=:orderNo  and a.delFlag =:whereDelFlag  ")
    int rateOrder(@Param("ratedFlag")Boolean ratedFlag,@Param("orderNo")String orderNo,@Param("whereDelFlag")String whereDelFlag);


    @Query(value ="select order_no FROM tubobo_order WHERE order_status=:order_status and  rated_flag=:rated_flag and finish_order_time >:beginDate and finish_order_time <:endDate and del_flag=:del_flag  ", nativeQuery = true )
    List<String> getUnRatedOrderNos(@Param("order_status")String order_status,@Param("rated_flag")Boolean rated_flag,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate, @Param("del_flag")String  del_flag);


    @Modifying
    @Query("update #{#entityName} a set a.unsettledStatus =:unsettledStatus,a.finishOrderTime =:finishOrderTime where a.orderNo=:orderNo and orderStatus=:orderStatus and unsettledStatus is null and a.delFlag =:delFlag")
    int riderUnsettledOrder(@Param("unsettledStatus")String unsettledStatus, @Param("finishOrderTime")Date finishOrderTime,@Param("orderNo") String orderNo, @Param("orderStatus")String orderStatus);


    @Modifying
    @Query("update #{#entityName} a set a.unsettledStatus =:unsettledStatus,a.orderStatus =:orderStatus,a.unsettledTime =:unsettledTime" +
            " where a.orderNo=:orderNo and a.orderStatus=:orderStatus and a.unsettledStatus =:whereUnsettledStatus and a.delFlag =:delFlag")
    int merchantHandlerUnsettledOrder(@Param("unsettledStatus")String unsettledStatus, @Param("orderStatus")String orderStatus,@Param("unsettledTime") Date unsettledTime,@Param("orderNo")String orderNo,@Param("whereUnsettledStatus")String whereUnsettledStatus,@Param("delFlag") String  delFlag);







    /**
     * 商家订单分页查询
     *//*
    public Page<MerchantOrderEntity> findMerchantOrderPageToApp(int pageNo, int pageSize, MerchantOrderEntity entity){
        Parameter parameter = new Parameter();
        StringBuffer sb = new StringBuffer();
        //TODO 优化 select *
        sb.append("select * from tubobo_merchant_order where del_flag = '0' ");
        if (StringUtils.isNotBlank(entity.getUserId())){
            sb.append("and user_id =:user_id  ");
            parameter.put("user_id", entity.getUserId());
        }
         //1.41版本增添待处理字段(已取消、待付款合并为待处理)
        if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                 MerchantConstants.ORDER_LIST_QUERY_CONDITION_UNHANDLE.equals(entity.getOrderStatus())){
             sb.append("and (order_status = 'CANCEL' or order_status='INIT') ");
        }else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                 MerchantConstants.ORDER_LIST_QUERY_CONDITION_FINISH.equals(entity.getOrderStatus())){
             sb.append("and (order_status = 'RESEND' or order_status='FINISH') ");
        }else if (EnumMerchantOrderStatus.UNDELIVERED.getValue().equals(entity.getOrderStatus())){
            //未妥投
            sb.append("and unsettled_status =:unsettled_status ");
            parameter.put("unsettled_status",PostOrderUnsettledStatusEnum.ING.getValue());
            sb.append("and order_status =:order_status ");
            parameter.put("order_status", EnumMerchantOrderStatus.DELIVERYING.getValue());
        }else if(EnumMerchantOrderStatus.CONFIRM.getValue().equals(entity.getOrderStatus())){
            //已经确认
            sb.append("and unsettled_status =:unsettled_status ");
            parameter.put("unsettled_status",PostOrderUnsettledStatusEnum.FINISH.getValue());
            sb.append("and order_status =:order_status ");
            parameter.put("order_status", EnumMerchantOrderStatus.FINISH.getValue());
        } else if (EnumMerchantOrderStatus.DELIVERYING.getValue().equals(entity.getOrderStatus())){
            //待配送
            sb.append("and unsettled_status is null ");
            sb.append("and order_status =:order_status ");
            parameter.put("order_status", entity.getOrderStatus());
        }
        else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){
                sb.append("and order_status =:order_status ");
                parameter.put("order_status", entity.getOrderStatus());
        }
        if (StringUtils.isNotBlank(entity.getOrderType())){
            sb.append("and order_type =:order_type ");
            parameter.put("order_type", entity.getOrderType());
        }
        sb.append(" order by create_date desc ");

//        sb.append("select new com.xinguang.tubobo.impl.merchant.entity.OrderPojo(id,platformCode, userId, orderNo, orderType, ratedFlag, orderStatus, payAmount, deliveryFee,  tipFee,  orderTime,  grabOrderTime,  grabItemTime,  finishOrderTime,  receiverName,  receiverPhone,  receiverAddressProvince,  receiverAddressCity,  receiverAddressDistrict,  receiverAddressStreet,  receiverAddressDetail, receiverAddressRoomNo) from MerchantOrderEntity where delFlag = '0' ");
//        if (StringUtils.isNotBlank(entity.getUserId())){
//            sb.append("and userId =:userId  ");
//            parameter.put("userId", entity.getUserId());
//        }
//        if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
//                !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){
//            sb.append("and orderStatus =:orderStatus ");
//            parameter.put("orderStatus", entity.getOrderStatus());
//        }
//        if (StringUtils.isNotBlank(entity.getOrderType())){
//            sb.append("and orderType =:orderType ");
//            parameter.put("orderType", entity.getOrderType());
//        }
//
//        sb.append(" order by createDate desc ");
//        List<OrderPojo> list =  createQuery(sb.toString(),parameter).list();
        return findPage(sb.toString(), parameter, MerchantOrderEntity.class,pageNo,pageSize);
    }

    *//**
     * 驿站订单分页查询
     * @return
     *//*
    public Page<MerchantOrderEntity> findMerchantOrderPageToPostHouse(int pageNo, int pageSize, String expectFinishTimeSort,
                                                                      String orderTimeSort, MerchantOrderEntity entity){
        Parameter parameter = new Parameter();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from tubobo_merchant_order where del_flag = '0' ");
        if (null != entity.getProviderId()){
            sb.append("and provider_id =:provider_id  ");
            parameter.put("provider_id", entity.getProviderId());
        }
        if (StringUtils.isNotBlank(entity.getOrderType())){
            sb.append("and order_type =:order_type ");
            parameter.put("order_type", entity.getOrderType());
        }
        if (StringUtils.isNotBlank(entity.getOrderStatus())){
            sb.append("and order_status =:order_status ");
            if (StringUtils.isNotBlank(entity.getUnsettledStatus())){
                sb.append("and unsettled_status =:unsettled_status ");
                parameter.put("unsettled_status", entity.getUnsettledStatus());
            }else{
                sb.append("and unsettled_status is null ");
            }
            parameter.put("order_status", entity.getOrderStatus());
        }
        if (StringUtils.isNotBlank(entity.getOrderNo())){
            sb.append("and order_no =:order_no ");
            parameter.put("order_no", entity.getOrderNo());
        }
        if (StringUtils.isNotBlank(entity.getReceiverPhone())){
            sb.append("and receiver_phone like :receiver_phone ");
            parameter.put("receiver_phone", entity.getReceiverPhone()+"%");
        }
        if (StringUtils.isNotBlank(entity.getRiderId())){
            sb.append("and rider_id =:rider_id ");
            parameter.put("rider_id", entity.getRiderId());
        }
        if (StringUtils.isNotBlank(entity.getRiderName())){
            sb.append("and rider_name like :rider_name ");
            parameter.put("rider_name", entity.getRiderName()+"%");
        }
        if (StringUtils.isNotBlank(entity.getSenderId())){
            sb.append("and sender_id =:sender_id ");
            parameter.put("sender_id", entity.getSenderId());
        }
        if (StringUtils.isNotBlank(entity.getSenderName())){
            sb.append("and sender_name like :sender_name ");
            parameter.put("sender_name", entity.getSenderName()+"%");
        }
        if (null != entity.getCreateDate()){
            sb.append("and order_time >=:create_date ");
            parameter.put("create_date", DateUtils.formatDateTime(entity.getCreateDate()));
        }
        if (null != entity.getUpdateDate()){
            sb.append("and order_time <=:update_date ");
            parameter.put("update_date", DateUtils.formatDateTime(entity.getUpdateDate()));
        }
        if(StringUtils.isNotBlank(expectFinishTimeSort)){
            sb.append(" order by expect_finish_time " + expectFinishTimeSort);
        }
        if(StringUtils.isNotBlank(orderTimeSort)){
            sb.append(" order by order_time " + orderTimeSort);
        }
        return findPage(sb.toString(), parameter, MerchantOrderEntity.class,pageNo,pageSize);
    }

    *//**
     * 后台查询商家订单，分页查询
     *//*
    public Page<MerchantOrderEntity> findMerchantOrderPageToAdmin(int pageNo, int pageSize, MerchantOrderEntity entity){
        Parameter parameter = new Parameter();
        StringBuffer sb = new StringBuffer();
        //TODO 优化 select *
        sb.append("select * from tubobo_merchant_order where del_flag = '0' ");
        if (StringUtils.isNotBlank(entity.getUserId())){
            sb.append("and user_id =:user_id  ");
            parameter.put("user_id", entity.getUserId());
        }
        if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){

            if (EnumMerchantOrderStatus.UNDELIVERED.getValue().equals(entity.getOrderStatus())){
                //未妥投
                sb.append("and unsettled_status =:unsettled_status ");
                parameter.put("unsettled_status",PostOrderUnsettledStatusEnum.ING.getValue());
                sb.append("and order_status =:order_status ");
                parameter.put("order_status", EnumMerchantOrderStatus.DELIVERYING.getValue());
            }else if(EnumMerchantOrderStatus.CONFIRM.getValue().equals(entity.getOrderStatus())){
                //已经确认
                sb.append("and unsettled_status =:unsettled_status ");
                parameter.put("unsettled_status",PostOrderUnsettledStatusEnum.FINISH.getValue());
                sb.append("and order_status =:order_status ");
                parameter.put("order_status", EnumMerchantOrderStatus.FINISH.getValue());
            } else {
                //正常
                sb.append("and order_status =:order_status ");
                parameter.put("order_status", entity.getOrderStatus());
                sb.append("and unsettled_status is  null ");
                sb.append("and order_status =:order_status ");
                parameter.put("order_status", entity.getOrderStatus());
            }
        }
        if (StringUtils.isNotBlank(entity.getOrderType())){
            sb.append("and order_type =:order_type ");
            parameter.put("order_type", entity.getOrderType());
        }
        if (StringUtils.isNotBlank(entity.getOrderNo())){
            sb.append("and order_no =:order_no ");
            parameter.put("order_no", entity.getOrderNo());
        }
        if (StringUtils.isNotBlank(entity.getRiderPhone())){
            sb.append("and rider_phone =:rider_phone ");
            parameter.put("rider_phone", entity.getRiderPhone());
        }
        if (StringUtils.isNotBlank(entity.getReceiverPhone())){
            sb.append("and receiver_phone like :receiver_phone ");
            parameter.put("receiver_phone", entity.getReceiverPhone()+"%");
        }
        if (StringUtils.isNotBlank(entity.getSenderName())){
            sb.append("and sender_name like :sender_name ");
            parameter.put("sender_name", entity.getSenderName()+"%");
        }
        if (null != entity.getCreateDate()){
            sb.append("and order_time >=:create_date ");
            parameter.put("create_date", DateUtils.getDateStart(entity.getCreateDate()));
        }
        if (null != entity.getUpdateDate()){
            sb.append("and order_time <=:update_date ");
            parameter.put("update_date", DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        if (StringUtils.isNotBlank(entity.getSenderAdcode())){
            sb.append("and sender_adcode like :sender_adcode ");
            parameter.put("sender_adcode", entity.getSenderAdcode()+"%");
        }
        if (null!=entity.getProviderId()){
            sb.append("and provider_id =:provider_id");
            parameter.put("provider_id", entity.getProviderId());
        }
        if (StringUtils.isNotBlank(entity.getProviderName())){
            sb.append("and provider_name =:provider_name");
            parameter.put("provider_name", entity.getProviderName());
        }
        sb.append(" order by create_date desc ");
        return findPage(sb.toString(), parameter, MerchantOrderEntity.class,pageNo,pageSize);
    }
    */



}

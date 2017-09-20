/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.merchant.api.enums.EnumCancelReason;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.enums.EnumPayStatus;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MerchantOrderDao extends BaseDao<MerchantOrderEntity> {
    Logger logger = LoggerFactory.getLogger(MerchantOrderDao.class);
    public MerchantOrderEntity findByOrderNo(String orderNo){
        String sqlString = "select * from tubobo_merchant_order where order_no = :p1 and del_flag = '0' ";
        List<MerchantOrderEntity> list = findBySql(sqlString, new Parameter(orderNo), MerchantOrderEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }
    public MerchantOrderEntity findByOrderNoAndStatus(String orderNo,String orderStatus){
        String sqlString = "select * from tubobo_merchant_order where order_no = :p1 and order_status=:p2 and del_flag = '0' ";
        List<MerchantOrderEntity> list = findBySql(sqlString, new Parameter(orderNo,orderStatus), MerchantOrderEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }
    public MerchantOrderEntity findByMerchantIdAndOrderNo(String merchantId, String orderNo){
        String sqlString = "select * from tubobo_merchant_order where user_id = :p1 and order_no = :p2 and del_flag = '0' ";
        List<MerchantOrderEntity> list = findBySql(sqlString, new Parameter(merchantId,orderNo), MerchantOrderEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }

    /**
     * 超时未抢单
     * @param orderNo
     * @return
     */
    public int orderExpire(String orderNo,Date expireTime){
        String sqlString = "update tubobo_merchant_order set order_status = :p1," +
                " update_date = :p2, cancel_time= :p3, cancel_reason= :p4  where order_no = :p5 and " +
                "order_status = :p6 and del_flag = '0' ";
        int count =  updateBySql(sqlString,
                new Parameter(EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue(),new Date(),expireTime,
                        EnumCancelReason.GRAB_OVERTIME.getValue(),orderNo,
                        EnumMerchantOrderStatus.WAITING_GRAB.getValue()));
        getSession().clear();
        return count;
    }

    /**
     * 重新发单
     * @param originOrderNo
     * @return
     */
    public int orderResend(String originOrderNo){
        String sqlString = "update tubobo_merchant_order set order_status = :p1," +
                " update_date = :p2 where order_no = :p3 and " +
                " order_status = :p4 and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.RESEND.getValue(), new Date(), originOrderNo, EnumMerchantOrderStatus.CANCEL.getValue()));
        getSession().clear();
        return count;
    }

    /**
     * 超时未支付
     * @param orderNo
     * @return
     */
    public int payExpire(String orderNo){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, update_date = :p2 ,cancel_reason = :p3 where order_no = :p4 and order_status = :p5 and del_flag = '0' ";
        int count =  updateBySql(sqlString,
                new Parameter(EnumMerchantOrderStatus.CANCEL_PAY_OVERTIME.getValue(),new Date(),EnumCancelReason.PAY_OVERTIME.getValue(),
                        orderNo,EnumMerchantOrderStatus.INIT.getValue()));
        getSession().clear();
        return count;
    }

    /***
     * 订单支付
     * @param merchantId
     * @param orderNo
     * @param payId
     * @return
     */
    public int merchantPay(String merchantId,String orderNo,long payId,Date payDate){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, pay_status = :p2, pay_time = :p3 ,pay_id = :p4 where sender_id = :p5 and order_no = :p6 and order_status = :p7 and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.WAITING_GRAB.getValue(),
                EnumPayStatus.PAID.getValue(),payDate,payId,
                merchantId,orderNo, EnumMerchantOrderStatus.INIT.getValue()));
        logger.info("支付操作数据库：count:{},merchantId:{},orderNo:{},payId:{},payDate:{}",count,merchantId,orderNo,payId,payDate);
        getSession().clear();
        return count;
    }

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
    public int riderGrabOrder(String riderId, String riderName, String riderPhone, String orderNo, Date grabOrderTime,
                              Date expectFinishTime, String riderCarNo, String riderCarType, Double pickupDistance){
        String updateQuery = "update "+MerchantOrderEntity.class.getSimpleName()+" set orderStatus = :orderStatus , riderId = :riderId ,riderPhone = :riderPhone,riderName=:riderName ,grabOrderTime = :grabOrderTime ," +
                " expectFinishTime=:expectFinishTime, riderCarNo=:riderCarNo, riderCarType=:riderCarType,pickupDistance=:pickupDistance" +
                " where orderNo = :orderNo and delFlag='0'";
        Parameter parameter = new Parameter();
        parameter.put("orderStatus",EnumMerchantOrderStatus.WAITING_PICK.getValue());
        parameter.put("riderId",riderId);
        parameter.put("riderPhone",riderPhone);
        parameter.put("riderName",riderName);
        parameter.put("riderCarNo",riderCarNo);
        parameter.put("riderCarType",riderCarType);
        parameter.put("grabOrderTime",grabOrderTime);
        parameter.put("expectFinishTime",expectFinishTime);
        parameter.put("orderNo",orderNo);
        parameter.put("pickupDistance",pickupDistance);
        int count =  update(updateQuery,parameter);
//        String sqlString = "update tubobo_merchant_order set order_status = :p1, grab_order_time = :p2, rider_id = :p3, rider_name = :p4, rider_phone = :p5 " +
//                " , expect_finish_time=:p6 where order_no = :p7 and order_status = :p8 and del_flag = '0' ";
//        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.WAITING_PICK.getValue(),grabOrderTime,riderId,riderName,
//                riderPhone,expectFinishTime,orderNo, EnumMerchantOrderStatus.WAITING_GRAB.getValue()));
        getSession().clear();
        return count;
    }

    /**
     * 骑手取货
     * @param orderNo
     * @param grabItemTime
     * @return
     */
    public int riderGrabItem(String orderNo, Date grabItemTime){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, grab_item_time = :p2 where order_no = :p3 and order_status = :p4 and del_flag = '0' ";
        int count =  updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.DELIVERYING.getValue(),
                grabItemTime,orderNo, EnumMerchantOrderStatus.WAITING_PICK.getValue()));
        getSession().clear();
        return count;
    }

    /**
     * 骑手完成送货
     * @param orderNo
     * @param finishOrderTime
     * @return
     */
    public int riderFinishOrder(String orderNo, Date finishOrderTime){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, finish_order_time = :p2 where order_no = :p3 and order_status = :p4 and del_flag = '0' ";
        int count =  updateBySql(sqlString,
                new Parameter(EnumMerchantOrderStatus.FINISH.getValue(),finishOrderTime,orderNo,
                        EnumMerchantOrderStatus.DELIVERYING.getValue()));
        getSession().clear();
        return count;
    }
    /**
     * 骑手超时完成送货
     * @param orderNo
     * @param finishOrderTime
     * @return
     */
    public int riderFinishExpiredOrder(String orderNo, Date finishOrderTime,double expiredMinute, double expiredCompensation){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, finish_order_time = :p2,expired_minute =:p3, expired_compensation =:p4 where order_no = :p5 and order_status = :p6 and del_flag = '0' ";
        int count =  updateBySql(sqlString,
                new Parameter(EnumMerchantOrderStatus.FINISH.getValue(),finishOrderTime,expiredMinute,expiredCompensation,orderNo,
                        EnumMerchantOrderStatus.DELIVERYING.getValue()));
        getSession().clear();
        return count;
    }
    /**
     *商家取消订单
     * @param merchantId
     * @param orderNo
     * @return
     */
    public boolean merchantCancel(String merchantId,String orderNo,String cancelReason,String waitPickCancelType,Double punishFee,Double subsidyFee){
        String[] orderStatusArr;
        if (StringUtils.isBlank(waitPickCancelType)){
            orderStatusArr = new String[]{EnumMerchantOrderStatus.INIT.getValue(), EnumMerchantOrderStatus.WAITING_GRAB.getValue()};
        }else{
            //不为空说明 处于带取货状态
            orderStatusArr = new String[]{EnumMerchantOrderStatus.WAITING_PICK.getValue()};
        }
        String sqlString = "update tubobo_merchant_order set order_status = :p1, cancel_time = :p2 , cancel_reason=:p3, wait_pick_cancel_type =:p4  ," +
                "cancel_fine=:p5 ,cancel_compensation=:p6 where sender_id = :p7 and order_no = :p8 and order_status in (:p9) and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.CANCEL.getValue(),
                new Date(),cancelReason,waitPickCancelType,punishFee,subsidyFee,merchantId,
                orderNo,orderStatusArr));
        getSession().clear();
        return count == 1;
    }

    /**
     * 取消订单（任何状态下都可取消）
     * @param orderNo
     * @param cancelReason
     * @return
     */
    public boolean adminCancel(String orderNo,String cancelReason){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, cancel_time = :p2 , cancel_reason=:p3 where order_no = :p4  and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.CANCEL.getValue(),
                new Date(),cancelReason,orderNo));
        getSession().clear();
        return count == 1;
    }
    /**
     * 骑手取消订单（待取货状态下）
     * @param orderNo
     * @param cancelReason
     * @param subsidy
     * @return
     */
    public boolean riderCancel(String orderNo, String cancelReason, Date date, Double subsidy){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, cancel_time = :p2 , cancel_reason=:p3 ,cancel_compensation=:p4 where order_no = :p5  and order_status=:p6  and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.CANCEL.getValue(),
                date,cancelReason,subsidy,orderNo,EnumMerchantOrderStatus.WAITING_PICK.getValue()));
        getSession().clear();
        return count == 1;
    }
    public int deleteOrder(String merchantId,String orderNo){
        String[] orderStatusArr = new String[]{EnumMerchantOrderStatus.INIT.getValue(),
                EnumMerchantOrderStatus.CANCEL_GRAB_OVERTIME.getValue(),
                EnumMerchantOrderStatus.CANCEL.getValue(),EnumMerchantOrderStatus.CANCEL_PAY_OVERTIME.getValue()};
        String sqlString = "update tubobo_merchant_order set del_flag = :p1, update_date = :p2 where sender_id = :p3 and order_no = :p4 and order_status in (:p5) and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(MerchantOrderEntity.DEL_FLAG_DELETE,new Date(),merchantId,orderNo,orderStatusArr));
        getSession().clear();
        return count;
    }
    /**
     * 商家订单分页查询
     */
    public Page<MerchantOrderEntity> findMerchantOrderPageToApp(int pageNo, int pageSize, MerchantOrderEntity entity){
        Parameter parameter = new Parameter();
        StringBuffer sb = new StringBuffer();
        //TODO 优化 select *
        sb.append("select * from tubobo_merchant_order where del_flag = '0' ");
        if (StringUtils.isNotBlank(entity.getUserId())){
            sb.append("and user_id = :user_id  ");
            parameter.put("user_id", entity.getUserId());
        }
         //1.41版本增添待处理字段(已取消、待付款合并为待处理)
        if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                 MerchantConstants.ORDER_LIST_QUERY_CONDITION_UNHANDLE.equals(entity.getOrderStatus())){
             sb.append("and (order_status = 'CANCEL' or order_status='INIT') ");
        }else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                 MerchantConstants.ORDER_LIST_QUERY_CONDITION_FINISH.equals(entity.getOrderStatus())){
             sb.append("and (order_status = 'RESEND' or order_status='FINISH') ");
        }else if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){
             sb.append("and order_status = :order_status ");
             parameter.put("order_status", entity.getOrderStatus());
        }
        if (StringUtils.isNotBlank(entity.getOrderType())){
            sb.append("and order_type = :order_type ");
            parameter.put("order_type", entity.getOrderType());
        }
        sb.append(" order by create_date desc ");

//        sb.append("select new com.xinguang.tubobo.impl.merchant.entity.OrderPojo(id,platformCode, userId, orderNo, orderType, ratedFlag, orderStatus, payAmount, deliveryFee,  tipFee,  orderTime,  grabOrderTime,  grabItemTime,  finishOrderTime,  receiverName,  receiverPhone,  receiverAddressProvince,  receiverAddressCity,  receiverAddressDistrict,  receiverAddressStreet,  receiverAddressDetail, receiverAddressRoomNo) from MerchantOrderEntity where delFlag = '0' ");
//        if (StringUtils.isNotBlank(entity.getUserId())){
//            sb.append("and userId = :userId  ");
//            parameter.put("userId", entity.getUserId());
//        }
//        if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
//                !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){
//            sb.append("and orderStatus = :orderStatus ");
//            parameter.put("orderStatus", entity.getOrderStatus());
//        }
//        if (StringUtils.isNotBlank(entity.getOrderType())){
//            sb.append("and orderType = :orderType ");
//            parameter.put("orderType", entity.getOrderType());
//        }
//
//        sb.append(" order by createDate desc ");
//        List<OrderPojo> list =  createQuery(sb.toString(),parameter).list();
        return findPage(sb.toString(), parameter, MerchantOrderEntity.class,pageNo,pageSize);
    }

    /**
     * 后台查询商家订单，分页查询
     */
    public Page<MerchantOrderEntity> findMerchantOrderPageToAdmin(int pageNo, int pageSize, MerchantOrderEntity entity){
        Parameter parameter = new Parameter();
        StringBuffer sb = new StringBuffer();
        //TODO 优化 select *
        sb.append("select * from tubobo_merchant_order where del_flag = '0' ");
        if (StringUtils.isNotBlank(entity.getUserId())){
            sb.append("and user_id = :user_id  ");
            parameter.put("user_id", entity.getUserId());
        }
        if (StringUtils.isNotBlank(entity.getOrderStatus()) &&
                !MerchantConstants.ORDER_LIST_QUERY_CONDITION_ALL.equals(entity.getOrderStatus())){
            sb.append("and order_status = :order_status ");
            parameter.put("order_status", entity.getOrderStatus());
        }
        if (StringUtils.isNotBlank(entity.getOrderType())){
            sb.append("and order_type = :order_type ");
            parameter.put("order_type", entity.getOrderType());
        }
        if (StringUtils.isNotBlank(entity.getOrderNo())){
            sb.append("and order_no = :order_no ");
            parameter.put("order_no", entity.getOrderNo());
        }
        if (StringUtils.isNotBlank(entity.getRiderPhone())){
            sb.append("and rider_phone = :rider_phone ");
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
        sb.append("and order_time >= :create_date ");
        parameter.put("create_date", DateUtils.getDateStart(new Date()));
        sb.append("and order_time <= :update_date ");
        parameter.put("update_date", DateUtils.getDateEnd(new Date()));
//        if (null != entity.getCreateDate()){
//        }
//        if (null != entity.getUpdateDate()){
//        }
        if (StringUtils.isNotBlank(entity.getSenderAdcode())){
            sb.append("and sender_adcode like :sender_adcode ");
            parameter.put("sender_adcode", entity.getSenderAdcode()+"%");
        }
        sb.append(" order by create_date desc ");
        return findPage(sb.toString(), parameter, MerchantOrderEntity.class,pageNo,pageSize);
    }
    /**
     * 管理员关闭订单
     * @param orderNo
     * @return
     */
    public int adminClose(String orderNo){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, close_time = :p2 where order_no = :p3 and del_flag = '0' ";
        int count =  updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.CLOSE.getValue(),new Date(),orderNo));
        getSession().clear();
        return count;
    }


    public List<String> getUnCanceledGrabOvertimeOrderNoList(Integer overTimeMilSeconds){
        Date diff = new Date(overTimeMilSeconds);
        String sqlString = "select orderNo FROM MerchantOrderEntity WHERE timediff(NOW(), payTime)> :p1 and orderStatus = 'WAITING_GRAB' ORDER BY payTime desc ";
        List<String> orderList = createQuery(sqlString,new Parameter(diff)).list();
        return orderList;
    }
    public Long getTodayFinishOrderNum(String userId){
//        Criteria criteria = getSession().createCriteria(MerchantOrderEntity.class);
//        criteria.add(Restrictions.or(Restrictions.eq("orderStatus",EnumMerchantOrderStatus.FINISH.getValue())));
//        criteria.add(Restrictions.or(Restrictions.eq("orderStatus",EnumMerchantOrderStatus.RATED.getValue())));
//        criteria.add(Restrictions.and(Restrictions.eq("userId",userId)));
//        criteria.add(Restrictions.and(Restrictions.eq("delFlag",MerchantOrderEntity.DEL_FLAG_NORMAL)));
//        criteria.add(Restrictions.and(Restrictions.gt("finishOrderTime",DateUtils.getDateStart(new Date()))));

        String sqlString = "select count(orderNo) FROM MerchantOrderEntity WHERE  userId=:p1 and finishOrderTime > :p2 and delFlag=:p3";
         Query query = createQuery(sqlString,new Parameter(userId,DateUtils.getDateStart(new Date()),MerchantOrderEntity.DEL_FLAG_NORMAL));
        Long count = (Long) query.uniqueResult();
        return count;
    }

    public int rateOrder(String orderNo) {
        String sqlString = "update MerchantOrderEntity set ratedFlag=:p1 where orderNo = :p2 and  delFlag = '0' ";
        return update(sqlString,new Parameter(true,orderNo));
    }

    public List<String> getUnRatedOrderNos(int days){
        String sqlString = "select orderNo from MerchantOrderEntity where orderStatus=:p1 and ratedFlag = :p2 and finishOrderTime>:p3 and finishOrderTime <:p4 and delFlag = '0' ";
        Date daysBefore = DateUtils.getDaysBefore(new Date(),days);
        Date begin = DateUtils.getDateStart(daysBefore);
        Date end = DateUtils.getDateEnd(daysBefore);
        List<String> orderList = createQuery(sqlString,
                new Parameter(EnumMerchantOrderStatus.FINISH.getValue(),false,begin,end)).list();
        return orderList;
    }
}

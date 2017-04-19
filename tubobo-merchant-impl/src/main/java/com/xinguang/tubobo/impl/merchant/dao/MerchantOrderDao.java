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
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.enums.EnumPayStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MerchantOrderDao extends BaseDao<MerchantOrderEntity> {
    public MerchantOrderEntity findByOrderNo(String orderNo){
        String sqlString = "select * from tubobo_merchant_order where order_no = :p1 and del_flag = '0' ";
        List<MerchantOrderEntity> list = findBySql(sqlString, new Parameter(orderNo), MerchantOrderEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }

    public int merchantPay(String merchantId,String orderNo,long payId){
        String sqlString = "update tubobo_merchant_order set order_status = :p1, pay_status = :p2, pay_time = :p3 ,pay_id = :p4 where sender_id = :p5 and order_no = :p6 and order_status = :p7 and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.WAITING_GRAB.getValue(),
                EnumPayStatus.PAID.getValue(),new Date(),payId,
                merchantId,orderNo, EnumMerchantOrderStatus.INIT.getValue()));
        getSession().clear();
        return count;
    }
    public boolean merchantCancel(String merchantId,String orderNo){
        String[] orderStatusArr = new String[]{EnumMerchantOrderStatus.INIT.getValue(), EnumMerchantOrderStatus.WAITING_GRAB.getValue()};
        String sqlString = "update tubobo_merchant_order set order_status = :p1, cancel_time = :p2 where sender_id = :p3 and order_no = :p4 and order_status in (:p5) and del_flag = '0' ";
        int count = updateBySql(sqlString, new Parameter(EnumMerchantOrderStatus.CANCEL.getValue(),new Date(),merchantId,orderNo,orderStatusArr));
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
    public Page<MerchantOrderEntity> findMerchantOrderPage(int pageNo, int pageSize, MerchantOrderEntity entity){
        Parameter parameter = new Parameter();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from tubobo_merchant_order where del_flag = '0' ");
        if (StringUtils.isNotBlank(entity.getSenderId())){
            sb.append("and sender_id = :sender_id ");
            parameter.put("sender_id", entity.getSenderId());
        }
        if (StringUtils.isNotBlank(entity.getOrderStatus())){
            sb.append("and order_status = :order_status ");
            parameter.put("order_status", entity.getOrderStatus());
        }
        if (StringUtils.isNotBlank(entity.getOrderNo())){
            sb.append("and order_no like :order_no ");
            parameter.put("order_no", "%"+entity.getOrderNo()+"%");
        }
        if (StringUtils.isNotBlank(entity.getRiderPhone())){
            sb.append("and rider_phone like :rider_phone ");
            parameter.put("rider_phone", "%"+entity.getRiderPhone()+"%");
        }
        if (StringUtils.isNotBlank(entity.getReceiverPhone())){
            sb.append("and receiver_phone like :receiver_phone ");
            parameter.put("receiver_phone", "%"+entity.getReceiverPhone()+"%");
        }

        if (null != entity.getCreateDate()){
            sb.append("and order_time >= :create_date ");
            parameter.put("create_date", DateUtils.getDateStart(entity.getCreateDate()));
        }
        if (null != entity.getUpdateDate()){
            sb.append("and order_time <= :update_date ");
            parameter.put("update_date", DateUtils.getDateEnd(entity.getUpdateDate()));
        }
        sb.append(" order by create_date desc ");
        return findPage(sb.toString(), parameter, MerchantOrderEntity.class,pageNo,pageSize);
    }
}

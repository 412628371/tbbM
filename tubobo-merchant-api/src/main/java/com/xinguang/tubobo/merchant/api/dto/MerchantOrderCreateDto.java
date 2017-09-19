package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yangxb on 2017/9/11.
 */
public class MerchantOrderCreateDto implements Serializable {

   private String userId; //Y
   private Date userAppointTime;//用户预约时间
   private String  orderRemarks;//	备注	string	　	O
   private AddressInfoDTO consignor;//	发货人信息	object
   private AddressInfoDTO receiver;

   public Date getUserAppointTime() {
      return userAppointTime;
   }

   public void setUserAppointTime(Date userAppointTime) {
      this.userAppointTime = userAppointTime;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String getOrderRemarks() {
      return orderRemarks;
   }

   public void setOrderRemarks(String orderRemarks) {
      this.orderRemarks = orderRemarks;
   }

   public AddressInfoDTO getConsignor() {
      return consignor;
   }

   public void setConsignor(AddressInfoDTO consignor) {
      this.consignor = consignor;
   }

   public AddressInfoDTO getReceiver() {
      return receiver;
   }

   public void setReceiver(AddressInfoDTO receiver) {
      this.receiver = receiver;
   }
}

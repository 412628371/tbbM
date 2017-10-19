/**
 * 订单实体类;
 */
package com.xinguang.tubobo.impl.merchant.entity;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tubobo_order_detail")
@DynamicInsert
@DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderDetailEntity extends BaseMerchantOrderEntity {

    private static final long serialVersionUID = 1L;


    private String cancelReason;    //取消原因
    private String userId;//商家ID
    private String orderNo;//订单No
    private String orderRemark;


    //private Date expiredTime;//超时时间

    //private String senderId;//发货人id
    private String senderPhone;//发货人联系方式
    private String senderAddressProvince;//收货人详细地址
    private String senderAddressCity;//寄货人地址名称
    private String senderAddressDistrict;//寄货人地址名称
    private String senderAddressStreet;//寄货人地址名称
    private String senderAddressDetail;//寄货人地址名称
    private String senderAddressRoomNo;//寄货人地址名称
    private Double senderLongitude;
    private Double senderLatitude;
   // private String senderAdcode;


    private Double expiredMinute; //超时分钟数
    private Double expiredCompensation;//超时赔付expiredCompensation
    private String waitPickCancelType; //带接单状态下商家取消订单原因

    private Double cancelCompensation; //订单取消赔付
    private Double cancelFine;         //订单取消罚款
    private Double pickupDistance;      //取货距离

    private String unsettledReason;         //驿站订单 未妥投原因
    //private Date unsettledTime;             //驿站订单 商家确认未妥投时间


    private String merMessage; //未妥投确认消息

    private Double platformFee; 	 //平台抽佣
    private Double riderFee ;  		//付给骑手配送费

    private Double cancelSourceDeliveryFee; 	        //取消订单原发配送费
    private Double cancelSourceDeliverySubsidy ;      //取消订单原骑手补贴金额
    private String cancelSourceOrderNo ;  		    //取消订单原单号

    public Double getCancelSourceDeliveryFee() {
        return cancelSourceDeliveryFee;
    }

    public void setCancelSourceDeliveryFee(Double cancelSourceDeliveryFee) {
        this.cancelSourceDeliveryFee = cancelSourceDeliveryFee;
    }

    public Double getCancelSourceDeliverySubsidy() {
        return cancelSourceDeliverySubsidy;
    }

    public void setCancelSourceDeliverySubsidy(Double cancelSourceDeliverySubsidy) {
        this.cancelSourceDeliverySubsidy = cancelSourceDeliverySubsidy;
    }

    public String getCancelSourceOrderNo() {
        return cancelSourceOrderNo;
    }

    public void setCancelSourceOrderNo(String cancelSourceOrderNo) {
        this.cancelSourceOrderNo = cancelSourceOrderNo;
    }

    public Double getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(Double platformFee) {
        this.platformFee = platformFee;
    }

    public Double getRiderFee() {
        return riderFee;
    }

    public void setRiderFee(Double riderFee) {
        this.riderFee = riderFee;
    }
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }






    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderAddressProvince() {
        return senderAddressProvince;
    }

    public void setSenderAddressProvince(String senderAddressProvince) {
        this.senderAddressProvince = senderAddressProvince;
    }

    public String getSenderAddressCity() {
        return senderAddressCity;
    }

    public void setSenderAddressCity(String senderAddressCity) {
        this.senderAddressCity = senderAddressCity;
    }

    public String getSenderAddressDistrict() {
        return senderAddressDistrict;
    }

    public void setSenderAddressDistrict(String senderAddressDistrict) {
        this.senderAddressDistrict = senderAddressDistrict;
    }

    public String getSenderAddressStreet() {
        return senderAddressStreet;
    }

    public void setSenderAddressStreet(String senderAddressStreet) {
        this.senderAddressStreet = senderAddressStreet;
    }

    public String getSenderAddressDetail() {
        return senderAddressDetail;
    }

    public void setSenderAddressDetail(String senderAddressDetail) {
        this.senderAddressDetail = senderAddressDetail;
    }

    public String getSenderAddressRoomNo() {
        return senderAddressRoomNo;
    }

    public void setSenderAddressRoomNo(String senderAddressRoomNo) {
        this.senderAddressRoomNo = senderAddressRoomNo;
    }

    public Double getSenderLongitude() {
        return senderLongitude;
    }

    public void setSenderLongitude(Double senderLongitude) {
        this.senderLongitude = senderLongitude;
    }

    public Double getSenderLatitude() {
        return senderLatitude;
    }

    public void setSenderLatitude(Double senderLatitude) {
        this.senderLatitude = senderLatitude;
    }


    public Double getExpiredMinute() {
        return expiredMinute;
    }

    public void setExpiredMinute(Double expiredMinute) {
        this.expiredMinute = expiredMinute;
    }

    public Double getExpiredCompensation() {
        return expiredCompensation;
    }

    public void setExpiredCompensation(Double expiredCompensation) {
        this.expiredCompensation = expiredCompensation;
    }

    public String getWaitPickCancelType() {
        return waitPickCancelType;
    }

    public void setWaitPickCancelType(String waitPickCancelType) {
        this.waitPickCancelType = waitPickCancelType;
    }

    public Double getCancelCompensation() {
        return cancelCompensation;
    }

    public void setCancelCompensation(Double cancelCompensation) {
        this.cancelCompensation = cancelCompensation;
    }

    public Double getCancelFine() {
        return cancelFine;
    }

    public void setCancelFine(Double cancelFine) {
        this.cancelFine = cancelFine;
    }

    public Double getPickupDistance() {
        return pickupDistance;
    }

    public void setPickupDistance(Double pickupDistance) {
        this.pickupDistance = pickupDistance;
    }

    public String getUnsettledReason() {
        return unsettledReason;
    }

    public void setUnsettledReason(String unsettledReason) {
        this.unsettledReason = unsettledReason;
    }




    public String getMerMessage() {
        return merMessage;
    }

    public void setMerMessage(String merMessage) {
        this.merMessage = merMessage;
    }
}
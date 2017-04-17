package com.xinguang.tubobo.impl.rider.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tubobo_rider_task")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RiderTaskEntity extends BaseRiderEntity {

    private String taskNo;
    private Integer taskStatus;
    private Long payId;

    private String senderName;//发货人姓名
    private String senderPhone;//发货人联系方式
    private String senderAvatarUrl;//发货人联系方式
    private String senderAddressProvince;//发货人省
    private String senderAddressCity;//发货人市
    private String senderAddressDistrict;//发货人区
    private String senderAddressStreet;//发货人街道
    private String senderAddressDetail;//发货人市详细地址
    private Double senderLongitude;
    private Double senderLatitude;

    private String riderId;
    private Integer tipFee;//小费 单位：分
    private Integer deliveryFee;//配送费 单位：分
    private Integer payAmount;//收入金额

    private String receiverName;//收货人姓名
    private String receiverPhone;//收货人联系方式
    private String receiverAddressProvince;//收货人省
    private String receiverAddressCity;//收货人市
    private String receiverAddressDistrict;//收货人区
    private String receiverAddressStreet;//收货人街道
    private String receiverAddressDetail;//收货人市详细地址
    private Double receiverLongitude;
    private Double receiverLatitude;

    private Double deliveryDistance;//配送距离

    private Date pickTime;//取货时间
    private Date deliveryTime;//送达时间
    private Date acceptTime;//接单时间

    private Integer costTime;//配送用时 单位：秒

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
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

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public Integer getTipFee() {
        return tipFee;
    }

    public void setTipFee(Integer tipFee) {
        this.tipFee = tipFee;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Integer deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Integer getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Integer payAmount) {
        this.payAmount = payAmount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddressProvince() {
        return receiverAddressProvince;
    }

    public void setReceiverAddressProvince(String receiverAddressProvince) {
        this.receiverAddressProvince = receiverAddressProvince;
    }

    public String getReceiverAddressCity() {
        return receiverAddressCity;
    }

    public void setReceiverAddressCity(String receiverAddressCity) {
        this.receiverAddressCity = receiverAddressCity;
    }

    public String getReceiverAddressDistrict() {
        return receiverAddressDistrict;
    }

    public void setReceiverAddressDistrict(String receiverAddressDistrict) {
        this.receiverAddressDistrict = receiverAddressDistrict;
    }

    public String getReceiverAddressStreet() {
        return receiverAddressStreet;
    }

    public void setReceiverAddressStreet(String receiverAddressStreet) {
        this.receiverAddressStreet = receiverAddressStreet;
    }

    public String getReceiverAddressDetail() {
        return receiverAddressDetail;
    }

    public void setReceiverAddressDetail(String receiverAddressDetail) {
        this.receiverAddressDetail = receiverAddressDetail;
    }

    public Double getReceiverLongitude() {
        return receiverLongitude;
    }

    public void setReceiverLongitude(Double receiverLongitude) {
        this.receiverLongitude = receiverLongitude;
    }

    public Double getReceiverLatitude() {
        return receiverLatitude;
    }

    public void setReceiverLatitude(Double receiverLatitude) {
        this.receiverLatitude = receiverLatitude;
    }

    public Double getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(Double deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }
}

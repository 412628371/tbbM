/**
 * 订单实体类;
 */
package com.xinguang.tubobo.impl.merchant.entity;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/*@Entity
@Table(name = "tubobo_merchant_order")
@DynamicInsert @DynamicUpdate*/
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantOrderEntity extends BaseMerchantEntity {

	private static final long serialVersionUID = 1L;

	private String platformCode;
	private String originOrderViewId;
	private String originOrderId;

	private String cancelReason;
	private Long payId;
	private String userId;//商家ID
	private String orderNo;//订单No
	private String orderStatus;//订单状态
	private String orderRemark;

	private Double payAmount;//支付总金额
	private Double deliveryFee;//配送费
	private Double tipFee;//小费
	private String payStatus;//支付状态
	private String payMethod;//支付方式
	private Date payTime;//付款时间

	private Date orderTime;//下单时间
	private Date cancelTime;//取消时间
	private Date closeTime;//关闭时间
	private Date expiredTime;//超时时间
	private Date grabOrderTime;//接单时间
	private Date grabItemTime;//取货时间
	private Date finishOrderTime;//送达时间
	private Date expectFinishTime;//预计送达时间

	private String senderId;//发货人id
	private String senderName;//发货人名称
	private String senderPhone;//发货人联系方式
	private String senderAddressProvince;//收货人详细地址
	private String senderAddressCity;//寄货人地址名称
	private String senderAddressDistrict;//寄货人地址名称
	private String senderAddressStreet;//寄货人地址名称
	private String senderAddressDetail;//寄货人地址名称
	private String senderAddressRoomNo;//寄货人地址名称
	private Double senderLongitude;
	private Double senderLatitude;
	private String senderAdcode;

	private String receiverId;//收货人id
	private String receiverName;//收货人姓名
	private String receiverPhone;//收货人联系方式
	private String receiverAddressProvince;//收货人详细地址
	private String receiverAddressCity;//收货人地址名称
	private String receiverAddressDistrict;//收货人地址名称
	private String receiverAddressStreet;//收货人地址名称
	private String receiverAddressDetail;//收货人地址名称
	private String receiverAddressRoomNo;//收货人地址名称

	private Double receiverLongitude;
	private Double receiverLatitude;
	private Double deliveryDistance;

//	private Double dispatchRadius;//分派半径，单位：米

	private String riderId;
	private String riderName;
	private String riderPhone;
	private String riderCarNo;
	private String riderCarType;
	private String orderType;//订单类型，大件或小件
	private String carType;//车辆类型
	private String carTypeName;//车辆类型名称

	private Date appointTime; //预约时间

	private String appointType; //配送类型

	private Boolean ratedFlag;

	private Double peekOverFee;  //高峰溢价费用

	private Double weatherOverFee; //天气溢价费用


	private Double expiredMinute; //超时分钟数
	private Double expiredCompensation;//超时赔付expiredCompensation
	private  String waitPickCancelType; //带接单状态下商家取消订单原因

	private Double cancelCompensation; //订单取消赔付
	private Double cancelFine;         //订单取消罚款
	private Double pickupDistance;	  //取货距离

    private String unsettledStatus;         //驿站订单 未妥投状态  默认""  0:未妥投处理中 1：未妥投已处理
    private String unsettledReason;         //驿站订单 未妥投原因
    private Date unsettledTime;             //驿站订单 商家确认未妥投时间
	private Boolean  shortMessage;			//订单是否发送短信通知收件人

	private Long providerId;//所属服务商ID
	private String providerName;//所属服务商名称
	private String merMessage; //未妥投确认消息
	private String orderFeature;//订单特征值 RIDER_CANCEL_RESEND  骑手取消重发单

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


	public String getOrderFeature() {
		return orderFeature;
	}

	public void setOrderFeature(String orderFeature) {
		this.orderFeature = orderFeature;
	}

	public Boolean getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(Boolean shortMessage) {
		this.shortMessage = shortMessage;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getMerMessage() {
		return merMessage;
	}

	public void setMerMessage(String merMessage) {
		this.merMessage = merMessage;
	}

	public Double getPickupDistance() {
		return pickupDistance;
	}

	public void setPickupDistance(Double pickupDistance) {
		this.pickupDistance = pickupDistance;
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

	public Double getExpiredMinute() {
		return expiredMinute;
	}

	public void setExpiredMinute(Double expiredMinute) {
		this.expiredMinute = expiredMinute;
	}

	public Double getPeekOverFee() {
		if (peekOverFee == null){
			return 0.0;
		}
		return peekOverFee;
	}

	public void setPeekOverFee(Double peekOverFee) {
		this.peekOverFee = peekOverFee;
	}

	public Double getWeatherOverFee() {
		if (weatherOverFee == null){
			return 0.0;
		}
		return weatherOverFee;
	}

	public void setWeatherOverFee(Double weatherOverFee) {
		this.weatherOverFee = weatherOverFee;
	}

	public String getAppointType() {
		return appointType;
	}

	public void setAppointType(String appointType) {
		this.appointType = appointType;
	}

	public Date getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
	}

	public String getReceiverAddressRoomNo() {
		return receiverAddressRoomNo;
	}

	public void setReceiverAddressRoomNo(String receiverAddressRoomNo) {
		this.receiverAddressRoomNo = receiverAddressRoomNo;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Double getDeliveryFee() {
		if (deliveryFee == null){
			return 0.0;
		}
		return deliveryFee;
	}

	public void setDeliveryFee(Double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public Double getTipFee() {
		if (tipFee == null){
			return 0.0;
		}
		return tipFee;
	}

	public void setTipFee(Double tipFee) {
		this.tipFee = tipFee;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public Date getGrabOrderTime() {
		return grabOrderTime;
	}

	public void setGrabOrderTime(Date grabOrderTime) {
		this.grabOrderTime = grabOrderTime;
	}

	public Date getGrabItemTime() {
		return grabItemTime;
	}

	public void setGrabItemTime(Date grabItemTime) {
		this.grabItemTime = grabItemTime;
	}

	public Date getFinishOrderTime() {
		return finishOrderTime;
	}

	public void setFinishOrderTime(Date finishOrderTime) {
		this.finishOrderTime = finishOrderTime;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = ConvertUtil.handleNullString(senderName);
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
		this.senderAddressProvince = ConvertUtil.handleNullString(senderAddressProvince);
	}

	public String getSenderAddressCity() {
		return senderAddressCity;
	}

	public void setSenderAddressCity(String senderAddressCity) {
		this.senderAddressCity = ConvertUtil.handleNullString(senderAddressCity);
	}

	public String getSenderAddressDistrict() {
		return senderAddressDistrict;
	}

	public void setSenderAddressDistrict(String senderAddressDistrict) {
		this.senderAddressDistrict = ConvertUtil.handleNullString(senderAddressDistrict);
	}

	public String getSenderAddressStreet() {
		return senderAddressStreet;
	}

	public void setSenderAddressStreet(String senderAddressStreet) {
		this.senderAddressStreet = ConvertUtil.handleNullString(senderAddressStreet);
	}

	public String getSenderAddressDetail() {
		return senderAddressDetail;
	}

	public void setSenderAddressDetail(String senderAddressDetail) {
		this.senderAddressDetail = ConvertUtil.handleNullString(senderAddressDetail);
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

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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
		this.receiverAddressDistrict = ConvertUtil.handleNullString(receiverAddressDistrict);
	}

	public String getReceiverAddressStreet() {
		return receiverAddressStreet;
	}

	public void setReceiverAddressStreet(String receiverAddressStreet) {
		this.receiverAddressStreet = ConvertUtil.handleNullString(receiverAddressStreet);
	}

	public String getReceiverAddressDetail() {
		return receiverAddressDetail;
	}

	public void setReceiverAddressDetail(String receiverAddressDetail) {
		this.receiverAddressDetail = ConvertUtil.handleNullString(receiverAddressDetail);
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

	public String getRiderId() {
		return riderId;
	}

	public void setRiderId(String riderId) {
		this.riderId = ConvertUtil.handleNullString(riderId);
	}

	public String getRiderName() {
		return riderName;
	}

	public void setRiderName(String riderName) {
		this.riderName = ConvertUtil.handleNullString(riderName);
	}

	public String getRiderPhone() {
		return riderPhone;
	}

	public void setRiderPhone(String riderPhone) {
		this.riderPhone = ConvertUtil.handleNullString(riderPhone);
	}

	public Double getDeliveryDistance() {
		return deliveryDistance;
	}

	public void setDeliveryDistance(Double deliveryDistance) {
		this.deliveryDistance = deliveryDistance;
	}

	public String getSenderAddressRoomNo() {
		return senderAddressRoomNo;
	}

	public void setSenderAddressRoomNo(String senderAddressRoomNo) {
		this.senderAddressRoomNo = senderAddressRoomNo;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Date getExpectFinishTime() {
		return expectFinishTime;
	}

	public void setExpectFinishTime(Date expectFinishTime) {
		this.expectFinishTime = expectFinishTime;
	}

	public Boolean getRatedFlag() {
		return ratedFlag;
	}

	public void setRatedFlag(Boolean ratedFlag) {
		this.ratedFlag = ratedFlag;
	}

	public String getRiderCarNo() {
		return riderCarNo;
	}

	public void setRiderCarNo(String riderCarNo) {
		this.riderCarNo = riderCarNo;
	}

	public String getRiderCarType() {
		return riderCarType;
	}

	public void setRiderCarType(String riderCarType) {
		this.riderCarType = riderCarType;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarTypeName() {
		return carTypeName;
	}

	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getOriginOrderViewId() {
		return originOrderViewId;
	}

	public void setOriginOrderViewId(String originOrderViewId) {
		this.originOrderViewId = originOrderViewId;
	}

	public String getOriginOrderId() {
		return originOrderId;
	}

	public void setOriginOrderId(String originOrderId) {
		this.originOrderId = originOrderId;
	}

	public String getSenderAdcode() {
		return senderAdcode;
	}

	public void setSenderAdcode(String senderAdcode) {
		this.senderAdcode = senderAdcode;
	}

    public String getUnsettledStatus() {
        return unsettledStatus;
    }

    public void setUnsettledStatus(String unsettledStatus) {
        this.unsettledStatus = unsettledStatus;
    }

    public String getUnsettledReason() {
        return unsettledReason;
    }

    public void setUnsettledReason(String unsettledReason) {
        this.unsettledReason = unsettledReason;
    }

    public Date getUnsettledTime() {
        return unsettledTime;
    }

    public void setUnsettledTime(Date unsettledTime) {
        this.unsettledTime = unsettledTime;
    }
}
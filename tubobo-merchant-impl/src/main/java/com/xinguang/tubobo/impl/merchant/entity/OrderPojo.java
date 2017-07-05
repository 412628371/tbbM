package com.xinguang.tubobo.impl.merchant.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/4.
 */
public class OrderPojo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String platformCode;
    private String userId;//商家ID
    private String orderNo;//订单No
    private String orderType;//订单类型，大件或小件

    private Boolean ratedFlag;

    private String orderStatus;//订单状态
    private Double payAmount;//支付总金额
    private Double deliveryFee;//配送费

    private Double tipFee;//小费
    private Date orderTime;//下单时间
    private Date grabOrderTime;//接单时间
    private Date grabItemTime;//取货时间

    private Date finishOrderTime;//送达时间
    private String receiverName;//收货人姓名
    private String receiverPhone;//收货人联系方式
    private String receiverAddressProvince;//收货人详细地址
    private String receiverAddressCity;//收货人地址名称
    private String receiverAddressDistrict;//收货人地址名称
    private String receiverAddressStreet;//收货人地址名称
    private String receiverAddressDetail;//收货人地址名称

    private String receiverAddressRoomNo;//收货人地址名称


   public OrderPojo(){

   }

    public OrderPojo(String id,String platformCode, String userId, String orderNo, String orderType, Boolean ratedFlag, String orderStatus, Double payAmount, Double deliveryFee, Double tipFee, Date orderTime, Date grabOrderTime, Date grabItemTime, Date finishOrderTime, String receiverName, String receiverPhone, String receiverAddressProvince, String receiverAddressCity, String receiverAddressDistrict, String receiverAddressStreet, String receiverAddressDetail, String receiverAddressRoomNo) {
       this.id = id;
       this.platformCode = platformCode;
        this.userId = userId;
        this.orderNo = orderNo;
        this.orderType = orderType;
        this.ratedFlag = ratedFlag;
        this.orderStatus = orderStatus;
        this.payAmount = payAmount;
        this.deliveryFee = deliveryFee;
        this.tipFee = tipFee;
        this.orderTime = orderTime;
        this.grabOrderTime = grabOrderTime;
        this.grabItemTime = grabItemTime;
        this.finishOrderTime = finishOrderTime;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddressProvince = receiverAddressProvince;
        this.receiverAddressCity = receiverAddressCity;
        this.receiverAddressDistrict = receiverAddressDistrict;
        this.receiverAddressStreet = receiverAddressStreet;
        this.receiverAddressDetail = receiverAddressDetail;
        this.receiverAddressRoomNo = receiverAddressRoomNo;
    }
}

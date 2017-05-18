package com.xinguang.tubobo.merchant.web.request.rate;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;


/**
 * Created by Administrator on 2017/5/17.
 */
public class ReqComment {

    @Range(min = 1,max = 5,message = "配送时效需要1-5星")
    private Integer deliveryScore;
    @Range(min = 1,max = 5,message = "服务评价需要1-5星")
    private Integer serviceScore;
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(Integer deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public Integer getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "ReqComment{" +
                "deliveryScore=" + deliveryScore +
                ", serviceScore=" + serviceScore +
                ", orderNo='" + orderNo + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

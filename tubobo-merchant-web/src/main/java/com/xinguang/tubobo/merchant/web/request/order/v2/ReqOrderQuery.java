package com.xinguang.tubobo.merchant.web.request.order.v2;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 查询收货人地址自动补全
 * 请求类
 */
public class ReqOrderQuery {
    @NotBlank(message = "手机号关键字不能为空")
    private String keyword;

    private String orderType;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "ReqOrderQuery{" +
                "keyword='" + keyword + '\'' +
                ", orderType='" + orderType + '\'' +
                '}';
    }
}

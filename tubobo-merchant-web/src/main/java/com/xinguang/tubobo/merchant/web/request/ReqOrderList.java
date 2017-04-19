package com.xinguang.tubobo.merchant.web.request;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 * Created by Administrator on 2017/4/14.
 */
public class ReqOrderList {
//    @NotBlank(message = "订单状态不能为空")
    private String orderStatus;
    @Range(min = 1,max = 20 ,message = "pageSize 取值为1至20")
    private Integer pageSize;
    @Min(value = 1 ,message = "pageNo 最小为1")
    private Integer pageNo;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}

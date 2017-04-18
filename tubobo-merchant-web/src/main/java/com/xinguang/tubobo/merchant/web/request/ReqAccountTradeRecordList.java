package com.xinguang.tubobo.merchant.web.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ReqAccountTradeRecordList {

    @Min(value = 1 ,message = "pageNo 最小为1")
    private int pageNo;

    @Min(value = 10 ,message = "pageSize 取值为10-20")
    @Max(value = 20 ,message = "pageSize 取值为10-20")
    private int pageSize;

//    RECHARGE("200"),
//    WITHDRAW("210"),
//    PAY("220"),
//    RECIEVE("221"),
//    FINE("230");
    private String type;


    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

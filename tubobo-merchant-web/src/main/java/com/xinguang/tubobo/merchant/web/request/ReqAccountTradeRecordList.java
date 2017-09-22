package com.xinguang.tubobo.merchant.web.request;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

public class ReqAccountTradeRecordList {

    @Min(value = 1 ,message = "pageNo 最小为1")
    private int pageNo;
    @Range(min = 1,max = 20 ,message = "pageSize 取值为1至20")
    private int pageSize;
    //RECHARGE("100", "充值"), WITHDRAW("110", "提现"),PAY("120", "付款"), RECIEVE("121", "收款"),
    // FINE("130", "罚款"),GIFT("140", "充值满送"), SUBSIDY("150", "补贴") 搜索全部为“1” 默认使用‘0’加载近三个月的
    private String type;
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

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

    @Override
    public String toString() {
        return "ReqAccountTradeRecordList{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", type='" + type + '\'' +
                '}';
    }

    public enum QueryTypeEnum
    {
        INIT("0","初始"),
        ALL("1","全类型");

        private String value;
        private String name;
        QueryTypeEnum(String value, String name){
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

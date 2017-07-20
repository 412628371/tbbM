package com.xinguang.tubobo.merchant.web.request.third;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/1.
 */
public class ReqQueryThirdOrderList implements Serializable {
//    @NotBlank(message = "分页编号不能为空")
    @Min(value = 1 ,message = "pageNo 最小为1")
    private Integer pageNo;
//    @NotBlank(message = "分页大小不能为空")
    @Range(min = 1,max = 20 ,message = "pageSize 取值为1至20")
    private Integer pageSize;
    @NotBlank(message = "平台编号不能为空")
    private String platformCode;
    private String keyword;

    private String queryType;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}

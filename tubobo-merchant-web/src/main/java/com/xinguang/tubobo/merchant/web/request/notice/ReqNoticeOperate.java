package com.xinguang.tubobo.merchant.web.request.notice;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuqinghua on 2017/7/18.
 */
public class ReqNoticeOperate implements Serializable {
    private List<Long> ids;
    @NotBlank(message = "操作类型不能为空")
    private String operate;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}

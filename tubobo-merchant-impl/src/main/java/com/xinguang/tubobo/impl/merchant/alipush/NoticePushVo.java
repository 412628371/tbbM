package com.xinguang.tubobo.impl.merchant.alipush;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NoticePushVo implements Serializable{
    private String type;
    private NoticeParamVo params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NoticeParamVo getParams() {
        return params;
    }

    public void setParams(NoticeParamVo params) {
        this.params = params;
    }
}

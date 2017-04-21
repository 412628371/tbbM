package com.xinguang.tubobo.merchant.web.response;

/**
 * Created by Administrator on 2017/4/19.
 */
public class RespAccountRecharge {
    private String payInfo;

    public RespAccountRecharge(){
    }
    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    @Override
    public String toString() {
        return "RespAccountRecharge{" +
                "payInfo='" + payInfo + '\'' +
                '}';
    }
}

package com.xinguang.tubobo.merchant.web.request.shop;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/4/14.
 */
public class ShopHeadImageUploadRequest {
    @NotBlank(message = "picUrl 不能为空")
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "ShopHeadImageUploadRequest{" +
                "avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}

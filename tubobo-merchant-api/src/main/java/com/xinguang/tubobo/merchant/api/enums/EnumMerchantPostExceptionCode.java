package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by xuqinghua on 2017/9/1.
 */
public enum EnumMerchantPostExceptionCode {
    SUCCESS(0),
    FAIL(1),
    SHOP_NOT_EXIST(2),
    SHOP_NOT_AUTHED(3),
    SHOP_ALREADY_BOUND(4),
    SHOP_ALREADY_UNBOUND(5),
    SHOP_NO_PERMISSION(6);
    private int code;

    private EnumMerchantPostExceptionCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

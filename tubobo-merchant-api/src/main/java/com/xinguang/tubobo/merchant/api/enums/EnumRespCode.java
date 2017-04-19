package com.xinguang.tubobo.merchant.api.enums;

public enum EnumRespCode {

	SUCCESS("0", "操作成功"),
	FAIL("1", "操作失败"),
	PARAMS_ERROR("2", "参数错误"),
	INFO_ERROR("3", "信息错误"),
	BUSY("4", "系统繁忙"),




	MERCHANT_APPLY_REPEAT("10100", "商家已认证成功，不允许重复认证"),
	MERCHANT_UN_IDENTIFIED("10101", "商家未认证通过，不允许操作"),
	MERCHANT_NOT_EXISTS("10102", "商家不存在"),
	MERCHANT_CANT_CANCEL("10200", "订单不存在或状态不允许取消"),
	MERCHANT_CANT_DELETE("10201", "订单不存在或状态不允许删除"),
	MERCHANT_ORDER_NOT_EXIST("10202", "订单不存在"),
	MERCHANT_RIDER_LOCATION_NOT_FOUND("10203", "未找到骑手位置"),
	MERCHANT_PUSH_SETTINGS_FAILURE("10001", "推送设置失败"),

	MERCHANT_DELIVERY_DISTANCE_TOO_FAR("10301", "配送距离太远"),


	ACCOUNT_CREATE_FAIL("30010", "账户信息生成失败"),
	ACCOUNT_INFO_NOT_EXIST("30020", "账户信息不存在"),
	ACCOUNT_WITHDRAW_APPLY_FAIL("30030", "提现申请操作失败"),
	ACCOUNT_WITHDRAW_COMFIRM_FAIL("30040", "提现申请确认操作失败"),
	ACCOUNT_PAY_FAIL("30050", "支付操作失败"),
	ACCOUNT_RECHARGE_FAIL("30060", "支付操作失败");




    private String value;
    private String desc;

    private EnumRespCode(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}

package com.xinguang.tubobo.merchant.api.enums;

public enum EnumRespCode {

	SUCCESS("0", "操作成功"),
	FAIL("1", "操作失败"),
	PARAMS_ERROR("2", "参数错误"),
	INFO_ERROR("3", "信息错误"),
	BUSY("4", "系统繁忙"),
	MERCHANT_APPLY_REPEAT("10100", "商家已认证成功，不允许重复认证"),
	MERCHANT_VERIFYING("10101", "商家信息正在审核中，请耐心等待"),
	MERCHANT_NOT_EXISTS("10102", "商家不存在"),
	MERCHANT_FROZEN("10103", "商家被冻结,请联系客服"),
	MERCHANT_ALREADY_APPLY_RIDER("10104", "您已申请骑手/司机，不能申请成为商家或货主"),
	MERCHANT_NEED_TOBE_CONSIGNOR_FIRST("10105", "成功认证成为货主后，才能完善店铺信息"),

	MERCHANT_CANT_CANCEL("10200", "订单状态不允许取消"),
	MERCHANT_CANT_DELETE("10201", "订单状态不允许删除"),
	MERCHANT_ORDER_NOT_EXIST("10202", "订单不存在"),
	MERCHANT_RIDER_LOCATION_NOT_FOUND("10203", "未找到骑手位置"),
	MERCHANT_REPEAT_PAY("10204", "订单已支付，不能重复支付"),
	MERCHANT_STATUS_CANT_OPERATE("10205", "未认证成功的用户不能操作"),
	MERCHANT_UNPAY_CNACELING("10206", "订单支付超时,取消中"),
	MERCHANT_UNGRAB_CANCELING("10207", "订单无人接单超时,取消中"),
	MERCHANT_CANT_PAY("10208", "订单状态不允许支付"),
	MERCHANT_NOT_WORK("10209", "骑手已经下班了，您的订单可能无法受理哦"),
	CONSIGNOR_NOT_WORK("10210", "兔司机已经下班了，您的订单可能无法受理哦"),

	MERCHANT_ORDER_RATED("10211", "订单已经评价"),
	MERCHANT_ORDER_NOT_FINISH("10212", "订单还未完成"),
	MERCHANT_ORDER_TYPE_NOT_SUPPORT("10213", "不支持的订单类型"),
	MERCHANT_CAR_TYPE_NOT_SUPPORT("10214", "不支持的车辆类型"),
	MERCHANT_APPOINTTIME_ERROR("10215", "商家预约时间超出预约时间范围"),
	MERCHANT_BDCODE_ERROR("10216","您已提交过邀请码"),

	MERCHANT_PUSH_SETTINGS_FAILURE("10001", "推送设置失败"),
	PASSWORD_DECRYPT_FAIL("10002", "密码解密失败"),

	MERCHANT_DELIVERY_DISTANCE_TOO_FAR("10301", "超过配送范围 请重新输入地址"),

	ACCOUNT_CREATE_FAIL("30010", "账户信息生成失败"),
	ACCOUNT_INFO_NOT_EXIST("30020", "账户信息不存在"),
	ACCOUNT_WITHDRAW_APPLY_FAIL("30030", "提现申请操作失败"),
	ACCOUNT_WITHDRAW_COMFIRM_FAIL("30040", "提现申请确认操作失败"),
	ACCOUNT_NOT_ENOUGH("30050", "余额不足，支付失败"),
	ACCOUNT_PWD_ERROR("30051", "支付密码不正确，您还有%s次机会"),
	ACCOUNT_PAY_FAIL("30052", "支付失败"),
	ACCOUNT_PWD_CANT_EQUAL("30053", "新旧密码不能相同"),
	ACCOUNT_PWD_RESET_CREDENTIAL_INVALID("30054", "重置密码，令牌已无效"),

	ACCOUNT_PWD_ERROR_TOO_MUCH("30055", "支付密码输错%s次，请找回密码"),
	ACCOUNT_RECHARGE_FAIL("30060", "充值操作失败"),

	PEEK_OVER_FEE_CHANGE("30070","高峰溢价费已发生变化,请重新提交订单"),
	WEATHER_OVER_FEE_CHANGE("30071","天气溢价费已发生变化,请重新提交订单"),
	OVER_FEE_CLOSE("30072","所有溢价均已结束"),
	ALL_OVER_FEE_CHANGE("30073","天气溢价高峰溢价均发生变化"),
	OVERFEE_VERSION_LOW("30074","您的版本过低，请先去更新版本再发单哦~");






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

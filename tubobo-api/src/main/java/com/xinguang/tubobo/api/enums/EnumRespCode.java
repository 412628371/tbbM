package com.xinguang.tubobo.api.enums;

public enum EnumRespCode {

	SUCCESS("0", "操作成功"),
	FAIL("1", "操作失败"),
	PARAMS_ERROR("2", "参数错误"),
	INFO_ERROR("3", "信息错误"),
	BUSY("4", "系统繁忙"),

	NO_USER_INFO("5", "用户不存在"),
	NO_BIND_PHONE("6", "未绑定手机"),
	LOGIN_EXPIRED("7", "登录过期"),
	TOKEN_VERIFY_ERROR("8", "token验证错误"),

	PHONE_OR_PWD_ERROR("10", "用户名或密码错误"),
	PHONE_NOT_EXIST("11", "手机号未绑定"),
	PHONE_EXIST("12", "手机号已被绑定"),
	PHONE_EXIST_NO_PWD("13", "手机号已被绑定,但无密码"),
	PHONE_FORMAT_ERROR("14", "手机号格式错误"),
	ORIGIN_PWD_ERROR("15", "原密码错误"),

	VALIDATE_CODE_NOT_FOUND("20", "请先发送验证码"),
	VALIDATE_CODE_EXPIRED("21", "验证码过期"),
	VALIDATE_CODE_ERROR("22", "验证码错误"),
	VALIDATE_CODE_DISABLED("23", "验证码失效，请重发"),

	NOT_RIDER("30", "未认证骑士"),
	WAYBILL_NOT_FOUND("31", "运单不存在"),
	STORE_INFO_ERROR("32", "门店信息错误"),
	ADDRESS_INFO_ERROR("33", "地址信息错误"),
	APPLY_ACCEPTED_CAN_NOT_DELETE("34", "寄件请求只有在未受理前可以删除"),
	APPLY_ACCEPTED_CAN_NOT_CHANGE("35", "预约收件方式不能改变"),
	SIGN_ERROR("36", "签收失败"),
	DELAY_NOT_ALLOW("37", "滞留件不允许此操作"),
	SMS_MODEL_ERROR("38", "短信模版错误"),
	WAYBILL_UPLOADTYPE_ERROR("39", "运单上传类型错误"),
	UPLOAD_WAYBILL_IS_NULL("40", "上传运单为空"),
	WAYBILLNO_IS_BLANK("41", "运单号为空"),
	WAYBILLNO_EXIST("42", "运单号已存在"),
	WAYBILLNO_DISABLED("43", "运单号不可用"),

	SMSTYPE_ERROR("50", "短信模版类型错误"),

	BIND_TUBOBOUSERINFO_ERROR("100", "绑定用户信息到兔波波帐号失败"),
	TUBOBOUSER_SMS_LIMIT("101", "今天已发送5次短信，请明天再试"),



	MERCHANT_APPLY_REPEAT("10100", "商家已存在，请勿重复操作"),
	MERCHANT_UN_IDENTIFIED("10101", "商家未认证，不允许操作"),
	MERCHANT_NOT_EXISTS("10102", "商家不存在"),
	MERCHANT_CANT_CANCEL("10200", "订单不存在或状态不允许取消"),
	MERCHANT_CANT_DELETE("10201", "订单不存在或状态不允许删除"),
	MERCHANT_ORDER_NOT_EXIST("10202", "订单不存在"),
	MERCHANT_RIDER_LOCATION_NOT_FOUND("10203", "未找到骑手位置"),
	MERCHANT_PUSH_SETTINGS_FAILURE("10001", "推送设置失败"),

	RIDER_APPLY_REPEAT("20010", "该用户已申请骑士，请勿重复操作"),
	RIDER_AUTHENTICATION_SUCCESS("20011", "骑手认证已成功，请勿重复操作"),
	RIDER_INFO_NOT_EXIST("20012", "骑手信息不存在"),
	RIDER_TASK_ORDERNO_NOT_EXIST("20020", "骑手任务单号不存在"),

	ACCOUNT_CREATE_FAIL("30010", "账户信息生成失败"),
	ACCOUNT_INFO_NOT_EXIST("30020", "账户信息不存在"),
	ACCOUNT_WITHDRAW_APPLY_FAIL("30030", "提现申请操作失败"),
	ACCOUNT_WITHDRAW_COMFIRM_FAIL("30040", "提现申请确认操作失败"),
	ACCOUNT_PAY_FAIL("30050", "支付操作失败");




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

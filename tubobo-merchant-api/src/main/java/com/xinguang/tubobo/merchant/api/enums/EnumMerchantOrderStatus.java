package com.xinguang.tubobo.merchant.api.enums;

/**
 * 订单状态
 */
public enum EnumMerchantOrderStatus {

	INIT("初始化,待支付", "INIT"),
	CANCEL("取消", "CANCEL"),
	CLOSE("关闭", "CLOSE"),
	CANCEL_PAY_OVERTIME("取消,超时未支付", "CANCEL_PAY_OVERTIME"),
//	WAITING_PAY("待支付", "WAITING_PAY"),
	WAITING_GRAB("待接单", "WAITING_GRAB"),// 商家付款后 变为待接单
	CANCEL_GRAB_OVERTIME("取消，超时未接单", "CANCEL_GRAB_OVERTIME"),
	WAITING_PICK("待取货", "WAITING_PICK"),
	DELIVERYING("配送中", "DELIVERYING"),
	FINISH("完成", "FINISH");

    private String name;
    private String value;

    private EnumMerchantOrderStatus(String name, String value){
        this.name = name;
        this.value = value;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
     
}

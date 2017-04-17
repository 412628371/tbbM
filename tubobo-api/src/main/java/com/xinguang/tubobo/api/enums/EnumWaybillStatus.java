package com.xinguang.tubobo.api.enums;

/**
 * 运单状态
 */
public enum EnumWaybillStatus{

	INIT("初始化", 0),
	IN("入库", 1),
	OUT("出库", 2),
	SIGNED("签收", 3),
	DELAY("滞留", 4),
	RETURNBACK("退回", 5);
	
    private String name;
    private int value;
     
    private EnumWaybillStatus(String name,int value){
        this.name = name;
        this.value = value;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
     
}

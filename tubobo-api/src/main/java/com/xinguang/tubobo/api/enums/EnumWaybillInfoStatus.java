package com.xinguang.tubobo.api.enums;

/**
 * 面单状态
 */
public enum EnumWaybillInfoStatus{

	//库存、领用、调拨、已使用、销售、报废（添加后默认状态为库存）
	INIT("库存", "init"),
	ALLOT("调拨", "allot"),
	USABLE("领用", "usable"),
	USED("已使用", "used"),
	SOLD("销售", "sold"),
	ABANDON("报废", "abandon"),
	DELETE("删除", "delete");
	
    private String name;
    private String value;
     
    private EnumWaybillInfoStatus(String name,String value){
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

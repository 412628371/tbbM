package com.xinguang.tubobo.api.enums;

public enum EnumExpressTrack{

	IN("入库", "已入库"),
	OUT("出库", "出库配送"),
	DELAY("滞留", "滞留"),
	SIGN("签收", "已签收"),
	RETURNBACK("退回", "已退回");
	
    private String name;
    private String value;
     
    private EnumExpressTrack(String name,String value){
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

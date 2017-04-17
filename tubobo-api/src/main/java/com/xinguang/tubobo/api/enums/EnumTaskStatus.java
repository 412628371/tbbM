package com.xinguang.tubobo.api.enums;

/**
 * 任务状态
 */
public enum EnumTaskStatus{

	INIT("初始化", "INIT"),
//	ACCEPTED("已受理", "ACCEPTED"),
	CANCELLED("取消", "CANCELLED"),
	CLOSED("关闭", "CLOSED"),
	ALLOTED("已分配", "ALLOTED"),
	REALLOT("重新分配", "REALLOT"),
	ASSIGNED("已指派", "ASSIGNED"),
	FINISH("完成", "FINISH");
	
    private String name;
    private String value;
     
    private EnumTaskStatus(String name,String value){
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

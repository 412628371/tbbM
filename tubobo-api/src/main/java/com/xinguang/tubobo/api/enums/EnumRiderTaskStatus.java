package com.xinguang.tubobo.api.enums;

public enum EnumRiderTaskStatus {
    INIT(0,"初始未分配"),
    WAITING_ACCEPTED(1,"待接单"),
    ACCEPTED(2,"已接单待取货"),
    DELIVERYING(3,"配送中"),
    FINISHED(4,"任务完成"),
    SPONSOR_CANCEL(5,"任务被发起者取消"),
    ACCEPTOR_CANCEL(6,"任务被骑士取消"),
    TIMEOUT(7,"超时");



    EnumRiderTaskStatus(int value,String name){
        this.value = value;
        this.name = name;
    }
    private int value;
    private String name;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.xinguang.tubobo.merchant.api.dto;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/30.
 */
public class TaskOperatorDTO implements Serializable {
    private EnumTaskOperatorType taskOperatorType;
    private String taskNo;
    private Date operateTime;

    public TaskOperatorDTO(EnumTaskOperatorType taskOperatorType,String taskNo,Date operateTime){
        this.taskOperatorType = taskOperatorType;
        this.taskNo = taskNo;
        this.operateTime = operateTime;
    }
    public enum EnumTaskOperatorType {

        PICK("已取货", "INIT"),
        EXPIRED("过期未抢单", "APPLY"),
        FINISH("送达", "FROZEN")
        ;

        EnumTaskOperatorType(String name, String value){
            this.name = name;
            this.value = value;
        }
        private String name;
        private String value;

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

    public EnumTaskOperatorType getTaskOperatorType() {
        return taskOperatorType;
    }

    public void setTaskOperatorType(EnumTaskOperatorType taskOperatorType) {
        this.taskOperatorType = taskOperatorType;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}

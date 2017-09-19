package com.xinguang.tubobo.merchant.api;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/2.
 */
public class TbbMerchantResponse<D> implements Serializable{
    private boolean succeeded;
    private String errorCode;
    private String message;
    private D data;

    /**
     * succeeded
     * @param data
     */
    public TbbMerchantResponse(D data) {
        this.data = data;
        this.succeeded = true;
    }

    /**
     * failed
     * @param errorCode
     */
    public TbbMerchantResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.succeeded = false;
    }
    public TbbMerchantResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode.getCode();
        if (null == message || message.length() == 0) {
            this.message = errorCode.getMessage();
        } else {
            this.message = message;
        }
        this.succeeded = false;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public D getData() {
        return data;
    }




    public enum ErrorCode {
        ERROR_SYSTEM_ERROR("T0001", "系统错误"),
        ERROR_PARAM_ILLEGAL("T0002", "参数不合法"),
        ERROR_TASK_NOT_EXIST("T0010", "任务单不存在"),
        ERROR_TASK_CANT_CANCEL("T0012", "任务不能取消"),
        ERROR_TASK_CANT_OPERATE("T0013", "任务状态不允许操作");

        ErrorCode(String code, String message) {
            this.code = code;
            this.message = message;
        }
        private String code;
        private String message;

        public String getCode() {
            return code;
        }
        public String getMessage() {
            return message;
        }
    }

    @Override
    public String toString() {
        return "TbbTaskResponse{" +
                "succeeded=" + succeeded +
                ", errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data==null?"":data.toString() +
                '}';
    }
}

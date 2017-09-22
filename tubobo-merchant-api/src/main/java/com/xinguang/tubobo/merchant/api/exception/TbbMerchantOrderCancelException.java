package com.xinguang.tubobo.merchant.api.exception;

import com.xinguang.tubobo.merchant.api.TbbMerchantResponse;

/**
 * 任务不能取消的异常.
 */
public class TbbMerchantOrderCancelException extends TbbMerchantBaseException {
    private String taskNo;
    public TbbMerchantOrderCancelException(String taskNo){
        this.taskNo = taskNo;
    }
    @Override
    public TbbMerchantResponse.ErrorCode getErrorCode() {
        return TbbMerchantResponse.ErrorCode.ERROR_TASK_CANT_CANCEL;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    @Override
    public String toString() {
        return "TbbTaskCantCancelException{" +
                "taskNo='" + taskNo + '\'' +
                '}';
    }
}

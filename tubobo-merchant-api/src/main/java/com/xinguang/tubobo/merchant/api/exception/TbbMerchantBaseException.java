package com.xinguang.tubobo.merchant.api.exception;



/**
 * Created by yangxb on 2017/9/13.
 */

import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.TbbMerchantResponse;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 基础异常类
 */
public abstract class TbbMerchantBaseException extends Exception{
    public TbbMerchantBaseException() {
    }

    public TbbMerchantBaseException(Throwable cause) {
        super(cause);
    }

    public abstract TbbMerchantResponse.ErrorCode getErrorCode();

    public static <D> TbbMerchantResponse<D> genErrorCodeResponse(Exception e) {
        if (e instanceof TbbMerchantBaseException) {
            return new TbbMerchantResponse<D>(((TbbMerchantBaseException) e).getErrorCode());
        } else if (e instanceof ConstraintViolationException) {
            return new TbbMerchantResponse<D>(TbbMerchantResponse.ErrorCode.ERROR_PARAM_ILLEGAL, genConstraintViolationDetail((ConstraintViolationException) e));
        } else if (e instanceof MerchantClientException){
            return new TbbMerchantResponse<D>(TbbMerchantResponse.ErrorCode.ERROR_TASK, genMerchantClientExceptionDetail((MerchantClientException) e));
        }
        else {
            return new TbbMerchantResponse<D>(TbbMerchantResponse.ErrorCode.ERROR_SYSTEM_ERROR);
        }
    }

    public static String genMerchantClientExceptionDetail(MerchantClientException e) {
        StringBuilder sb = new StringBuilder("失败原因:");
        sb.append("code:").append( e.getCode()).append(" message:").append(e.getErrorMsg());
        return sb.toString();
    }

    public static String genConstraintViolationDetail(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (ConstraintViolation<?> constraintViolation : set) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append("{").append(constraintViolation.getPropertyPath()).append(":").append(constraintViolation.getMessage()).append("}");

        }
        sb.append("]");
        return sb.toString();
    }

}

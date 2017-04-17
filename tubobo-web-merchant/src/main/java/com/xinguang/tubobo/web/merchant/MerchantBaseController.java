package com.xinguang.tubobo.web.merchant;

import com.google.common.collect.Lists;
import com.hzmux.hzcms.common.beanvalidator.BeanValidators;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.baseDTO.ClientResp;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.usercentergate.api.TokenServiceInterface;
import com.xinguang.usercentergate.response.XgUserResCheckToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/14.
 */
public abstract class MerchantBaseController <P, R>{
    protected Logger logger = LoggerFactory.getLogger(MerchantBaseController.class);

    @Autowired
    protected Validator validator;
    @Autowired
    private TokenServiceInterface tokenServiceInterface;

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ClientResp<R> request(@RequestBody(required = false) P req,
                                 @RequestHeader(MerchantConstants.TOKEN_HEADER) String token) {

        // 验证请求参数
        try {
            if (null != req) {
                BeanValidators.validateWithException(validator, req);
            }
        }catch (ConstraintViolationException e){
            List<String> cons = extractMessage(e.getConstraintViolations());
            return new ClientResp(EnumRespCode.PARAMS_ERROR.getValue() , StringUtils.join(cons, ","));
        }
        String userId = "";

        // 验证登录
        if (needLogin()) {
            // 验证token
//            XgUserResCheckToken check = tokenServiceInterface.checkToken(token);
//            if (!check.isSuc()) {
//                return new ClientResp(check.getCode(),check.getErrorMsg());
//            }
//            userId = check.getData().getUserId();

            userId="888";
        }

        if (needIndetify()){
            //TODO 是否认证判断

        }
        // 处理业务
        try {
            R res = doService(userId,req);
            return new ClientResp(res);
        }catch (ClientException e){
            logger.error(e.getMessage(),e);
            return new ClientResp(e.getCode() , e.getErrorMsg());
        }

    }

    /**
     * 默认需要认证后才能操作，子类可以重写此方法以关闭登录验证
     *
     * @return
     */
    protected boolean needIndetify() {
        return true;
    }
    /**
     * 默认验证登录，子类可以重写此方法以关闭登录验证
     *
     * @return
     */
    protected boolean needLogin() {
        return true;
    }

    /**
     * 业务处理
     * @param req
     * @return 正常处理完成之后正常返回
     */
    protected abstract R doService(String userId,P req) throws ClientException;



    /**
     * 辅助方法, 转换Set<ConstraintViolation>为List<message>
     */
    private static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
        List<String> errorMessages = Lists.newArrayList();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getPropertyPath() +":"+ violation.getMessage());
        }
        return errorMessages;
    }
}

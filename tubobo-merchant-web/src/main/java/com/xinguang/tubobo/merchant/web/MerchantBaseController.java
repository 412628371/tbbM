package com.xinguang.tubobo.merchant.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hzmux.hzcms.common.beanvalidator.BeanValidators;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumIdentifyType;
import com.xinguang.tubobo.merchant.web.response.ClientResp;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
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
    @Autowired
    MerchantInfoService merchantInfoService;

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ClientResp<R> request(@RequestBody(required = false) P req,
                                 @RequestHeader(MerchantConstants.TOKEN_HEADER) String token,@RequestHeader(MerchantConstants.REQUESTID_HEADER) String requestID) {
        logger.info("controller accept request -reqID:{}",requestID);
        // 验证请求参数
        try {
            if (null != req) {
                BeanValidators.validateWithException(validator, req);
            }
        }catch (ConstraintViolationException e){
            List<String> cons = extractMessage(e.getConstraintViolations());
            return new ClientResp(EnumRespCode.PARAMS_ERROR.getValue() , StringUtils.join(cons, ","));
        }
        String json = JSON.toJSONString(req);
        if (StringUtils.isContainIllegalChars(json)){
            return new ClientResp(EnumRespCode.PARAMS_ERROR.getValue() , "输入包含非法字符，请检查输入");
        }
//        String dealedJson = StringUtils.stripXSS(json);
//        req = (P) JSON.parseObject(dealedJson,req.getClass());
        String userId = "";
//        String userId = "30126";

        // 验证登录
        if (needLogin()) {
            // 验证token
            XgUserResCheckToken check = tokenServiceInterface.checkToken(token);
            if (!check.isSuc()) {
                return new ClientResp(check.getCode(),check.getErrorMsg());
            }
            userId = check.getData().getUserId();

        }

        if (needIdentify()){
            // 是否认证判断
            MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
            if (entity == null){
                return  new ClientResp(EnumRespCode.MERCHANT_NOT_EXISTS.getValue(),
                        EnumRespCode.MERCHANT_NOT_EXISTS.getDesc());
            }
            if (entity != null){
                String status = entity.getMerchantStatus();
                if (EnumIdentifyType.CONSIGNOR.getValue().equals(entity.getIdentifyType())){
                    status = entity.getConsignorStatus();
                }
                if (EnumAuthentication.FROZEN.getValue().equals(status)){
                    return  new ClientResp(EnumRespCode.MERCHANT_FROZEN.getValue(),
                            EnumRespCode.MERCHANT_FROZEN.getDesc());
                }else if (!EnumAuthentication.SUCCESS.getValue().equals(entity.getMerchantStatus())&&
                        !EnumAuthentication.SUCCESS.getValue().equals(entity.getConsignorStatus())){
                    return  new ClientResp(EnumRespCode.MERCHANT_STATUS_CANT_OPERATE.getValue(),
                            EnumRespCode.MERCHANT_STATUS_CANT_OPERATE.getDesc());
                }
            }
        }
        // 处理业务
        try {
            R res = doService(userId,req);
            return new ClientResp(res);
        }catch (MerchantClientException e){
            logger.error("MerchantClientException: errorCode:{},errorMsg:{}，userId:{}",e.getCode(),e.getErrorMsg(),userId);
            return new ClientResp(e.getCode() , e.getErrorMsg());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new ClientResp(EnumRespCode.BUSY.getValue() , EnumRespCode.BUSY.getDesc());
        }

    }

    /**
     * 默认需要认证后才能操作，子类可以重写此方法以关闭登录验证
     *
     * @return
     */
    protected boolean needIdentify() {
        return false;
    }
    /**
     * 默认验证登录s，子类可以重写此方法以关闭登录验证
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
    protected abstract R doService(String userId,P req) throws MerchantClientException;



    /**
     * 辅助方法, 转换Set<ConstraintViolation>为List<message>
     */
    private static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
        List<String> errorMessages = Lists.newArrayList();
        for (ConstraintViolation violation : constraintViolations) {
//            errorMessages.add(violation.getPropertyPath() +":"+ violation.getMessage());
            errorMessages.add( violation.getMessage());
        }
        return errorMessages;
    }
}

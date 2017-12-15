package com.xinguang.tubobo.merchant.web.controller.fund;

import com.hzmux.hzcms.common.utils.AliOss;
import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.xinguang.taskcenter.api.common.enums.TaskTypeEnum;
import com.xinguang.taskcenter.api.request.TaskCreateDTO;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayRequest;
import com.xinguang.tubobo.account.api.request.PayWithOutPwdRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.cache.RedisOp;
import com.xinguang.tubobo.impl.merchant.common.AESUtils;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumPayStatus;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import com.xinguang.tubobo.merchant.web.request.ReqAccountPay;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderPay;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;


/**
 * Created by Administrator on 2017/4/15.
 */
@Controller
@RequestMapping("/account/pay")
public class MerchantAccountPayController extends MerchantBaseController<ReqAccountPay,RespOrderPay> {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Resource
    Config config;
    @Autowired
    RedisOp redisOp;
    @Override
    protected RespOrderPay doService(String userId, ReqAccountPay req) throws MerchantClientException {
        logger.info("支付请求：userId:{}, request:{} , ",userId,req.toString());
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        MerchantOrderEntity orderEntity = merchantOrderManager.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (null == orderEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if(EnumPayStatus.PAID.getValue().equals(orderEntity.getPayStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_REPEAT_PAY);
        }
        if (EnumMerchantOrderStatus.CANCEL.getValue().equals(orderEntity.getOrderStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_PAY);
        }
        TbbAccountResponse<PayInfo> response;
        Double amountD=orderEntity.getPayAmount();
        //check订单短信开关,if开启--扣除短信费用,短信费用扣除发生在骑手取货时 生成额外短信流水
        if (orderEntity.getShortMessage()){
            amountD=  CalCulateUtil.sub(amountD,MerchantConstants.MESSAGE_FEE);
        }
        long amount = ConvertUtil.convertYuanToFen(amountD);
        long commission = ConvertUtil.convertYuanToFen(orderEntity.getPlatformFee());
        //设置了免密支付，并且支付金额不大于免密支付额度，可以免密支付
        if (infoEntity.getEnablePwdFree()&&
                orderEntity.getPayAmount() <= config.getNonConfidentialPaymentLimit()){
            PayWithOutPwdRequest payWithOutPwdRequest = new PayWithOutPwdRequest();
            payWithOutPwdRequest.setOrderId(orderEntity.getOrderNo());
            payWithOutPwdRequest.setAccountId(infoEntity.getAccountId());
            payWithOutPwdRequest.setAmount(amount);
            payWithOutPwdRequest.setCommission(commission);
            logger.info("免密支付请求：userId:{}, orderNo:{} ,amount:{}分 ",userId,req.getOrderNo(),payWithOutPwdRequest.getAmount());
            response = tbbAccountService.payWithOutPwd(payWithOutPwdRequest);
        }else {
            //计算支付密码错误次数
            redisOp.checkPwdErrorTimes(MerchantConstants.KEY_PWD_WRONG_TIMES_PAY,userId);
            String plainPwd = AESUtils.decrypt(req.getPayPassword());
            PayRequest payRequest = new PayRequest();
            payRequest.setOrderId(orderEntity.getOrderNo());
            payRequest.setAccountId(infoEntity.getAccountId());
            payRequest.setAmount(amount);
            payRequest.setPwd(plainPwd);
            payRequest.setCommission(commission);
            response = tbbAccountService.pay(payRequest);
        }
        if (response != null && response.isSucceeded()){
            redisOp.resetPwdErrorTimes(userId);
            long payId = response.getData().getId();
            orderEntity.setPayId(payId);
            TaskCreateDTO orderDTO = merchantOrderManager.buildMerchantOrderDTO(orderEntity,infoEntity);
            orderDTO.setPayId(payId);
            logger.info("pay  SUCCESS. orderNo:{}, accountId:{}, payId:{}, amount:{}",req.getOrderNo()
                    ,infoEntity.getAccountId(),response.getData().getId(),amount);
            merchantOrderManager.merchantPay(orderDTO,infoEntity.getUserId(),req.getOrderNo(),payId);
            RespOrderPay respOrderPay = new RespOrderPay();
            respOrderPay.setGrabExpiredStartTime(new Date());
            //驿站订单不做倒计时 不处理
            // 1.47之后驿站订单处理,配置同众包单
            respOrderPay.setGrabExpiredMilSeconds(config.getTaskGrabExpiredMilSeconds());
            return respOrderPay;
        }else {
            if (response == null){
                logger.error("pay  FAIL.orderNo:{}, accountId:{}}",
                        req.getOrderNo(),infoEntity.getAccountId());
            }else {
                if (response.getErrorCode().equals(TbbAccountResponse.ErrorCode.ERROR_AMOUNT_NOT_ENOUGH.getCode())){
                    logger.error("pay  FAIL.,余额不足。orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
                            req.getOrderNo(),infoEntity.getAccountId(),response.getErrorCode(),response.getMessage());
                    throw  new MerchantClientException(EnumRespCode.ACCOUNT_NOT_ENOUGH);
                }
                if (TbbAccountResponse.ErrorCode.ERROR_ACCOUNT_PAY_PWD_WRONG.getCode().
                        equals(response.getErrorCode())){
                    //支付密码错误。错误计数+1
                    logger.error("pay  FAIL.,支付密码错误。orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
                            req.getOrderNo(),infoEntity.getAccountId(),response.getErrorCode(),response.getMessage());
                    redisOp.pwdWrong(MerchantConstants.KEY_PWD_WRONG_TIMES_PAY,userId);
                }
                logger.error("pay  FAIL.,orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
                        req.getOrderNo(),infoEntity.getAccountId(),response.getErrorCode(),response.getMessage());
            }
            throw  new MerchantClientException(EnumRespCode.ACCOUNT_PAY_FAIL);
        }
    }




    @Override
    protected boolean needIdentify() {
        return true;
    }
}

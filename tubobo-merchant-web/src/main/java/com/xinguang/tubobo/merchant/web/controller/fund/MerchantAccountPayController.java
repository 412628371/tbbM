package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayWithOutPwdRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumPayStatus;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.merchant.web.request.ReqAccountPay;
import com.xinguang.tubobo.merchant.web.response.RespOrderPay;
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
    private MerchantOrderService merchantOrderService;
    @Resource
    Config config;
    @Override
    protected RespOrderPay doService(String userId, ReqAccountPay req) throws MerchantClientException {
        logger.info("支付请求：userId:{}, request:{} , ",userId,req.toString());
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        MerchantOrderEntity orderEntity = merchantOrderService.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (null == orderEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if(EnumPayStatus.PAID.getValue().equals(orderEntity.getPayStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_REPEAT_PAY);
        }
        if (EnumMerchantOrderStatus.CANCEL.getValue().equals(orderEntity.getPayStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_PAY);
        }
        PayWithOutPwdRequest payRequest = new PayWithOutPwdRequest();
        payRequest.setOrderId(orderEntity.getOrderNo());
        payRequest.setAccountId(infoEntity.getAccountId());
        payRequest.setAmount(ConvertUtil.convertYuanToFen(orderEntity.getPayAmount()));
        logger.info("支付请求：userId:{}, orderNo:{} ,amount:{}分 ",userId,req.getOrderNo(),payRequest.getAmount());
        TbbAccountResponse<PayInfo> response = tbbAccountService.payWithOutPwd(payRequest);
        if (response != null && response.isSucceeded()){
            long payId = response.getData().getId();
            orderEntity.setPayId(payId);
            MerchantOrderDTO orderDTO = buildMerchantOrderDTO(orderEntity,infoEntity);
            orderDTO.setPayId(payId);
            merchantOrderService.merchantPay(orderDTO,infoEntity.getUserId(),req.getOrderNo(),payId);
            logger.info("pay  SUCCESS. orderNo:{}, accountId:{}, payId:{}, amount:{}",req.getOrderNo()
                    ,infoEntity.getAccountId(),response.getData().getId(),payRequest.getAmount());
            RespOrderPay respOrderPay = new RespOrderPay();
            respOrderPay.setGrabExpiredStartTime(new Date());
            respOrderPay.setGrabExpiredMilSeconds(config.getPayExpiredMilSeconds());
            return respOrderPay;
        }else {
            if (response == null){
                logger.error("pay  FAIL.orderNo:{}, accountId:{}}",
                        req.getOrderNo(),infoEntity.getAccountId());
            }else {
                if (response.getErrorCode().equals(TbbAccountResponse.ErrorCode.ERROR_AMOUNT_NOT_ENOUGH)){
                    throw  new MerchantClientException(EnumRespCode.ACCOUNT_NOT_ENOUGH);
                }
                logger.error("pay  FAIL.,余额不足。orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
                        req.getOrderNo(),infoEntity.getAccountId(),response.getErrorCode(),response.getMessage());
            }
            throw  new MerchantClientException(EnumRespCode.ACCOUNT_PAY_FAIL);
        }
    }
    private MerchantOrderDTO buildMerchantOrderDTO(MerchantOrderEntity entity,MerchantInfoEntity infoEntity){
        MerchantOrderDTO merchantOrderDTO = new MerchantOrderDTO();
        BeanUtils.copyProperties(entity,merchantOrderDTO);
        if (entity.getPayAmount() != null){
            merchantOrderDTO.setPayAmount(ConvertUtil.convertYuanToFen(entity.getPayAmount()).intValue());
        }
        if (entity.getDeliveryFee() != null){
            merchantOrderDTO.setDeliveryFee(ConvertUtil.convertYuanToFen(entity.getDeliveryFee()).intValue());
        }
        if (entity.getTipFee() != null){
            merchantOrderDTO.setTipFee(ConvertUtil.convertYuanToFen(entity.getTipFee()).intValue());
        }
        merchantOrderDTO.setSenderAddressDetail(ConvertUtil.handleNullString(entity.getSenderAddressDetail())
                +ConvertUtil.handleNullString(entity.getSenderAddressRoomNo()));
        merchantOrderDTO.setSenderAvatar(ConvertUtil.handleNullString(infoEntity.getAvatarUrl()));
        return merchantOrderDTO;
    }
}

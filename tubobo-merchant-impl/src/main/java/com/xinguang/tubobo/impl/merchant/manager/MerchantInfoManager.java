package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.utils.AliOss;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.AccountInfoRequest;
import com.xinguang.tubobo.account.api.response.AccountInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.AESUtils;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumIdentifyType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.rider.api.RiderToAdminServiceInterface;
import com.xinguang.tubobo.rider.api.dto.RiderInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Administrator on 2017/5/26.
 */
@Service
public class MerchantInfoManager {
    Logger logger = LoggerFactory.getLogger(MerchantInfoManager.class);
    @Autowired
    MerchantInfoService merchantInfoService;
    @Autowired
    RiderToAdminServiceInterface riderToAdminServiceInterface;

    @Autowired
    TbbAccountService tbbAccountService;

    /**
     * 认证，包括货主认证和商家认证
     * @param userId
     * @param payPassword
     * @param identifyType
     * @param entity
     * @return
     * @throws MerchantClientException
     */
    public MerchantInfoEntity identify(String userId,String payPassword,String identifyType, MerchantInfoEntity entity) throws MerchantClientException {
        RiderInfoDTO riderInfoDTO = riderToAdminServiceInterface.findByUserId(userId);
        if (riderInfoDTO!= null){
            logger.error("店铺申请失败，该用户已经申请成为骑手。userId:{}",userId);
            throw new MerchantClientException(EnumRespCode.MERCHANT_ALREADY_APPLY_RIDER);
        }
        entity.setUserId(userId);
        entity.setIdCardFrontImageUrl(AliOss.subAliossUrl(entity.getIdCardFrontImageUrl()));
        entity.setIdCardBackImageUrl(AliOss.subAliossUrl(entity.getIdCardBackImageUrl()));
        entity.setShopImageUrl(AliOss.subAliossUrl(entity.getShopImageUrl()));
        entity.setIdentifyType(identifyType);
        entity.setHasSetPayPwd(true);
        entity.setEnablePwdFree(false);
        MerchantInfoEntity existEntity  = merchantInfoService.findByUserId(userId);

        if (null == existEntity){
            createFundAccount(userId,entity,payPassword,identifyType);
        }else {
            String status = existEntity.getMerchantStatus();
            if (EnumIdentifyType.CONSIGNOR.getValue().equals(identifyType)){
                status = existEntity.getConsignorStatus();
            }
            if ( EnumAuthentication.FROZEN.getValue().equals(status)){
                throw new MerchantClientException(EnumRespCode.MERCHANT_FROZEN);
            }else if (null != existEntity && EnumAuthentication.SUCCESS.getValue().equals(status)){
                throw new MerchantClientException(EnumRespCode.MERCHANT_APPLY_REPEAT);
            }else if (null != existEntity && EnumAuthentication.APPLY.getValue().equals(status)){
                throw new MerchantClientException(EnumRespCode.MERCHANT_VERIFYING);
            }else {
                entity.setAccountId(existEntity.getAccountId());
                entity.setId(existEntity.getId());
                entity.setCreateDate(existEntity.getCreateDate());
                boolean result = false;
                TbbAccountResponse<Boolean> response = tbbAccountService.resetPayPassword(entity.getAccountId(),AESUtils.decrypt(payPassword));
                if(response != null && response.isSucceeded() && response.getData()){
                    logger.info("重新认证，重置支付密码成功：userID：{}",userId);
                    //重新认证，
                    if (EnumIdentifyType.CONSIGNOR.getValue().equals(identifyType)){
                        entity.setConsignorStatus(EnumAuthentication.APPLY.getValue());
                        entity.setMerchantStatus(EnumAuthentication.INIT.getValue());
                    }else {
                        entity.setMerchantStatus(EnumAuthentication.APPLY.getValue());
                        entity.setConsignorStatus(EnumAuthentication.APPLY.getValue());
                    }
                    result = merchantInfoService.merchantUpdate(entity);
                }
                if (!result){
                    throw new MerchantClientException(EnumRespCode.FAIL);
                }
            }
        }

        MerchantInfoEntity entityResp = merchantInfoService.findByUserId(userId);
        AliOss.generateMerchantSignedUrl(entityResp);
        return entityResp;
    }

    /**
     * 完善店铺信息
     * @param userId
     * @param infoEntity
     * @return
     * @throws MerchantClientException
     */
    public MerchantInfoEntity shopComplete(String userId,MerchantInfoEntity infoEntity) throws MerchantClientException {
        MerchantInfoEntity existEntity  = merchantInfoService.findByUserId(userId);
        if (null == existEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        if (EnumIdentifyType.CONSIGNOR.getValue().equals(existEntity.getIdentifyType())&&
                !EnumAuthentication.SUCCESS.getValue().equals(existEntity.getConsignorStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NEED_TOBE_CONSIGNOR_FIRST);
        }
        if ( EnumAuthentication.FROZEN.getValue().equals(existEntity.getMerchantStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_FROZEN);
        }else if (EnumAuthentication.SUCCESS.getValue().equals(existEntity.getMerchantStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_APPLY_REPEAT);
        }else if ( EnumAuthentication.APPLY.getValue().equals(existEntity.getMerchantStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_VERIFYING);
        }else{
            existEntity.setAddressDetail(infoEntity.getAddressDetail());
            existEntity.setAddressProvince(infoEntity.getAddressProvince());
            existEntity.setAddressCity(infoEntity.getAddressCity());
            existEntity.setAddressDistrict(infoEntity.getAddressDistrict());
            existEntity.setAddressRoomNo(infoEntity.getAddressRoomNo());
            existEntity.setAddressStreet(infoEntity.getAddressStreet());
            existEntity.setShopImageUrl(infoEntity.getShopImageUrl());
            existEntity.setMerchantName(infoEntity.getMerchantName());
            existEntity.setLongitude(infoEntity.getLongitude());
            existEntity.setLatitude(infoEntity.getLatitude());

            existEntity.setIdentifyType(EnumIdentifyType.MERCHANT.getValue());
            existEntity.setMerchantStatus(EnumAuthentication.APPLY.getValue());
            boolean result = merchantInfoService.merchantUpdate(existEntity);
            if (!result){
                throw new MerchantClientException(EnumRespCode.FAIL);
            }
        }
        MerchantInfoEntity entityResp = merchantInfoService.findByUserId(userId);
        AliOss.generateMerchantSignedUrl(entityResp);
        return entityResp;
    }
    /**
     * 创建资金账户
     * @param userId
     * @param entity
     * @param payPassword
     * @param identifyType
     * @throws MerchantClientException
     */
    private void createFundAccount(String userId,MerchantInfoEntity entity,String payPassword,String identifyType) throws MerchantClientException {
        // 生成账户信息
        AccountInfoRequest request = new AccountInfoRequest();
        request.setName(entity.getRealName());
        request.setPhone(entity.getPhone());
        String password = AESUtils.decrypt(payPassword);
        if (StringUtils.isBlank(password)){
            throw new MerchantClientException(EnumRespCode.PASSWORD_DECRYPT_FAIL);
        }
        TbbAccountResponse<AccountInfo> response = tbbAccountService.createAccount(userId,password,request);
        if (response != null && response.isSucceeded() && null != response.getData()){
            logger.info("create shop info SUCCESS. request:{}, response:{}",response.getErrorCode(),response.getMessage(),request.toString(),response.getData().toString());
            Long accountId = response.getData().getId();
            entity.setAccountId(accountId);
            entity.setIdentifyType(identifyType);
            if (EnumIdentifyType.CONSIGNOR.getValue().equals(identifyType)){
                entity.setConsignorStatus(EnumAuthentication.APPLY.getValue());
                entity.setMerchantStatus(EnumAuthentication.INIT.getValue());
            }else {
                entity.setMerchantStatus(EnumAuthentication.APPLY.getValue());
                entity.setConsignorStatus(EnumAuthentication.APPLY.getValue());
            }
            merchantInfoService.merchantApply(userId,entity);

        }else{
            if (null != null){
                logger.error("create shop info FAIL. errorCode:{}, errorMsg:{}, request:{}, response:{}",response.getErrorCode(),response.getMessage(),request.toString(),response.toString());
            }
            throw new MerchantClientException(EnumRespCode.ACCOUNT_CREATE_FAIL) ;
        }
    }
}

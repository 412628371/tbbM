package com.xinguang.tubobo.web.merchant.controller.account;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.AccountInfoRequest;
import com.xinguang.tubobo.account.api.response.AccountInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.api.enums.EnumAuthentication;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ShopIdentifyRequest;
import com.xinguang.tubobo.web.merchant.response.ShopIdentifyResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/13.
 */
@Controller
@RequestMapping(value = "/shop/identify")
public class ShopIdentifyController extends MerchantBaseController<ShopIdentifyRequest,ShopIdentifyResponse>{
    @Autowired
    MerchantInfoService merchantInfoService;

    @Autowired
    TbbAccountService tbbAccountService;
    @Override
    protected ShopIdentifyResponse doService(String userId, ShopIdentifyRequest req) throws ClientException {
        // 生成账户信息
        AccountInfoRequest request = new AccountInfoRequest();
        request.setUserId(userId);
        request.setName(req.getRealName());
        request.setPhone(req.getPhone());
        MerchantInfoEntity entity = translateRequestToEntity(userId,req);
        TbbAccountResponse<AccountInfo> response = tbbAccountService.createAccount(request);
        if (response != null && response.isSucceeded() && null != response.getData()){
            logger.info("create account info SUCCESS. request:{}, response:{}",response.getErrorCode(),response.getMessage(),request.toString(),response.getData().toString());

            Long accountId = response.getData().getId();

            entity.setAccountId(accountId);
            EnumRespCode respCode =merchantInfoService.merchantApply(userId,entity);
            if (respCode.getValue() != EnumRespCode.SUCCESS.getValue()){
                throw new ClientException(respCode);
            }
            ShopIdentifyResponse resp = new ShopIdentifyResponse();
            resp.setIdentifyStatus(EnumAuthentication.APPLY.getValue());
            return resp;
        }else{
            logger.error("create account info FAIL. errorCode:{}, errorMsg:{}, request:{}, response:{}",response.getErrorCode(),response.getMessage(),request.toString(),response.toString());
            throw new ClientException(EnumRespCode.ACCOUNT_CREATE_FAIL) ;
        }

    }

    /**
     * 请求参数和实体参数转化
     * @param userId
     * @param shopIdentifyRequest
     * @return
     */
    private MerchantInfoEntity translateRequestToEntity(String userId,ShopIdentifyRequest shopIdentifyRequest){
        MerchantInfoEntity merchant = new MerchantInfoEntity();
        BeanUtils.copyProperties(shopIdentifyRequest,merchant);

        return merchant;
    }

}

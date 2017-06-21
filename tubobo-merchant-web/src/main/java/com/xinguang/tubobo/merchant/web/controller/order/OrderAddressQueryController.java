package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.AddressDTO;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.v2.ReqOrderQuery;
import com.xinguang.tubobo.merchant.web.response.order.v2.RespOrderQueryV2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 收货人地址自动补全
 * 根据手机号查询
 */
@Controller
@RequestMapping("/order/address/autoComplete")
public class OrderAddressQueryController extends MerchantBaseController<ReqOrderQuery,RespOrderQueryV2> {

    private final static int NUM = 5;//查询的条数

    @Autowired
    private AdminToMerchantService adminToMerchantService;
    @Autowired
    private MerchantInfoService merchantInfoService;

    @Override
    protected RespOrderQueryV2 doService(String userId, ReqOrderQuery req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        String phone = req.getKeyword();
        RespOrderQueryV2 resp = new RespOrderQueryV2();
        List<AddressDTO> lists = adminToMerchantService.queryAddressRecords(userId, phone, NUM);
        if(CollectionUtils.isNotEmpty(lists)){
            resp.setAddressList(lists);
        }
        return resp;
    }
}

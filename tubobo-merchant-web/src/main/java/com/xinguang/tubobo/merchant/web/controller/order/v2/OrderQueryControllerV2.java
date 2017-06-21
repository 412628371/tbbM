package com.xinguang.tubobo.merchant.web.controller.order.v2;

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
 * Created by XG on 2017/6/20.
 */
@Controller
@RequestMapping("/2.0/order/query")
public class OrderQueryControllerV2 extends MerchantBaseController<ReqOrderQuery,RespOrderQueryV2> {

    private final static int NUM = 5;

    @Autowired
    private AdminToMerchantService adminToMerchantService;
    @Autowired
    MerchantInfoService merchantInfoService;

    @Override
    protected RespOrderQueryV2 doService(String userId, ReqOrderQuery req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        String phone = req.getPhone();
        RespOrderQueryV2 resp = new RespOrderQueryV2();
        List<AddressDTO> lists = adminToMerchantService.queryAddressRecords(userId, phone, NUM);
        if(CollectionUtils.isNotEmpty(lists)){
            resp.setAddressList(lists);
        }
        return resp;
    }
}

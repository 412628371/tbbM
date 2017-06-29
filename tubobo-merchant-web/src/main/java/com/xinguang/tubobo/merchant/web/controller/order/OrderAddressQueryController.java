package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.AddressDTO;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.common.info.AddressInfo;
import com.xinguang.tubobo.merchant.web.request.order.ReqAddressInfoQuery;
import com.xinguang.tubobo.merchant.web.response.order.RespAddressInfoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;

/**
 * 收货人地址自动补全
 * 根据手机号查询
 */
@Controller
@RequestMapping("/order/address/autoComplete")
public class OrderAddressQueryController extends MerchantBaseController<ReqAddressInfoQuery,RespAddressInfoQuery> {

    private final static int NUM = 20;//查询的条数

    @Autowired
    private AdminToMerchantService adminToMerchantService;
    @Autowired
    private MerchantInfoService merchantInfoService;

    @Override
    protected RespAddressInfoQuery doService(String userId, ReqAddressInfoQuery req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        String phone = req.getKeyword();
        RespAddressInfoQuery resp = new RespAddressInfoQuery();
        List<AddressDTO> dtolists = adminToMerchantService.queryAddressRecords(userId, phone, NUM);
        List<AddressInfo> lists = getAddressInfoList(dtolists);
        resp.setKeyword(phone);
        resp.setList(lists);
        if(lists==null || lists.size()==0){
            resp.setTotalSize(0);
        }else{
            resp.setTotalSize(lists.size());
        }
        return resp;
    }

    public List<AddressInfo> getAddressInfoList(List<AddressDTO> lists){
        List<AddressInfo> infolists = new ArrayList<AddressInfo>();
        AddressInfo a = new AddressInfo();
        for(AddressDTO dto : lists){
            a.setAddressProvince(dto.getProvince());
            a.setAddressCity(dto.getCity());
            a.setAddressStreet(dto.getStreet());
            a.setAddressDetail(dto.getDetailAddress());
            a.setAddressDistrict(dto.getDistrict());
            a.setAddressRoomNo(dto.getRoomNo());
            a.setName(dto.getName());
            a.setTelephone(dto.getPhone());
            a.setLatitude(dto.getLatitude());
            a.setLongitude(dto.getLongitude());
            infolists.add(a);
        }
        return infolists;
    }
}
